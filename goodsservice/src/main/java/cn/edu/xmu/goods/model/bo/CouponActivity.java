package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.CouponActivityPo;
import cn.edu.xmu.goods.model.vo.CouponActivityCreatorValidation;
import cn.edu.xmu.goods.model.vo.CouponActivityModifierValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CouponActivity implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State implements Serializable{
        OFFLINE(0, "已下线"),
        ONLINE(1, "已上线"),
        DELETED(2, "已删除");
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
    }

    public enum Type {
        SINGLE_MAXIMUM(0, "每人数量"),
        LIMITED_INVENTORY(1, "总数控制");
        private final Integer literal;
        private final String description;

        Type(Integer literal, String description) {
            this.literal = literal;
            this.description = description;
        }

        public Integer getLiteral() {
            return literal;
        }

        public String getDescription() {
            return description;
        }
    }

    public CouponActivity(CouponActivityCreatorValidation vo) {
        this.name = vo.getName();
        this.quantity = vo.getQuantity();
        this.type = vo.getQuantityType() == 0
                ? Type.SINGLE_MAXIMUM
                : Type.LIMITED_INVENTORY;
        this.validTerm = vo.getValidTerm();
        this.couponTime = vo.getCouponTime();
        this.beginTime = vo.getBeginTime();
        this.endTime = vo.getEndTime();
        this.strategy = vo.getStrategy();
    }

    public CouponActivity(CouponActivityPo po) {
        this.id = po.getId();
        this.name = po.getName();
        this.beginTime = po.getBeginTime();
        this.endTime = po.getEndTime();
        this.couponTime = po.getCouponTime();
        this.state = State.values()[po.getState().intValue()];
        this.quantity = po.getQuantity();
        this.type = Type.values()[po.getQuantitiyType().intValue()];
        this.validTerm = po.getValidTerm().intValue();
        this.imageUrl = po.getImageUrl();
        this.strategy = po.getStrategy();
        this.modifierId = po.getModiBy();
        this.gmtCreated = po.getGmtCreate();
        this.modifierId = po.getModiBy();
    }

    public CouponActivityPo toCouponActivityPo() {
        CouponActivityPo po = new CouponActivityPo();
        po.setId(id);
        po.setName(name);
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setCouponTime(couponTime);
        po.setState(state.getCode().byteValue());
        po.setShopId(shopId);
        po.setQuantity(quantity);
        po.setQuantitiyType(type.getLiteral().byteValue());
        po.setValidTerm(validTerm.byteValue());
        po.setImageUrl(imageUrl);
        po.setStrategy(strategy);
        po.setCreatedBy(creatorId);
        po.setModiBy(modifierId);
        po.setGmtCreate(gmtCreated);
        po.setGmtModified(gmtModified);
        return po;
    }

    public CouponActivity(CouponActivityModifierValidation vo) {
        this.name = vo.getName();
        this.quantity = vo.getQuantity();
        this.beginTime = vo.getBeginTime();
        this.endTime = vo.getEndTime();
        this.strategy = vo.getStrategy();
    }

    public CouponActivity() {
    }

    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime couponTime;
    private State state;
    private Long shopId;
    private Integer quantity;
    private Type type;
    private Integer validTerm;
    private String imageUrl;
    private String strategy;
    private Long creatorId;
    private Long modifierId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
