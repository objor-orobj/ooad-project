package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsCategoryPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.po.GoodsCategoryPo;
import cn.edu.xmu.goods.model.po.GoodsCategoryPoExample;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPoExample;
import cn.edu.xmu.goods.model.vo.GoodsCategoryRetVo;
import cn.edu.xmu.goods.model.vo.GoodsCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<StatusWrap> createGoodsCategory(Long pId, GoodsCategoryVo vo){
        if(pId!=0) {
            GoodsCategoryPo goodsCategoryPo=goodsCategoryPoMapper.selectByPrimaryKey(pId);
            if(goodsCategoryPo==null)
                return StatusWrap.just(Status.PARENT_CATEGORY_NOT_EXIST);
        }
        GoodsCategoryPo po=vo.createGoodsCategory().getGoodsCategoryPo();
        po.setPid(pId);
        List<GoodsCategoryPo> list=null;
        GoodsCategoryPoExample example=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdIsNotNull();
        list=goodsCategoryPoMapper.selectByExample(example);
        for(GoodsCategoryPo p:list){
            if(p.getName().equals(po.getName()))
                return StatusWrap.just(Status.GOODSCATEGORY_EXISTED);
        }
        int ret=goodsCategoryPoMapper.insert(po);
        GoodsCategoryRetVo gVo=new GoodsCategoryRetVo(po);
        if(ret!=0){
            return StatusWrap.of(gVo, HttpStatus.CREATED);
        }else{
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public ResponseEntity<StatusWrap> modifyGoodsCategory(Long categoryId, GoodsCategoryVo vo){
        GoodsCategoryPo po=goodsCategoryPoMapper.selectByPrimaryKey(categoryId);
        if(po==null){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<GoodsCategoryPo> categoryPo=null;
        GoodsCategoryPoExample example=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdIsNotNull();
        categoryPo=goodsCategoryPoMapper.selectByExample(example);
        for(GoodsCategoryPo p:categoryPo){
            if(p.getName().equals(vo.getName()))
                return StatusWrap.just(Status.CATEGORY_EXISTED);
        }
        po.setName(vo.getName());
        po.setGmtModified(LocalDateTime.now());
        int ret=goodsCategoryPoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public ResponseEntity<StatusWrap> getGoodsCategory(Long id){
        GoodsCategoryPoExample  example=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria=example.createCriteria();
        List<GoodsCategoryPo> listPo=null;
        criteria.andPidEqualTo(id);
        listPo=goodsCategoryPoMapper.selectByExample(example);
        if(listPo==null||listPo.isEmpty()){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<GoodsCategoryRetVo> retVo=new ArrayList<>(listPo.size());
        for(GoodsCategoryPo p:listPo){
            GoodsCategoryRetVo vo=new GoodsCategoryRetVo(p);
            retVo.add(vo);
        }
        return StatusWrap.of(retVo);
    }

    public ResponseEntity<StatusWrap> deleteGoodsCategory(Long id){
        GoodsCategoryPo po1=goodsCategoryPoMapper.selectByPrimaryKey(id);
        if(po1==null){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsCategoryPoExample example=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(id);
        int ret=goodsCategoryPoMapper.deleteByExample(example);
        if(ret==0)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        //删除子类
        GoodsCategoryPoExample example1=new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria1=example1.createCriteria();
        criteria1.andPidEqualTo(id);
        List<GoodsCategoryPo> po=goodsCategoryPoMapper.selectByExample(example1);
        if(po!=null){
            int r=goodsCategoryPoMapper.deleteByExample(example1);
        }
        return StatusWrap.just(Status.OK);
    }

    public ResponseEntity<StatusWrap> addSpuToCategory(Long id,Long spuId){
        GoodsSpuPo goodsSpuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo goodsCategoryPo=goodsCategoryPoMapper.selectByPrimaryKey(id);
        if(goodsSpuPo==null||goodsCategoryPo==null){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPoExample example=new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andCategoryIdEqualTo(id);
        List<GoodsSpuPo> spuPos=goodsSpuPoMapper.selectByExample(example);
        if(spuPos.isEmpty()){
            goodsSpuPo.setCategoryId(id);
            int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if(ret==0)
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            return StatusWrap.just(Status.OK);
        }
        return StatusWrap.just(Status.ADDED_BRAND);
    }

    public ResponseEntity<StatusWrap> removeSpuFromCategory(Long id,Long spuId){
        GoodsSpuPo goodsSpuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo goodsCategoryPo=goodsCategoryPoMapper.selectByPrimaryKey(id);
        if(goodsSpuPo==null||goodsCategoryPo==null){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPoExample example=new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andCategoryIdEqualTo(id);
        List<GoodsSpuPo> spuPos=goodsSpuPoMapper.selectByExample(example);
        if(spuPos.isEmpty()){
            return StatusWrap.just(Status.NOT_ADDED_CATEGORY);
        }else{
            goodsSpuPo.setCategoryId(Long.valueOf(0));
            int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if(ret==0)
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            return StatusWrap.just(Status.OK);
        }
    }
}
