package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Brand;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-09 11:12
 */
@Data
public class BrandVo {
    //@NotBlank(message="name不得为空")
    private String name;

    //@NotBlank(message="detail不得为空")
    private String detail;

    public Brand createBrand(){
        Brand brand=new Brand();
        brand.setName(this.name);
        brand.setDetail(this.detail);
        brand.setGmtCreate(LocalDateTime.now());
        return brand;
    }
}
