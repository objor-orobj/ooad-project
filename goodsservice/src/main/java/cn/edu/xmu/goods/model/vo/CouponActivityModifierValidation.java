package cn.edu.xmu.goods.model.vo;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponActivityModifierValidation {
    private String name;
    private Integer quantity;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String strategy;
}
