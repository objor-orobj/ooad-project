package cn.edu.xmu.goods.model.vo;

import lombok.Data;


@Data
public class GetGoodsSkuVo
{
    private Long shopId;
    private String skuSn;
    private Long goodsSpuId;
    private String spuSn;
    private Integer page;
    private Integer pageSize;
    public GetGoodsSkuVo(Long shopId,String skuSn,Long goodsSpuId,String spuSn,Integer page,Integer pageSize)
    {
        this.shopId=shopId;
        this.skuSn=skuSn;
        this.goodsSpuId=goodsSpuId;
        this.spuSn=spuSn;
        this.page=page;
        this.pageSize=pageSize;
    }
}
