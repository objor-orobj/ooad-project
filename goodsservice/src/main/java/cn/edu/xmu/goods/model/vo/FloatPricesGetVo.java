package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.FloatPricePo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FloatPricesGetVo
{
    @NotNull
    private Long activityPrice;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
    @NotNull
    private Integer quantity;

    public FloatPricePo toFloatPricePo(Long skuId,FloatPricesGetVo vo)
    {
        FloatPricePo po=new FloatPricePo();
        po.setGoodsSkuId(skuId);
        po.setActivityPrice(vo.getActivityPrice());
        po.setBeginTime(vo.getBeginTime());
        po.setEndTime(vo.getEndTime());
        po.setQuantity(vo.getQuantity());
        return po;
    }
}