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
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * //
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

    public ResponseEntity<StatusWrap> createGoodsCategory(Long pId, GoodsCategoryVo vo) {
        if (vo.getName() == null || vo.getName().isBlank() || vo.getName().isEmpty()) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        if (pId != 0) {
            GoodsCategoryPo goodsCategoryPo = goodsCategoryPoMapper.selectByPrimaryKey(pId);
            if (goodsCategoryPo == null)
                return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsCategoryPo po = vo.createGoodsCategory().getGoodsCategoryPo();
        po.setPid(pId);
        List<GoodsCategoryPo> list = null;
        GoodsCategoryPoExample example = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        list = goodsCategoryPoMapper.selectByExample(example);
        for (GoodsCategoryPo p : list) {
            if (p.getName().equals(po.getName()))
                return StatusWrap.just(Status.GOODSCATEGORY_EXISTED);
        }
        int ret = goodsCategoryPoMapper.insert(po);
        GoodsCategoryRetVo gVo = new GoodsCategoryRetVo(po);
        if (ret != 0) {
            return StatusWrap.of(gVo, HttpStatus.CREATED);
        } else {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public ResponseEntity<StatusWrap> modifyGoodsCategory(Long categoryId, GoodsCategoryVo vo) {
        if (vo.getName() == null || vo.getName().isBlank() || vo.getName().isEmpty()) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        GoodsCategoryPo po = goodsCategoryPoMapper.selectByPrimaryKey(categoryId);
        if (po == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<GoodsCategoryPo> categoryPo = null;
        GoodsCategoryPoExample example = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        categoryPo = goodsCategoryPoMapper.selectByExample(example);
        for (GoodsCategoryPo p : categoryPo) {
            if (p.getName().equals(vo.getName()))
                return StatusWrap.just(Status.GOODSCATEGORY_EXISTED);
        }
        po.setName(vo.getName());
        po.setGmtModified(LocalDateTime.now());
        int ret = goodsCategoryPoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public ResponseEntity<StatusWrap> getGoodsCategory(Long id) {
        if (id != null && !id.equals(0L)) {
            GoodsCategoryPo parent = goodsCategoryPoMapper.selectByPrimaryKey(id);
            if (parent == null)
                return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsCategoryPoExample example = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = example.createCriteria();
        criteria.andPidEqualTo(id);
        List<GoodsCategoryPo> child = goodsCategoryPoMapper.selectByExample(example);
        if (child == null || child.size() == 0)
            return StatusWrap.of(new ArrayList<>());
        List<GoodsCategoryRetVo> view = child.stream().map(GoodsCategoryRetVo::new).collect(Collectors.toList());
        return StatusWrap.of(view);
    }

    public ResponseEntity<StatusWrap> deleteGoodsCategory(Long cateId) {
        // 删除子类
        // recurse
        GoodsCategoryPoExample example1 = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andPidEqualTo(cateId);
        List<GoodsCategoryPo> po = goodsCategoryPoMapper.selectByExample(example1);
        if (po != null && po.size() > 0) {
            for (GoodsCategoryPo sub : po) {
                deleteGoodsCategory(sub.getId());
            }
        }
        GoodsCategoryPo catePo = goodsCategoryPoMapper.selectByPrimaryKey(cateId);
        if (catePo == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        GoodsCategoryPoExample cateExample = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria cateCriteria = cateExample.createCriteria();
        cateCriteria.andIdEqualTo(cateId);
        int ret = goodsCategoryPoMapper.deleteByExample(cateExample);
        if (ret == 0)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        // set spu.cate_id to 0
        GoodsSpuPoExample spuExample = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria spuCriteria = spuExample.createCriteria();
        spuCriteria.andCategoryIdEqualTo(cateId);
        List<GoodsSpuPo> list;
        try {
            list = goodsSpuPoMapper.selectByExample(spuExample);
        } catch (DataAccessException exception) {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
        if (list == null || list.size() == 0)
            return StatusWrap.ok();
        for (GoodsSpuPo spuPoo : list) {
            spuPoo.setBrandId(0L);
            try {
                goodsSpuPoMapper.updateByPrimaryKeySelective(spuPoo);
            } catch (DataAccessException ignored) {
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            }
        }
        return StatusWrap.just(Status.OK);
    }

    public ResponseEntity<StatusWrap> addSpuToCategory(Long id, Long spuId) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo goodsCategoryPo = goodsCategoryPoMapper.selectByPrimaryKey(id);
        if (goodsSpuPo == null || goodsCategoryPo == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPoExample example = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andCategoryIdEqualTo(id);
        List<GoodsSpuPo> spuPos = goodsSpuPoMapper.selectByExample(example);
        if (spuPos.isEmpty()) {
            goodsSpuPo.setCategoryId(id);
            int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if (ret == 0)
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            return StatusWrap.just(Status.OK);
        }
        return StatusWrap.just(Status.ADDED_BRAND);
    }

    public ResponseEntity<StatusWrap> removeSpuFromCategory(Long id, Long spuId) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        GoodsCategoryPo goodsCategoryPo = goodsCategoryPoMapper.selectByPrimaryKey(id);
        if (goodsSpuPo == null || goodsCategoryPo == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPoExample example = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andCategoryIdEqualTo(id);
        List<GoodsSpuPo> spuPos = goodsSpuPoMapper.selectByExample(example);
        if (spuPos.isEmpty()) {
            return StatusWrap.just(Status.NOT_ADDED_CATEGORY);
        } else {
            goodsSpuPo.setCategoryId(Long.valueOf(0));
            int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if (ret == 0)
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            return StatusWrap.just(Status.OK);
        }
    }
}
