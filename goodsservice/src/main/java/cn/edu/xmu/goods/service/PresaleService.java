package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.*;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.*;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@DubboService(version = "0.0.1")
public class PresaleService implements PresaleServiceInterface {
    @Autowired
    private PresaleActivityDao presaleActivityDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private GoodsSkuDao goodsSkuDao;

    //PRESALE
    public ResponseEntity<StatusWrap> createPresaleActivity(Long shopid, Long id, PresaleActivityVo vo) {
        if (vo.getName() == null || vo.getName().isEmpty() || vo.getName().isBlank())
            return StatusWrap.just(Status.FIELD_NOTVALID);
        Shop shop = shopDao.select(shopid);
        ReturnGoodsSkuVo goodsSku = goodsSkuDao.getSingleSimpleSku(id.intValue());
        if (vo.getBeginTime().isBefore(LocalDateTime.now())
                || vo.getPayTime().isBefore(LocalDateTime.now())
                || vo.getEndTime().isBefore(LocalDateTime.now())
                || vo.getQuantity() < 0
                || vo.getAdvancePayPrice() < 0
                || vo.getRestPayPrice() < 0
        ) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        if (shop.getState() != Shop.State.ONLINE) {
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        }
        if (goodsSku == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        PresaleActivity presaleActivity = new PresaleActivity(shop, goodsSku, vo);
        return presaleActivityDao.createPresaleActivity(presaleActivity);
    }

    public ResponseEntity<StatusWrap> getPresaleActivity(PresaleActivityInVo vo) {
        return presaleActivityDao.getPresaleActivity(vo);
    }

    public ResponseEntity<StatusWrap> getallPresaleActivity(PresaleActivityInVo vo) {
        return presaleActivityDao.getallPresaleAcitvity(vo);
    }

    public ResponseEntity<StatusWrap> modifyPresaleActivityById(Long id, PresaleActivityModifyVo vo) {
        return presaleActivityDao.modifyPresaleActivity(id, vo);
    }

    public ResponseEntity<StatusWrap> PtoONLINE(Long shopId, Long Id) {
        return presaleActivityDao.PtoONLINE(shopId, Id);
    }

    public ResponseEntity<StatusWrap> PtoOFFLINE(Long shopId, Long Id) {
        return presaleActivityDao.PtoOFFLINE(shopId, Id);
    }

    public ResponseEntity<StatusWrap> deletePresaleActivity(Long shopId, Long id) {
        return presaleActivityDao.deletePresaleActivityById(shopId, id);
    }

    @Override
    public Boolean deductPresaleInventory(Long skuId, Integer amount) {
        return presaleActivityDao.deductPresaleActivityquantity(skuId, amount);
    }

    @Override
    public Boolean increasePresaleInventory(Long skuId, Integer amount) {
        return presaleActivityDao.plusPresaleActivityquantity(skuId, amount);
    }


    @Override
    public Long getEarnestBySkuId(Long skuId) {
        return presaleActivityDao.getearnestBySkuId(skuId);
    }
}
