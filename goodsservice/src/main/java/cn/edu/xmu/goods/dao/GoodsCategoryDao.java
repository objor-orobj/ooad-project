package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsCategoryPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.po.GoodsCategoryPo;
import cn.edu.xmu.goods.model.po.GoodsCategoryPoExample;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPoExample;
import cn.edu.xmu.goods.model.vo.GoodsCategoryRetVo;
import cn.edu.xmu.goods.model.vo.GoodsCategoryVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-10 10:32
 */
@Repository
public class GoodsCategoryDao {
    @Autowired
    private GoodsCategoryPoMapper goodsCategoryPoMapper;
    @Autowired
    private GoodsSpuPoMapper goodsSpuPoMapper;

    public Object createGoodsCategory(Long pId, GoodsCategoryVo vo){
        GoodsCategoryPo goodsCategoryPo=goodsCategoryPoMapper.selectByPrimaryKey(pId);
        if(goodsCategoryPo!=null&&goodsCategoryPo.getPid()!=0)
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        GoodsCategoryPo po=vo.createGoodsCategory().getGoodsCategoryPo();
        po.setPid(pId);
        int ret=goodsCategoryPoMapper.insert(po);
        if(ret!=0){
            return new ReturnObject<>(po);
        }else{
            return new ReturnObject<>(ResponseCode.USER_HASSHOP, ResponseCode.USER_HASSHOP.getMessage());
        }
    }

    public Object modifyGoodsCategory(Long categoryId, GoodsCategoryVo vo){
        GoodsCategoryPo po=goodsCategoryPoMapper.selectByPrimaryKey(categoryId);
        po.setName(vo.getName());
        po.setGmtModified(LocalDateTime.now());
        int ret=goodsCategoryPoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
        } else {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, ResponseCode.FIELD_NOTVALID.getMessage());
        }
    }

    public List<GoodsCategoryRetVo> getGoodsCategory(Long id){
        GoodsCategoryPoExample  example=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria=example.createCriteria();
        List<GoodsCategoryPo> listPo=null;
        criteria.andPidEqualTo(id);
        listPo=goodsCategoryPoMapper.selectByExample(example);
        if(listPo==null||listPo.isEmpty()){
            return null;
        }
        List<GoodsCategoryRetVo> retVo=new ArrayList<>(listPo.size());
        for(GoodsCategoryPo p:listPo){
            GoodsCategoryRetVo vo=new GoodsCategoryRetVo(p);
            retVo.add(vo);
        }
        return retVo;
    }

    public ReturnObject<GoodsCategoryPo> deleteGoodsCategory(Long id){
        GoodsCategoryPoExample example=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(id);
        int ret=goodsCategoryPoMapper.deleteByExample(example);
        if(ret==0)
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        //删除子类
        GoodsCategoryPoExample example1=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria1=example1.createCriteria();
        criteria1.andPidEqualTo(id);
        List<GoodsCategoryPo> po=goodsCategoryPoMapper.selectByExample(example1);
        if(po!=null){
            int r=goodsCategoryPoMapper.deleteByExample(example1);
        }
        return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
    }

    public Object addSpuToCategory(Long id,Long spuId,Long shopId){
        GoodsSpuPo goodsSpuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo goodsCategoryPo=goodsCategoryPoMapper.selectByPrimaryKey(id);
        if(goodsSpuPo==null||goodsCategoryPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
        GoodsSpuPoExample example=new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria=example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        criteria.andIdEqualTo(spuId);
        criteria.andCategoryIdEqualTo(id);
        List<GoodsSpuPo> spuPos=goodsSpuPoMapper.selectByExample(example);
        if(spuPos.isEmpty()){
            goodsSpuPo.setCategoryId(id);
            int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if(ret==0)
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
            return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
        }
        return new ReturnObject<>(ResponseCode.FIELD_NOTVALID,ResponseCode.FIELD_NOTVALID.getMessage());
    }

    public Object removeSpuFromCategory(Long id,Long spuId){
        GoodsSpuPo goodsSpuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo goodsCategoryPo=goodsCategoryPoMapper.selectByPrimaryKey(id);
        if(goodsSpuPo==null||goodsCategoryPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }
        GoodsSpuPoExample example=new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andCategoryIdEqualTo(id);
        List<GoodsSpuPo> spuPos=goodsSpuPoMapper.selectByExample(example);
        if(spuPos.isEmpty()){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
        }else{
            goodsSpuPo.setCategoryId(Long.valueOf(0));
            int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if (ret != 0) {
                return new ReturnObject<>(ResponseCode.OK, ResponseCode.OK.getMessage());
            } else {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
            }
        }
    }
}
