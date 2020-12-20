package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsCategory;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-10 10:27
 */
@Data
public class GoodsCategoryVo {
    //@NotBlank(message="name不得为空")
    private String name;

    public GoodsCategory createGoodsCategory(){
        GoodsCategory goodsCategory=new GoodsCategory();
        goodsCategory.setName(this.name);
        goodsCategory.setGmtCreate(LocalDateTime.now());
        return goodsCategory;
    }
}
