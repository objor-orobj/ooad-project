package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PresaleActivityModifyVo
{
    @ApiModelProperty(value = "活动名称")
    private String name;
    @Range(min=0)
    @ApiModelProperty(value = "活动库存量")
    private Integer quantity;
    @Range(min=0)
    @ApiModelProperty(value = "定金")
    private Long advancePayPrice;
    @Range(min=0)
    @ApiModelProperty(value = "尾款")
    private Long restPayPrice;
    @NotNull
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @ApiModelProperty(value = "活动开始时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "开始支付尾款时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private LocalDateTime payTime;
    @ApiModelProperty(value = "活动结束时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private LocalDateTime endTime;

    public PresaleActivityModifyVo(String name, Integer quantity, Long advancePayPrice, Long restPayPrice,
                                   Long shopId, LocalDateTime beginTime, LocalDateTime payTime, LocalDateTime endTime)
    {
        this.name=name;
        this.quantity=quantity;
        this.advancePayPrice=advancePayPrice;
        this.restPayPrice=restPayPrice;
        this.shopId=shopId;
        this.beginTime=beginTime;
        this.payTime=payTime;
        this.endTime=endTime;
    }
}
