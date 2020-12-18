package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.BrandPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-09 10:57
 */
@Data
public class Brand {
    private  Long id;
    private String name;
    private String detail;
    private String imageUrl;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public BrandPo getBrandPo(){
        BrandPo po=new BrandPo();
        po.setId(this.id);
        po.setName(this.name);
        po.setDetail(this.detail);
        po.setImageUrl(this.imageUrl);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }

    public Brand(){
    }

    public Brand(BrandPo po){
        this.id=po.getId();
        this.name=po.getName();
        this.detail=po.getDetail();
        this.imageUrl=po.getImageUrl();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }
}
