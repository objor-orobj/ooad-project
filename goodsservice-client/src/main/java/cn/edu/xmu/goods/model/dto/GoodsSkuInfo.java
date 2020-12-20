package cn.edu.xmu.goods.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsSkuInfo implements Serializable {
    private String skuName;
    private Long price;
}
