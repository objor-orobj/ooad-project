package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GoodsSpuVo
{
    @NotNull
    @NotEmpty
    private String name;
    private String decription;
    private String specs;

    public GoodsSpuPo toGoodsSpuPo(GoodsSpuVo vo)
    {
        GoodsSpuPo po=new GoodsSpuPo();
        po.setName(vo.getName());
        po.setDetail(vo.getDecription());
        po.setSpec(vo.getSpecs());
        return po;
    }
}
