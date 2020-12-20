package cn.edu.xmu.goods.model.ro;

import cn.edu.xmu.goods.model.bo.Shop;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class ShopView {
    private final Long id;
    private final String name;
    private final Byte state;
    private final LocalDateTime gmtCreate;
    private final LocalDateTime gmtModified;

    public ShopView(Shop bo) {
        this.id = bo.getId();
        this.name = bo.getName();
        this.state = bo.getState().getCode().byteValue();
        this.gmtCreate = bo.getGmtCreate();
        this.gmtModified = bo.getGmtModified();
    }
}
