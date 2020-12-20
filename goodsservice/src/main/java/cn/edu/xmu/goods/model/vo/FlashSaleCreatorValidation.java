package cn.edu.xmu.goods.model.vo;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class FlashSaleCreatorValidation {
//    @NotEmpty
    private LocalDateTime flashDate;
}
