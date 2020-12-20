package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PresaleActivityInVo {

    private Long shopid;
    private Long goodsSkuId;
    @ApiModelProperty(value = "状态")
    private Integer state;
    @ApiModelProperty(value = "时间")
    private Integer timeline;
    private Integer page;
    private Integer pageSize;

    public PresaleActivityInVo(Long shopid, Long goodsSkuId, Integer state, Integer timeline, Integer page, Integer pageSize){
        this.shopid=shopid;
        this.goodsSkuId=goodsSkuId;
        this.state=state;
        this.timeline=timeline;
        this.page=page;
        this.pageSize=pageSize;
    }
}
