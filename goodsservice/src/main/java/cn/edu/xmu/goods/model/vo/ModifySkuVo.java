package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSku;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModifySkuVo
{
    private String name;
    private Long originalPrice;
    private String configuration;
    private Long weight;
    private Integer inventory;
    private String detail;

    public GoodsSku asNewSku()
    {
        GoodsSku goodsSku=new GoodsSku();
        goodsSku.setName(name);
        goodsSku.setOriginalPrice(originalPrice);
        goodsSku.setConfiguration(configuration);
        goodsSku.setWeight(weight);
        goodsSku.setInventory(inventory);
        goodsSku.setDetail(detail);
        goodsSku.setGmtModified(LocalDateTime.now());
        return goodsSku;
    }
}
