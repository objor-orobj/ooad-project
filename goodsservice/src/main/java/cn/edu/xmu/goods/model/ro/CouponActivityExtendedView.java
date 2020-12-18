package cn.edu.xmu.goods.model.ro;

import cn.edu.xmu.goods.model.bo.CouponActivity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponActivityExtendedView {
    private final Long id;
    private final String name;
    private final Integer state;
    private final ShopIdAndNameView shop;
    private final Integer quantity;
    private final Integer quantityType;
    private final Integer validTerm;
    private final String imageUrl;
    private final LocalDateTime beginTime;
    private final LocalDateTime endTime;
    private final LocalDateTime couponTime;
    private final String strategy;
    private final UserIdAndView createdBy;
    private final UserIdAndView ModiBy;
    private final LocalDateTime gmtCreate;
    private final LocalDateTime gmtModified;

    public CouponActivityExtendedView(
            CouponActivity bo,
            ShopIdAndNameView shop,
            UserIdAndView creator,
            UserIdAndView modifier) {
        this.id = bo.getId();
        this.name = bo.getName();
        this.state = bo.getState().getCode();
        this.shop = shop;
        this.quantity = bo.getQuantity();
        this.quantityType = bo.getType().getLiteral();
        this.validTerm = bo.getValidTerm();
        this.imageUrl = bo.getImageUrl();
        this.beginTime = bo.getBeginTime();
        this.endTime = bo.getEndTime();
        this.couponTime = bo.getCouponTime();
        this.strategy = bo.getStrategy();
        this.createdBy = creator;
        this.ModiBy = modifier;
        this.gmtCreate = bo.getGmtCreated();
        this.gmtModified = bo.getGmtModified();
    }
}
