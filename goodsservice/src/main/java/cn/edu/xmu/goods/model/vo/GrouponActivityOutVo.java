package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GrouponActivityOutVo
{
    @ApiModelProperty(value = "活动id")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime endTime;

    public GrouponActivityOutVo(GrouponActivityPo po)
    {
        this.id = po.getId();
        this.name = po.getName();
        this.endTime = po.getEndTime();
        this.beginTime = po.getBeginTime();
    }
}
