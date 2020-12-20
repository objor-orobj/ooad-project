package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class GrouponActivityVo {
    @ApiModelProperty(value = "活动名称")
    private String name;
//    @NotNull
    @ApiModelProperty(value = "活动开始时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
//    @NotNull
    @ApiModelProperty(value = "活动结束时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
//    @Range(min=0, max=2)
    @ApiModelProperty(value = "活动状态")
    private Byte state;
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @ApiModelProperty(value = "活动商品spuid")
    private Long goodsSpuId;
//    @NotBlank
    @ApiModelProperty(value = "团购规则")
    private String strategy;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;
}

