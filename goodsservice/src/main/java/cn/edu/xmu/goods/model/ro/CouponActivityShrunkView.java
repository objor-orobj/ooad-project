package cn.edu.xmu.goods.model.ro;

import cn.edu.xmu.goods.model.bo.CouponActivity;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class CouponActivityShrunkView {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Integer quantity;
    private final LocalDateTime beginTime;
    private final LocalDateTime endTime;
    private final LocalDateTime couponTime;

    public CouponActivityShrunkView(CouponActivity bo) {
        this.id = bo.getId();
        this.name = bo.getName();
        this.imageUrl = bo.getImageUrl();
        this.quantity = bo.getQuantity();
        this.beginTime = bo.getBeginTime();
        this.endTime = bo.getEndTime();
        this.couponTime = bo.getCouponTime();
    }
}
