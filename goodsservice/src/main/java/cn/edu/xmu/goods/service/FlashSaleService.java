package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.FlashSaleDao;
import cn.edu.xmu.goods.dao.GoodsSkuDao;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.FlashSale;
import cn.edu.xmu.other.model.dto.FlashSaleTimeSegmentDTO;
import cn.edu.xmu.goods.model.po.FlashSaleItemPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.ro.FlashSaleItemExtendedView;
import cn.edu.xmu.goods.model.ro.FlashSaleWithTimeSegmentView;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.other.service.TimeServiceInterface;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DubboService(version = "0.0.1")
@Service
public class FlashSaleService implements FlashSaleServiceInterface {
    @Autowired
    private FlashSaleDao flashSaleDao;
    @Autowired
    private GoodsSkuDao goodsSkuDao;
    @DubboReference(version = "0.0.1")
    private TimeServiceInterface timeSegmentService;

    private static final Logger logger = LoggerFactory.getLogger(FlashSaleService.class);

    public ResponseEntity<StatusWrap> createWithinTimeSegment(FlashSaleCreatorValidation vo, Long timeSegId) {
        if (vo.getFlashDate() == null
                || vo.getFlashDate().isBefore(LocalDate.now().atTime(LocalTime.MAX))) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        FlashSaleTimeSegmentDTO timeSegment = null;
        try {
            timeSegment = timeSegmentService.getFlashSaleTimeSegmentById(timeSegId);
        } catch (Exception exception) {
            logger.error("error fetching timeSeg info");
            exception.printStackTrace();
        }
        if (timeSegment == null) {
            logger.error("timeSeg id invalid");
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        if (flashSaleDao.timeSegConflict(vo.getFlashDate(), timeSegId)) {
            logger.debug("timeSeg conflict");
            return StatusWrap.just(Status.TIMESEG_CONFLICT);
        }
        FlashSale create = new FlashSale(vo);
        create.setTimeSegId(timeSegId);
        create.setState(FlashSale.State.OFFLINE);
        create.setGmtCreate(LocalDateTime.now());
        FlashSale saved = flashSaleDao.createActivity(create);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.of(new FlashSaleWithTimeSegmentView(saved, timeSegment), HttpStatus.CREATED);
    }

    public ResponseEntity<StatusWrap> modifyInfo(Long activityId, FlashSaleModifierValidation vo) {
        logger.debug("modified activity: " + activityId + ", flash date: " + vo.getFlashDate());
        FlashSale origin = flashSaleDao.selectActivity(activityId);
        if (origin == null || origin.getState() == FlashSale.State.DELETED)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        logger.debug("activity state: " + origin.getState());
        if (vo.getFlashDate() == null
                || vo.getFlashDate().isBefore(LocalDate.now().atTime(LocalTime.MAX))) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        if (origin.getState() != FlashSale.State.OFFLINE)
            return StatusWrap.just(Status.FLASH_SALE_STATE_DENIED);
        FlashSale modified = new FlashSale(vo);
        modified.setId(activityId);
        FlashSale saved = flashSaleDao.updateActivity(modified);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringOnline(Long id) {
        FlashSale activity = flashSaleDao.selectActivity(id);
        if (activity == null || activity.getState() == FlashSale.State.DELETED)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (activity.getState() == FlashSale.State.ONLINE)
            return StatusWrap.ok();
        if (activity.getState() != FlashSale.State.OFFLINE)
            return StatusWrap.just(Status.FLASH_SALE_STATE_DENIED);
        activity.setState(FlashSale.State.ONLINE);
        FlashSale saved = flashSaleDao.updateActivity(activity);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringOffline(Long id) {
        FlashSale activity = flashSaleDao.selectActivity(id);
        if (activity == null || activity.getState() == FlashSale.State.DELETED)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (activity.getState() == FlashSale.State.OFFLINE)
            return StatusWrap.ok();
        if (activity.getState() != FlashSale.State.ONLINE)
            return StatusWrap.just(Status.FLASH_SALE_STATE_DENIED);
        activity.setState(FlashSale.State.OFFLINE);
        FlashSale saved = flashSaleDao.updateActivity(activity);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }


    public ResponseEntity<StatusWrap> forceCancel(Long id) {
        FlashSale activity = flashSaleDao.selectActivity(id);
        if (activity == null || activity.getState() == FlashSale.State.DELETED)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (activity.getState() == FlashSale.State.ONLINE)
            return StatusWrap.just(Status.FLASH_SALE_STATE_DENIED);
        activity.setState(FlashSale.State.DELETED);
        FlashSale saved = flashSaleDao.updateActivity(activity);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> insertItem(Long saleId, FlashSaleItemCreatorValidation vo) {
        if (vo.getSkuId() == null || vo.getPrice() == null || vo.getQuantity() == null)
            return StatusWrap.just(Status.FIELD_NOTVALID);
        FlashSale sale = flashSaleDao.selectActivity(saleId);
        GoodsSkuPo sku = goodsSkuDao.getSkuPoById(vo.getSkuId().intValue());
        if (sale == null || sku == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        FlashSale.Item create = new FlashSale.Item(vo);
        create.setSaleId(saleId);
        create.setGmtCreate(LocalDateTime.now());
        FlashSale.Item saved = flashSaleDao.insertItem(create);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        ReturnGoodsSkuVo retSkuVo = goodsSkuDao.getSingleSimpleSku(vo.getSkuId().intValue());
        return StatusWrap.of(new FlashSaleItemExtendedView(saved, retSkuVo), HttpStatus.CREATED);
    }

    public ResponseEntity<StatusWrap> removeItem(Long id) {
        FlashSale.Item item = flashSaleDao.selectItem(id);
        if (item == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        FlashSale parent = flashSaleDao.selectActivity(item.getSaleId());
        if (parent == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (parent.getState() == FlashSale.State.DELETED)
            return StatusWrap.just(Status.FLASH_SALE_STATE_DENIED);
        Long deleted = flashSaleDao.deleteItem(id);
        if (deleted == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public Flux<FlashSaleItemExtendedView> getCurrentFlashSaleItems() {
        ArrayList<Long> ids = null;
        try {
            ids = timeSegmentService.getCurrentFlashSaleTimeSegs();
        } catch (Exception exception) {
            logger.error("error fetching timeSeg");
            exception.printStackTrace();
        }
        if (ids == null || ids.size() <= 0) {
            logger.error(" empty timeSeg list");
            return Flux.empty();
        }
        return flashSaleDao.getAllFlashSaleItemsWithinTimeSegments(ids);
    }

    public Flux<FlashSaleItemExtendedView> getFlashSaleItemsWithinTimeSegment(Long id) {
        if (!timeSegmentService.timeSegIsFlashSale(id)) return null;
        return flashSaleDao.getAllFlashSaleItemsWithinTimeSegments(Collections.singletonList(id));
    }

    @Override
    public Boolean setFlashSaleSegId(Long segId) {
        List<FlashSale> sales = flashSaleDao.selectActivityOfTimeSegment(segId);
        if (sales == null || sales.size() == 0)
            return true;
        for (FlashSale origin : sales) {
            origin.setState(FlashSale.State.OFFLINE);
            origin.setTimeSegId(0L);
            FlashSale saved = flashSaleDao.updateActivity(origin);
            if (saved == null)
                return false;
        }
        return true;
    }
}
