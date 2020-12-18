package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.GoodsCategoryDao;
import cn.edu.xmu.goods.model.vo.GoodsCategoryRetVo;
import cn.edu.xmu.goods.model.vo.GoodsCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-10 10:31
 */
@Service
public class GoodsCategoryService {
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;

    public Object createGoodsCategory(Long pId, GoodsCategoryVo vo) {
        return goodsCategoryDao.createGoodsCategory(pId,vo);
    }

    public Object modifyGoodsCategory(Long categoryId,GoodsCategoryVo vo) {
        return goodsCategoryDao.modifyGoodsCategory(categoryId,vo);
    }

    public List<GoodsCategoryRetVo> getGoodsCategory(Long id){
        return goodsCategoryDao.getGoodsCategory(id);
    }

    public Object deleteGoodsCategory(Long id){

        return goodsCategoryDao.deleteGoodsCategory(id);
    }

    public Object addSpuToCategory(Long id,Long spuId,Long shopId){
        return goodsCategoryDao.addSpuToCategory(id,spuId,shopId);
    }

    public Object removeSpuFromCategory(Long id,Long spuId){
        return goodsCategoryDao.removeSpuFromCategory(id,spuId);
    }
}
