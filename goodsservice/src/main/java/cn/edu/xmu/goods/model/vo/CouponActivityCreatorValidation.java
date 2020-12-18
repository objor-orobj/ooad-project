package cn.edu.xmu.goods.model.vo;

import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
public class CouponActivityCreatorValidation {
    @NotEmpty
    private String name;
    @PositiveOrZero
    @NotNull
    private Integer quantity;
    @Max(1)
    @Min(0)
    @NotNull
    private Integer quantityType;
    @NotNull
    private Integer validTerm;
    @NotNull
    private LocalDateTime couponTime;
    @NotNull
    private LocalDateTime beginTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private String strategy;
}
