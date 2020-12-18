package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.ro.ShopIdAndNameView;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleActivityCreateVo {

    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime payTime;
    private LocalDateTime endTime;
    private String state;
    private ShopIdAndNameView shop;
    private ReturnGoodsSkuVo goodsSku;
    private Integer quantity;
    private Long advancePayPrice;
    private Long restPayPrice;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public PresaleActivityCreateVo(Shop shop, ReturnGoodsSkuVo goodsSku, PresaleActivityPo po)
    {
        this.shop = new ShopIdAndNameView(shop);
        this.goodsSku = goodsSku;
        this.id = po.getId();
        this.name = po.getName();
        this.beginTime = po.getBeginTime();
        this.payTime = po.getPayTime();
        this.endTime = po.getEndTime();
        this.state = po.getState().toString();
        this.quantity =po.getQuantity();
        this.advancePayPrice = po.getAdvancePayPrice();
        this.restPayPrice = po.getRestPayPrice();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }
}
