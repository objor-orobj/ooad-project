package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.GoodsSkuDao;
import cn.edu.xmu.goods.dao.GoodsSpuDao;
import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.dto.GoodsInfoDTO;
import cn.edu.xmu.goods.model.dto.GoodsSkuDTO;
import cn.edu.xmu.goods.model.dto.GoodsSkuInfo;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.privilegeservice.client.IUserService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@DubboService(version = "0.0.1")
public class GoodsService implements GoodsServiceInterface {
    @Autowired
    private GoodsSkuDao goodsSkuDao;

    @Autowired
    private GoodsSpuDao goodsSpuDao;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IUserService userService;

    @Override
    public boolean compareInventoryBySkuId(Long skuId, Integer amount) {

        return goodsSkuDao.compareInventoryBySkuId(skuId, amount);
    }

    @Override
    public boolean deductInventory(Long skuId, Integer amount) {
        return goodsSkuDao.deductInventory(skuId, amount);
    }

    @Override
    public boolean increaseInventory(Long skuId, Integer amount) {
        return goodsSkuDao.increaseInventory(skuId, amount);
    }

    @Override
    public Long findPriceBySkuId(Long skuId) {
        return goodsSkuDao.findPriceBySkuId(skuId);
    }

    @Override
    public String getGoodsNameBySkuId(Long skuId) {
        return goodsSkuDao.getGoodsNameBySkuId(skuId);
    }

    @Override
    public GoodsSkuDTO getSkuById(Long skuId) {
        return goodsSkuDao.getSkuById(skuId);
    }

    @Override
    public Boolean hasGoodsSku(Long skuId) {
        return goodsSkuDao.selectGoodsForCustomer(skuId)!=null;
    }

    @Override
    public Boolean hasGoodsSku(Long skuId,Long shopId)
    {
        return goodsSkuDao.isBelongShop(skuId,shopId);
    }

    @Override
    public Boolean anbleChange(Long newGoodSkuId, Long goodSkuId) {
        return goodsSkuDao.anbleChange(newGoodSkuId, goodSkuId);
    }

    @Override
    public GoodsInfoDTO getGoodsInfoDTOBySkuId(Long skuId) {
        return goodsSkuDao.getGoodsInfoDTOBySkuId(skuId);
    }

    //TODO 需求api
    @Override
    public GoodsSkuInfo getGoodsSkuInfoAlone(Long goodsSkuId)
    {
        return null;
    }

    public ResponseEntity<StatusWrap> getGoodsSkus(GetGoodsSkuVo getSkuVo) {
        return goodsSkuDao.getGoodsSkus(getSkuVo);
    }

    public ResponseEntity<StatusWrap> getSkuDetailedById(Long id) {
        return goodsSkuDao.getSkuDetailedById(id);
    }

    public ResponseEntity<StatusWrap> createSku(Long shopId, Long goodsSpuId, CreateSkuVo vo) {
        return goodsSkuDao.createSku(shopId, goodsSpuId, vo);
    }

    public ResponseEntity<StatusWrap> uploadSkuImg(Long shopId, Long skuId, String img) {
        return goodsSkuDao.uploadSkuImg(shopId, skuId, img);
    }

    public ResponseEntity<StatusWrap> deleteSku(Long skuId) {
        return goodsSkuDao.deleteSku(skuId);
    }

    public ResponseEntity<StatusWrap> updateSku(Long skuId, ModifySkuVo modifySkuVo) {
        return goodsSkuDao.updateSku(skuId, modifySkuVo);
    }

    public ResponseEntity<StatusWrap> getSpuById(Long spuId) {
        return goodsSpuDao.getSpuById(spuId);
    }

    public ResponseEntity<StatusWrap> getSkuBySid(Long sid,Long skuId) {
        GoodsSkuPo po= goodsSkuDao.getSkuPoById(skuId.intValue());
        if(po==null||po.getDisabled()==1||po.getState()!=4) return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        return goodsSpuDao.getSpuById(po.getGoodsSpuId());
    }

    public ResponseEntity<StatusWrap> createSpu(Long id, GoodsSpuVo vo) {
        return goodsSpuDao.createSpu(id, vo);
    }

    public ResponseEntity<StatusWrap> uploadSpuImg(Long shopId, Long spuId, String img) {
        return goodsSpuDao.uploadSpuImg(shopId, spuId, img);
    }

    public ResponseEntity<StatusWrap> updateSpu(Long spuId, GoodsSpuVo vo) {
        return goodsSpuDao.updateSpu(spuId, vo);
    }

    public ResponseEntity<StatusWrap> deleteSpu(Long shopId, Long spuId) {
        return goodsSpuDao.deleteSpu(shopId, spuId);
    }

    public ResponseEntity<StatusWrap> putGoodsOnSale(Long spuId) {
        return goodsSkuDao.putGoodsOnSale( spuId);
    }

    public ResponseEntity<StatusWrap> putOffGoodsOnSale( Long spuId) {
        return goodsSkuDao.putOffGoodsOnSale(spuId);
    }

    public ResponseEntity<StatusWrap> addFloatingPrice( Long userId,Long skuId, FloatPricesGetVo vo) {
        return goodsSkuDao.addFloatingPrice(userId,userService.getUserName(userId),skuId, vo);
    }

    public ResponseEntity<StatusWrap> invalidFloatPrice(Long userId, Long floatId) {
        return goodsSkuDao.invalidFloatPrice(userId,floatId);
    }
}
