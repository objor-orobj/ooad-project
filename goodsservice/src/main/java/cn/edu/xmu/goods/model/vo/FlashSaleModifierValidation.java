package cn.edu.xmu.goods.model.vo;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
public class FlashSaleModifierValidation {
    @NotEmpty
    private LocalDateTime flashDate;
}
