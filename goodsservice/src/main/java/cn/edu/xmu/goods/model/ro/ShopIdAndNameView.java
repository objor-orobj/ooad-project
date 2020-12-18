package cn.edu.xmu.goods.model.ro;

import cn.edu.xmu.goods.model.bo.Shop;
import lombok.Getter;

@Getter
public class ShopIdAndNameView {
    private final Long id;
    private final String name;

    public ShopIdAndNameView(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
    }
}
