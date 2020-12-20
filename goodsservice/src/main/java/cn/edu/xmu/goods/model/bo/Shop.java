package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopCreatorValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Shop {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State {
        PENDING_APPROVAL(0, "未审核"),
        OFFLINE(1, "未上线"),
        ONLINE(2, "上线"),
        CLOSED(3, "关闭"),
        REJECTED(4, "审核未通过");
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
                    return PENDING_APPROVAL;
                case 1:
                    return OFFLINE;
                case 2:
                    return ONLINE;
                case 3:
                    return CLOSED;
                case 4:
                    return REJECTED;
                default:
                    return null;
            }
        }
    }

    private Long id;
    private String name;
    private State state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;


    public ShopPo toShopPo() {
        ShopPo po = new ShopPo();
        po.setId(id);
        po.setName(name);
        po.setState(state == null ? null : state.getCode().byteValue());
        po.setGmtCreate(gmtCreate);
        po.setGmtModified(gmtModified);
        return po;
    }

    public Shop(ShopPo po) {
        this.id = po.getId();
        this.name = po.getName();
        if (po.getState() != null)
            this.state = State.fromCode(po.getState().intValue());
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public Shop() {
    }

    public Shop(ShopCreatorValidation vo) {
        name = vo.getName();
    }
}
