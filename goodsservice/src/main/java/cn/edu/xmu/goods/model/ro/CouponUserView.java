package cn.edu.xmu.goods.model.ro;

import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import lombok.Data;
import lombok.Getter;

@Data
public class CouponUserView {
    private final Long id;
    private final CouponActivityShrunkView activity;
    private final String name;
    private final String couponSn;

    public CouponUserView(Coupon coupon, CouponActivity activity) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.couponSn = coupon.getCouponSn();
        this.activity = new CouponActivityShrunkView(activity);
    }
}
