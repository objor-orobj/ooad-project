package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.CouponPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Coupon {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State {
        FREE(0, "未领取"),
        TAKEN(1, "已领取"),
        USED(2, "已使用"),
        INVALID(3, "已失效");

        private final Integer code;
        private final String name;

        State(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static State fromCode(Integer code) {
            switch (code) {
                case 0:
                    return FREE;
                case 1:
                    return TAKEN;
                case 2:
                    return USED;
                case 3:
                    return INVALID;
                default:
                    return null;
            }
        }
    }

    private Long id;
    private String couponSn;
    private String name;
    private Long customerId;
    private Long activityId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private State state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Coupon(CouponPo po) {
        this.id = po.getActivityId();
        this.couponSn = po.getCouponSn();
        this.name = po.getName();
        this.customerId = po.getCustomerId();
        this.activityId = po.getActivityId();
        this.beginTime = po.getBeginTime();
        this.endTime = po.getEndTime();
        this.state = State.fromCode(po.getState().intValue());
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public CouponPo toCouponPo() {
        CouponPo po = new CouponPo();
        po.setId(id);
        po.setCouponSn(couponSn);
        po.setName(name);
        po.setCustomerId(customerId);
        po.setActivityId(activityId);
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setState(state.getCode().byteValue());
        po.setGmtCreate(gmtCreate);
        po.setGmtModified(gmtModified);
        return po;
    }
}
