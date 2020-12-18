package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.FloatPricePo;
import cn.edu.xmu.goods.model.po.FloatPricePoExample;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ReturnGoodsSkuVo
{
    @NotNull
    @NotEmpty
    private Long id;
    private String name;
    private String skuSn;
    private String imageUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private Boolean disabled;

    public ReturnGoodsSkuVo(GoodsSkuPo po,Long price)
    {
        this.id=po.getId();
        this.name=po.getName();
        this.skuSn=po.getSkuSn();
        this.imageUrl=po.getImageUrl();
        this.inventory=po.getInventory();
        this.originalPrice=po.getOriginalPrice();
        this.disabled =po.getDisabled()==1;
        if(price.equals((long) -1)) this.price=originalPrice;
        else
        {
            this.price=price;
        }
    }
}

