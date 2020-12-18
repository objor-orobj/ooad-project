package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class GrouponActivityInVo
{
    private Long shopid;
    private Long goodsSpuId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    @Range(min=0, max=2)
    @ApiModelProperty(value = "状态")
    private Integer state;
    @Range(min=0, max=3)
    @ApiModelProperty(value = "时间")
    private Integer timeline;
    @NotNull
    private Integer page;
    @NotNull
    private Integer pageSize;

    public GrouponActivityInVo(Long shopid, Long goodsSpuId, LocalDateTime beginTime, LocalDateTime endTime,
                               Integer state, Integer timeline, Integer page, Integer pageSize){
        this.shopid=shopid;
        this.goodsSpuId=goodsSpuId;
        this.beginTime=beginTime;
        this.endTime=endTime;
        this.state=state;
        this.timeline=timeline;
        this.page=page;
        this.pageSize=pageSize;
    }
}
