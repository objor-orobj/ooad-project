package cn.edu.xmu.goods.model.vo;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ShopModifierValidation {
    @NotEmpty
    private String name;
}
