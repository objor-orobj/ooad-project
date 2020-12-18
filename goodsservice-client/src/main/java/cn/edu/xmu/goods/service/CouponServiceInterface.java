package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.dto.CouponActivityDTO;

import java.util.ArrayList;

public interface CouponServiceInterface {
    ArrayList<CouponActivityDTO> getCouponActivityAlone(Long userId, Long goodsSkuId);

    Boolean deleteCoupon(Long couponId, Long userId);
}
