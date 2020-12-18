package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.BrandPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.po.BrandPoExample;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPoExample;
import cn.edu.xmu.goods.model.vo.BrandRetVo;
import cn.edu.xmu.goods.model.vo.BrandVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-09 11:20
 */
@Repository
public class BrandDao {
    @Autowired
    private BrandPoMapper brandPoMapper;
    @Autowired
    private GoodsSpuPoMapper goodsSpuPoMapper;

    public Object createBrand(BrandVo vo){
        BrandPo po=vo.createBrand().getBrandPo();
        int ret=brandPoMapper.insert(po);
        if(ret!=0){
            return new ReturnObject<>(po);
        }else{
            return new ReturnObject<>(ResponseCode.USER_HASSHOP, ResponseCode.USER_HASSHOP.getMessage());
        }
    }

    public Object uploadBrandImage(Long id,String imageUrl) {
        BrandPo po=brandPoMapper.selectByPrimaryKey(id);
        if (po == null ) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
        if (po.getImageUrl() != null) {
            File file = new File(po.getImageUrl());
            if (file.isFile() && file.exists())
                file.delete();
        }
        po.setImageUrl(imageUrl);
        int ret = brandPoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
        } else {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
    }

    public ReturnObject<PageInfo<BrandRetVo>> getAllBrands(Integer page,Integer pageSize){
        List<BrandPo> po=null;
        BrandPoExample example=new BrandPoExample();
        BrandPoExample.Criteria criteria=example.createCriteria();
        PageHelper.startPage(page,pageSize);
        criteria.andIdIsNotNull();
        po=brandPoMapper.selectByExample(example);
        if(po==null||po.isEmpty()){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
        List<BrandRetVo> brandRet=new ArrayList<>(po.size());
        for(BrandPo p:po){
            BrandRetVo brand=new BrandRetVo(p);
            brandRet.add(brand);
        }
        PageInfo<BrandRetVo> ret=PageInfo.of(brandRet);
        return new ReturnObject<>(ret);
    }

    public Object modifyBrand(Long id,BrandVo vo){
        BrandPo po=brandPoMapper.selectByPrimaryKey(id);
        po.setName(vo.getName());
        po.setDetail(vo.getDetail());
        po.setGmtModified(LocalDateTime.now());
        int ret=brandPoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
        } else {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, ResponseCode.FIELD_NOTVALID.getMessage());
        }
    }

    public ReturnObject<BrandPo> deleteBrand(Long id){
        BrandPoExample example=new BrandPoExample();
        BrandPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(id);
        int ret=brandPoMapper.deleteByExample(example);
        if(ret==0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
    }

    public Object addSpuToBrand(Long id,Long spuId,Long shopId){
        BrandPo brandPo=brandPoMapper.selectByPrimaryKey(id);
        GoodsSpuPo goodsSpuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if(brandPo==null||goodsSpuPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
        GoodsSpuPoExample example=new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andBrandIdEqualTo(id);
        criteria.andShopIdEqualTo(shopId);
        List<GoodsSpuPo> spuPos=goodsSpuPoMapper.selectByExample(example);
        if(spuPos.isEmpty()){
            goodsSpuPo.setBrandId(id);
            int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if(ret==0)
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
            return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
        }
        return new ReturnObject<>(ResponseCode.FIELD_NOTVALID,ResponseCode.FIELD_NOTVALID.getMessage());
    }

    public Object removeSpuFromBrand(Long id,Long spuId){
        BrandPo brandPo=brandPoMapper.selectByPrimaryKey(id);
        GoodsSpuPo goodsSpuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if(brandPo==null||goodsSpuPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
        GoodsSpuPoExample example=new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andBrandIdEqualTo(id);
        List<GoodsSpuPo> spuPos=goodsSpuPoMapper.selectByExample(example);
        if(spuPos.isEmpty()){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }else{
            goodsSpuPo.setBrandId(Long.valueOf(0));
            int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if(ret==0)
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
            return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
        }
    }

}
