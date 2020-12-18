package cn.edu.xmu.goods.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashSaleTimeSegmentDTO {
    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
