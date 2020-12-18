package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSku;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateSkuVo
{
    @NotNull
    @NotEmpty
    private String sn;
    private String name;
    private Long originalPrice;
    private String configuration;
    private Long weight;
    private String imageUrl;
    private Integer inventory;
    private String detail;

    public GoodsSku asNewSku()
    {
        GoodsSku goodsSku=new GoodsSku();
        goodsSku.setSkuSn(sn);
        goodsSku.setName(name);
        goodsSku.setOriginalPrice(originalPrice);
        goodsSku.setConfiguration(configuration);
        goodsSku.setWeight(weight);
        goodsSku.setImageUrl(imageUrl);
        goodsSku.setInventory(inventory);
        goodsSku.setDetail(detail);
        goodsSku.setGmtCreate(LocalDateTime.now());
        goodsSku.setGmtModified(goodsSku.getGmtCreate());
        return goodsSku;
    }
}
