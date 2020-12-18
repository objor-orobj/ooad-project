package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.order.model.dto.FreightModelDTO;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.po.GoodsCategoryPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.ShopPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReturnGoodsSpuVo
{
    @Data
    public static class SimpleBrand
    {

        private Long id=null;
        private String name=null;
        private String imageUrl=null;
    }
    @Data
    public static class SimpleCategory
    {
        private Long id=null;
        private String name=null;
    }


    @Data
    public static class SimpleShop
    {
        private Long id=null;
        private String name=null;

    }
    public void setBrand(BrandPo brandPo)
    {
        this.brand.id=brandPo.getId();
        this.brand.name=brandPo.getName();
        this.brand.imageUrl=brandPo.getImageUrl();
    }
    public void setCategory(GoodsCategoryPo goodsCategoryPo)
    {
        this.category.id=goodsCategoryPo.getId();
        this.category.name=goodsCategoryPo.getName();
    }

    public void setShop(ShopPo shop)
    {
        this.shop.id=shop.getId();
        this.shop.name=shop.getName();
    }

    public ReturnGoodsSpuVo(GoodsSpuPo po)
    {
        this.id=po.getId();
        this.name=po.getName();
        this.goodsSn=po.getGoodsSn();
        this.detail=po.getDetail();
        this.imageUrl=po.getImageUrl();
        this.spec=po.getSpec();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.disabled= po.getDisabled()==1;
    }
    public ReturnGoodsSpuVo()
    {
        ;
    }

    private Long id;

    private String name;

    private SimpleBrand brand=new SimpleBrand();

    private SimpleCategory category=new SimpleCategory();

    private FreightModelDTO freightModelDTO=new FreightModelDTO();

    private SimpleShop shop=new SimpleShop();

    private String goodsSn;

    private String detail;

    private String imageUrl;

    private String spec;

    private List<ReturnGoodsSkuVo> skuList=null;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Boolean disabled;
}
