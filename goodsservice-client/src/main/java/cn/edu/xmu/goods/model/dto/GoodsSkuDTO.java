package cn.edu.xmu.goods.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsSkuDTO implements Serializable {
    private Long id;
    private Long shopId;
    private String name;
    private String skuSn;
    private String imgUrl;
    private Integer inventory;
    private Integer originalPrice;
    private Integer price;
    private Boolean disable;
}
