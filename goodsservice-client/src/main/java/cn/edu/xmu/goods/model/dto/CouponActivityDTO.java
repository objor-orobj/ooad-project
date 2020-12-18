package cn.edu.xmu.goods.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CouponActivityDTO implements Serializable {
    private Long id;
    private String name;
    private String beginTime;
    private String endTIme;
}
