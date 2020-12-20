package cn.edu.xmu.goods.model.vo;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Data
public class FlashSaleItemCreatorValidation {
//    @NotNull
    private Long skuId;
//    @NotNull
    private Long price;
//    @NotNull
    private Integer quantity;
}
