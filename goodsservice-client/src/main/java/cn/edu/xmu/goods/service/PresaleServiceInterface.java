package cn.edu.xmu.goods.service;

public interface PresaleServiceInterface {
    //根据商品skuId扣除预售商品库存
    Boolean deductPresaleInventory(Long skuId, Integer amount);

    // 根据商品skuId增加预售商品库存
    Boolean increasePresaleInventory(Long skuId, Integer amount);

    // 根据（预售）商品skuId获得预售商品定金
    Long getEarnestBySkuId(Long skuId);
}
