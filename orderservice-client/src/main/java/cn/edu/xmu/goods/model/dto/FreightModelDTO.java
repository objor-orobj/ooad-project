package cn.edu.xmu.goods.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FreightModelDTO {
    private Long id;
    private String name;
    private Byte type;
    private Integer unit;
    private Byte defaultModel;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
