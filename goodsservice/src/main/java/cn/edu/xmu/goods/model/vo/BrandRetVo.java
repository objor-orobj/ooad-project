package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.BrandPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-09 20:48
 */
@Data
public class BrandRetVo {
    private Long id;
    private String name;
    private String detail;
    private String imageUrl;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public BrandRetVo(BrandPo po){
        this.id=po.getId();
        this.name=po.getName();
        this.detail=po.getDetail();
        this.imageUrl=po.getImageUrl();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }
}
