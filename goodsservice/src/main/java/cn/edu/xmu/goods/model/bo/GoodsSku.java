package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.vo.ReturnGoodsSkuVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GoodsSku {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State implements Serializable {
        NOT_SHELF(0, "未上架"),
        SHELF(4, "上架"),
        DELETED(6, "已删除");
        private final Integer code;
        private final String name;

        State(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static State fromCode(Integer code) {
            switch (code) {
                case 0:
                    return NOT_SHELF;
                case 4:
                    return SHELF;
                case 6:
                    return DELETED;

            }
            return null;
        }
    }

    private Long id;
    private Long goodsSpuId;
    private String skuSn;
    private String name;
    private Long originalPrice;
    private String configuration;
    private Long weight;
    private String imageUrl;
    private Integer inventory;
    private String detail;
    private Byte disable;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public GoodsSkuPo getGoodsSkuPo() {
        GoodsSkuPo po = new GoodsSkuPo();
        po.setSkuSn(this.skuSn);
        po.setGoodsSpuId(this.goodsSpuId);
        po.setGmtCreate(this.gmtCreate);
        return po;
    }

    public GoodsSku() {
    }


    public GoodsSku(Long spuId) {
        this.goodsSpuId = spuId;
        this.gmtCreate = LocalDateTime.now();
    }

    public GoodsSku(String skuSn) {
        this.skuSn = skuSn;
        this.gmtCreate = LocalDateTime.now();
    }

    public static ReturnGoodsSkuVo creatRetVo(GoodsSkuPo po) {
        ReturnGoodsSkuVo vo = null;
        vo.setId(po.getId());
        vo.setName(po.getName());
        vo.setSkuSn(po.getSkuSn());
        vo.setImageUrl(po.getImageUrl());
        vo.setInventory(po.getInventory());
        vo.setOriginalPrice(po.getOriginalPrice());
        vo.setDisable(po.getDisabled()==1);

        return vo;
    }

    public GoodsSkuPo toGoodsSkuPo() {
        GoodsSkuPo goodsSkuPo = new GoodsSkuPo();
        goodsSkuPo.setSkuSn(skuSn);
        goodsSkuPo.setName(name);
        goodsSkuPo.setOriginalPrice(originalPrice);
        goodsSkuPo.setConfiguration(configuration);
        goodsSkuPo.setWeight(weight);
        goodsSkuPo.setImageUrl(imageUrl);
        goodsSkuPo.setInventory(inventory);
        goodsSkuPo.setDetail(detail);
        goodsSkuPo.setGmtModified(gmtModified);
        goodsSkuPo.setGmtCreate(gmtCreate);
        return goodsSkuPo;
    }
}
