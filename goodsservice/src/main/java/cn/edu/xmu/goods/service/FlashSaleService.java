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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlashSaleService {
    @Autowired
    private FlashSaleDao flashSaleDao;
    @Autowired
    private GoodsSkuDao goodsSkuDao;
    @DubboReference(version="0.0.1")
    private TimeServiceInterface timeSegmentService;


    public ResponseEntity<StatusWrap> createWithinTimeSegment(FlashSaleCreatorValidation vo, Long timeSegId) {
        FlashSaleTimeSegmentDTO timeSegment = timeSegmentService.getFlashSaleTimeSegmentById(timeSegId);
        if (timeSegment == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        FlashSale create = new FlashSale(vo);
        create.setTimeSegId(timeSegId);
        create.setState(FlashSale.State.OFFLINE);
        create.setGmtCreate(LocalDateTime.now());
        FlashSale saved = flashSaleDao.createActivity(create);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.of(new FlashSaleWithTimeSegmentView(saved, timeSegment));
    }

    public ResponseEntity<StatusWrap> modifyInfo(Long activityId, FlashSaleModifierValidation vo) {
        FlashSale origin = flashSaleDao.selectActivity(activityId);
        if (origin == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
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
        if (activity == null)
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
        if (activity == null)
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
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (activity.getState() == FlashSale.State.DELETED)
            return StatusWrap.ok();
        activity.setState(FlashSale.State.DELETED);
        FlashSale saved = flashSaleDao.updateActivity(activity);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> insertItem(Long saleId, FlashSaleItemCreatorValidation vo) {
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
        return StatusWrap.of(new FlashSaleItemExtendedView(saved, retSkuVo));
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
        List<Long> ids = timeSegmentService.getCurrentFlashSaleTimeSegs();
        if(ids==null||ids.size()<=0)return null;
        System.out.println(ids.size());
        // TODO change this !!!
        return flashSaleDao.getAllFlashSaleItemsWithinTimeSegments(ids);
    }

    public Flux<FlashSaleItemExtendedView> getFlashSaleItemsWithinTimeSegment(Long id) {
        if (!timeSegmentService.timeSegIsFlashSale(id)) return null;
        return flashSaleDao.getAllFlashSaleItemsWithinTimeSegments(Collections.singletonList(id));
    }
}
