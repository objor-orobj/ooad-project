package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GoodsCategoryPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-10 15:02
 */
@Data
public class GoodsCategoryRetVo {
    private Long id;
    private String name;
    private Long pid;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public GoodsCategoryRetVo(GoodsCategoryPo po){
        this.id=po.getId();
        this.name=po.getName();
        this.pid=po.getPid();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }
}
