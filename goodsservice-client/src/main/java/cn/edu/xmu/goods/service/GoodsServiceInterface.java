package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.dto.GoodsInfoDTO;
import cn.edu.xmu.goods.model.dto.GoodsSkuDTO;

import java.util.List;

public interface GoodsServiceInterface {
    // 根据商品skuId判断（非预售非秒杀商品）库存是否可以满足其购买量
    boolean compareInventoryBySkuId(Long skuId, Integer amount);

    // 根据商品skuId扣除（非预售非秒杀商品）库存
    boolean deductInventory(Long skuId, Integer amount);

    // 根据商品skuId增加（非预售非秒杀商品）库存
    boolean increaseInventory(Long skuId, Integer amount);


    // 根据商品skuId获得商品价格
    Long findPriceBySkuId(Long skuId);

    // 根据商品skuId获得商品名称
    String getGoodsNameBySkuId(Long skuId);

    // 根据skuId获取商品sku信息
    GoodsSkuDTO getSkuById(Long skuId);

    // 查询一个skuId是否存在
    Boolean hasGoodsSku(Long skuId);

    //查询一个sku是否属于一个店铺
    Boolean hasGoodsSku(Long skuId,Long shopId);

    //通过两个skuId判断是不是属于一个spu，相同也是true
    Boolean anbleChange(Long newGoodSkuId, Long goodSkuId);

    // 根据商品skuId获得商品信息
    GoodsInfoDTO getGoodsInfoDTOBySkuId(Long skuId);
}
