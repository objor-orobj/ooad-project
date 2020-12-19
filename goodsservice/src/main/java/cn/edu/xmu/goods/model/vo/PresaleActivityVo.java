package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class PresaleActivityVo {

    @ApiModelProperty(value = "活动名称")
//    @NotEmpty
    private String name;
    @ApiModelProperty(value = "活动库存量")
    private Integer quantity;
    @ApiModelProperty(value = "定金")
    private Long advancePayPrice;
    @ApiModelProperty(value = "尾款")
    private Long restPayPrice;
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @ApiModelProperty(value = "活动商品skuid")
    private Long goodsSkuId;
    @ApiModelProperty(value = "状态")
    private Byte state;
    @ApiModelProperty(value = "活动开始时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "开始支付尾款时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private LocalDateTime payTime;
    @ApiModelProperty(value = "活动结束时间")
    //@DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private LocalDateTime endTime;
}
