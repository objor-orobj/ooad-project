package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReturnWholeGoodsSkuVo
{
    private Long id;
    private String name;
    private String skuSn;
    private String detail;
    private String imageUrl;
    private Long originalPrice;
    private Long price;
    private Integer inventory;
    private Byte state;
    private String configuration;
    private Long weight;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private ReturnGoodsSpuVo spu=new ReturnGoodsSpuVo();
    private Boolean disabled;

    public ReturnWholeGoodsSkuVo(GoodsSkuPo po,Long price)
    {
        this.id=po.getId();
        this.name=po.getName();
        this.skuSn=po.getSkuSn();
        this.detail=po.getDetail();
        this.imageUrl=po.getImageUrl();
        this.originalPrice=po.getOriginalPrice();
        if(price.equals((long) -1)) this.price=originalPrice;
        else
        {
            this.price=price;
        }
        this.inventory=po.getInventory();
        this.state=po.getState();
        this.configuration=po.getConfiguration();
        this.weight=po.getWeight();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.disabled = po.getDisabled()==1;
    }
}
