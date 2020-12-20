package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.BrandPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
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
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<StatusWrap> createBrand(BrandVo vo) {
        if (vo.getName() == null || vo.getName().isEmpty() || vo.getName().isBlank()) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        BrandPo po = vo.createBrand().getBrandPo();
        List<BrandPo> brandPo = null;
        BrandPoExample example = new BrandPoExample();
        BrandPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        brandPo = brandPoMapper.selectByExample(example);
        for (BrandPo p : brandPo) {
            if (p.getName().equals(po.getName()))
                return StatusWrap.just(Status.BRAND_EXISTED);
        }
        int ret = brandPoMapper.insert(po);
        BrandRetVo bVo = new BrandRetVo(po);
        if (ret != 0) {
            return StatusWrap.of(bVo, HttpStatus.CREATED);
        } else {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public Object uploadBrandImage(Long id, String imageUrl) {
        BrandPo po = brandPoMapper.selectByPrimaryKey(id);
        if (po == null) {
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

    public ResponseEntity<StatusWrap> getAllBrands(Integer page, Integer pageSize) {
        List<BrandPo> po = null;
        BrandPoExample example = new BrandPoExample();
        BrandPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, pageSize);
        criteria.andIdIsNotNull();
        po = brandPoMapper.selectByExample(example);
        if (po == null || po.isEmpty()) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<BrandRetVo> brandRet = new ArrayList<>(po.size());
        for (BrandPo p : po) {
            BrandRetVo brand = new BrandRetVo(p);
            brandRet.add(brand);
        }
        PageInfo<BrandPo> p1o = PageInfo.of(po);
        PageInfo<BrandRetVo> ret = new PageInfo<>(brandRet);
        ret.setPages(p1o.getPages());
        ret.setPageNum(p1o.getPageNum());
        ret.setPageSize(p1o.getPageSize());
        ret.setTotal(p1o.getTotal());
        return StatusWrap.of(ret);
    }

    public ResponseEntity<StatusWrap> modifyBrand(Long id, BrandVo vo) {
        if (vo.getName() == null || vo.getName().isBlank() || vo.getName().isEmpty()) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        BrandPo po = brandPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<BrandPo> brandPo = null;
        BrandPoExample example = new BrandPoExample();
        BrandPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        brandPo = brandPoMapper.selectByExample(example);
        for (BrandPo p : brandPo) {
            if (p.getName().equals(vo.getName()))
                return StatusWrap.just(Status.BRAND_EXISTED);
        }
        po.setName(vo.getName());
        po.setDetail(vo.getDetail());
        po.setGmtModified(LocalDateTime.now());
        int ret = brandPoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public ResponseEntity<StatusWrap> deleteBrand(Long id) {
        BrandPo po = brandPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        BrandPoExample brandExample = new BrandPoExample();
        BrandPoExample.Criteria brandCriteria = brandExample.createCriteria();
        brandCriteria.andIdEqualTo(id);
        int ret = brandPoMapper.deleteByExample(brandExample);
        if (ret == 0)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        // set spu.brandId to 0
        GoodsSpuPoExample spuExample = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria spuCriteria = spuExample.createCriteria();
        spuCriteria.andBrandIdEqualTo(id);
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

    public ResponseEntity<StatusWrap> addSpuToBrand(Long id, Long spuId) {
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if (brandPo == null || goodsSpuPo == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPoExample example = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andBrandIdEqualTo(id);
        List<GoodsSpuPo> spuPos = goodsSpuPoMapper.selectByExample(example);
        if (spuPos.isEmpty()) {
            goodsSpuPo.setBrandId(id);
            int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if (ret == 0)
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            return StatusWrap.just(Status.OK);
        }
        return StatusWrap.just(Status.ADDED_BRAND);
    }

    public ResponseEntity<StatusWrap> removeSpuFromBrand(Long id, Long spuId) {
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if (brandPo == null || goodsSpuPo == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPoExample example = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(spuId);
        criteria.andBrandIdEqualTo(id);
        List<GoodsSpuPo> spuPos = goodsSpuPoMapper.selectByExample(example);
        if (spuPos.isEmpty()) {
            return StatusWrap.just(Status.NOT_ADDED_BRAND);
        } else {
            goodsSpuPo.setBrandId(Long.valueOf(0));
            int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
            if (ret == 0)
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            return StatusWrap.just(Status.OK);
        }
    }

}
