package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.dto.CouponActivityDTO;

public interface CouponServiceInterface {
    CouponActivityDTO getCouponActivityAlone(Long userId, Long goodsSkuId);
}
