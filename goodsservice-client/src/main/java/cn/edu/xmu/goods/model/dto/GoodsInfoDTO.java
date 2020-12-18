package cn.edu.xmu.goods.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsInfoDTO implements Serializable {
    private Long weight;
    private Long shopId;
    private Long freightId;
}
