package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsCategoryPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-10 10:22
 */
@Data
public class GoodsCategory {
    private Long id;
    private String name;
    private Long pid;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public GoodsCategoryPo getGoodsCategoryPo(){
        GoodsCategoryPo po=new GoodsCategoryPo();
        po.setId(this.id);
        po.setName(this.name);
        po.setPid(this.pid);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }
}
