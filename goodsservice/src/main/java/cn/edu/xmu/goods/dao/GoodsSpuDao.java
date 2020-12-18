package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.dto.FreightModelDTO;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.GoodsSpuVo;
import cn.edu.xmu.goods.model.vo.ReturnGoodsSkuVo;
import cn.edu.xmu.goods.model.vo.ReturnGoodsSpuVo;
import cn.edu.xmu.goods.service.FreightServiceInterface;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GoodsSpuDao
{
    @Autowired(required = false)
    private GoodsSkuPoMapper goodsSkuPoMapper;

    @Autowired(required = false)
    private GoodsSpuPoMapper goodsSpuPoMapper;

    @Autowired(required = false)
    private BrandPoMapper brandPoMapper;

    @Autowired(required = false)
    private GoodsCategoryPoMapper goodsCategoryPoMapper;

    @Autowired(required = false)
    private ShopPoMapper shopPoMapper;

    @Autowired(required = false)
    private GoodsSkuDao goodsSkuDao;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private FreightServiceInterface freightServiceInterface;

    public ResponseEntity<StatusWrap> getSpuById(Long spuId)
    {
        GoodsSkuPoExample example =new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria=example.createCriteria();
        GoodsSpuPo spuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if(spuPo==null)
        {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        BrandPo brandPo=brandPoMapper.selectByPrimaryKey(spuPo.getBrandId());
        GoodsCategoryPo goodsCategoryPo=goodsCategoryPoMapper.selectByPrimaryKey(spuPo.getCategoryId());

        //TODO 此处查询运费模板,skuDao层相同
        FreightModelDTO freightModelDTO=freightServiceInterface.getFreightModelById(spuPo.getFreightId());

        ShopPo shopPo=shopPoMapper.selectByPrimaryKey(spuPo.getShopId());
        example.or().andGoodsSpuIdEqualTo(spuId);
        List<GoodsSkuPo> skus=goodsSkuPoMapper.selectByExample(example);
        List<ReturnGoodsSkuVo> vos = skus.stream().map(sku-> new ReturnGoodsSkuVo(sku,goodsSkuDao.selectFloatPrice(sku.getId()))).collect(Collectors.toList());
        ReturnGoodsSpuVo vo=new ReturnGoodsSpuVo(spuPo);
        if(brandPo!=null) vo.setBrand(brandPo);

        if(goodsCategoryPo!=null) vo.setCategory(goodsCategoryPo);

        if(freightModelDTO!=null) vo.setFreightModelDTO(freightModelDTO);

        if(shopPo!=null) vo.setShop(shopPo);

        vo.setSkuList(vos);
        return StatusWrap.of(vo);
    }

    public ResponseEntity<StatusWrap> createSpu(Long id,GoodsSpuVo vo)
    {
        GoodsSpuPo spuPo= vo.toGoodsSpuPo(vo);
        spuPo.setGmtCreate(LocalDateTime.now());
        spuPo.setDisabled((byte) 0);
        spuPo.setShopId(id);
        int ret =goodsSpuPoMapper.insertSelective(spuPo);
        if (ret != 0) {
            ReturnGoodsSpuVo returnGoodsSpuVo=new ReturnGoodsSpuVo(spuPo);
            return StatusWrap.of(returnGoodsSpuVo);
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> uploadSpuImg(Long shopId,Long spuId, String img)
    {
        GoodsSpuPo spuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if(spuPo==null)
        {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        //TODO 此处进行了权限校验
        if(!spuPo.getShopId().equals(shopId))
        {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if(spuPo.getImageUrl()!=null)
        {
            File file = new File(spuPo.getImageUrl());
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
        spuPo.setImageUrl(img);
        spuPo.setGmtCreate(LocalDateTime.now());
        int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(spuPo);
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> updateSpu(Long spuId, GoodsSpuVo vo)
    {
        GoodsSpuPo spuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if(spuPo==null)
        {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPo po= vo.toGoodsSpuPo(vo);
        po.setGmtModified(LocalDateTime.now());
        po.setId(spuId);
        int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> deleteSpu(Long shopId, Long spuId)
    {
        GoodsSkuPoExample example =new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria=example.createCriteria();
        GoodsSpuPo spuPo=goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if(spuPo==null)
        {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        //TODO 此处进行了权限校验
        if(!spuPo.getShopId().equals(shopId))
        {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }

        example.or().andGoodsSpuIdEqualTo(spuId);
        List<GoodsSkuPo> skus=goodsSkuPoMapper.selectByExample(example);
        if(skus==null||skus.size()<=0)
        {
            int ret=goodsSpuPoMapper.deleteByPrimaryKey(spuId);
            if (ret != 0) {
                return StatusWrap.ok();
            } else {
                return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
            }
        }
        if(spuPo.getDisabled()==(byte)6)
        {
            return StatusWrap.ok();
        }
        spuPo.setDisabled((byte)6);
        spuPo.setGmtModified(LocalDateTime.now());
        int ret=goodsSpuPoMapper.updateByPrimaryKeySelective(spuPo);
        if (ret == 0){
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
        for (GoodsSkuPo goodsSkuPo : skus) {
            if (goodsSkuPo.getDisabled() == (byte) 6) continue;
            goodsSkuPo.setDisabled((byte) 6);
            int ret2 = goodsSkuPoMapper.updateByPrimaryKeySelective(goodsSkuPo);
            if (ret2 == 0) {
                return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
            }
        }
        return StatusWrap.ok();
    }
}
