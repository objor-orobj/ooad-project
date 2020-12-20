package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.controller.ShopController;
import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.PageWrap;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.dto.GoodsInfoDTO;
import cn.edu.xmu.goods.model.dto.GoodsSkuDTO;
import cn.edu.xmu.goods.model.dto.GoodsSkuInfo;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.order.model.dto.FreightModelDTO;
import cn.edu.xmu.order.service.FreightServiceInterface;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GoodsSkuDao {
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
    private FloatPricePoMapper floatPricePoMapper;

    @DubboReference(version = "0.0.1")
    private FreightServiceInterface freightServiceInterface;

    private static final Logger logger = LoggerFactory.getLogger(GoodsSkuDao.class);


    public Long selectFloatPrice(Long id) {
        FloatPricePoExample example = new FloatPricePoExample();
        FloatPricePoExample.Criteria criteria = example.createCriteria();
        example.or().andGoodsSkuIdEqualTo(id).andValidEqualTo((byte) 1);
        List<FloatPricePo> pos = floatPricePoMapper.selectByExample(example);

        //若没有打折价格则返回-1，在构造函数里将原价赋给现价
        if (pos == null || pos.size() <= 0) return (long) -1;
        for (FloatPricePo po : pos) {
            if (LocalDateTime.now().isAfter(po.getBeginTime()) && LocalDateTime.now().isBefore(po.getEndTime())) {
                return po.getActivityPrice();
            }
        }
        return (long) -1;
    }

    public boolean judgeResource(GoodsSkuPo skuPo, Long shopId) {
        GoodsSpuPo spuPo = goodsSpuPoMapper.selectByPrimaryKey(skuPo.getGoodsSpuId());
        if (spuPo == null) return false;
        if (!spuPo.getShopId().equals(shopId)) return false;
        return true;
    }

    //TODO 商店关闭商品不可见
    public boolean DisableGoods(Long shopId) {
        GoodsSkuPoExample example = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = example.createCriteria();
        GoodsSpuPoExample example1 = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria1 = example1.createCriteria();
        GoodsSkuPo skuPo = new GoodsSkuPo();
        GoodsSpuPo spuPo = new GoodsSpuPo();
        spuPo.setDisabled((byte) 1);
        skuPo.setDisabled((byte) 1);


        example1.or().andShopIdEqualTo(shopId).andDisabledEqualTo((byte) 0);
        List<GoodsSpuPo> spuPos = goodsSpuPoMapper.selectByExample(example1);
        if (spuPos == null || spuPos.size() <= 0) return true;

        int ret1 = goodsSpuPoMapper.updateByExampleSelective(spuPo, example1);
        if (ret1 == 0) return false;
        for (GoodsSpuPo spuPo1 : spuPos) {
            example.or().andGoodsSpuIdEqualTo(spuPo1.getId()).andDisabledEqualTo((byte) 0);
        }
        int ret = goodsSkuPoMapper.updateByExampleSelective(skuPo, example);
        return ret != 0;

    }

    public boolean ableGoods(Long shopId) {
        GoodsSkuPoExample example = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = example.createCriteria();
        GoodsSpuPoExample example1 = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria1 = example1.createCriteria();
        GoodsSkuPo skuPo = new GoodsSkuPo();
        GoodsSpuPo spuPo = new GoodsSpuPo();
        spuPo.setDisabled((byte) 0);
        skuPo.setDisabled((byte) 0);


        example1.or().andShopIdEqualTo(shopId).andDisabledEqualTo((byte) 1);
        List<GoodsSpuPo> spuPos = goodsSpuPoMapper.selectByExample(example1);
        if (spuPos == null || spuPos.size() <= 0) return true;

        int ret1 = goodsSpuPoMapper.updateByExampleSelective(spuPo, example1);
        if (ret1 == 0) return false;
        for (GoodsSpuPo spuPo1 : spuPos) {
            example.or().andGoodsSpuIdEqualTo(spuPo1.getId()).andDisabledEqualTo((byte) 1);
        }
        int ret = goodsSkuPoMapper.updateByExampleSelective(skuPo, example);
        return ret != 0;
    }

    public ResponseEntity<StatusWrap> getGoodsSkus(GetGoodsSkuVo getSkuVo) {
        List<ReturnGoodsSkuVo> view = new ArrayList<>();
        List<GoodsSkuPo> raw = new ArrayList<>();
        boolean ifNull = false;
        GoodsSkuPoExample example = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = example.createCriteria();
        if (getSkuVo.getShopId() != null || (getSkuVo.getSpuSn() != null && getSkuVo.getSpuSn().length() > 0)) {
            GoodsSpuPoExample example1 = new GoodsSpuPoExample();
            GoodsSpuPoExample.Criteria criteria1 = example1.createCriteria();
            ifNull = true;//判断要查询的sku是否数量为0
            if (getSkuVo.getShopId() != null && getSkuVo.getSpuSn() != null && getSkuVo.getSpuSn().length() > 0) {
                example1.or().andShopIdEqualTo(getSkuVo.getShopId()).andGoodsSnEqualTo(getSkuVo.getSpuSn());
            } else {
                if (getSkuVo.getShopId() != null)
                    example1.or().andShopIdEqualTo(getSkuVo.getShopId());
                else
                    example1.or().andGoodsSnEqualTo(getSkuVo.getSpuSn());
            }
            List<GoodsSpuPo> spuPo = goodsSpuPoMapper.selectByExample(example1);
            if (spuPo == null || spuPo.size() == 0) {
                view = new ArrayList<>();
                ifNull = true;
            } else {
                for (GoodsSpuPo goodsSpuPo : spuPo) {
                    if (getSkuVo.getGoodsSpuId() != null && !getSkuVo.getGoodsSpuId().equals(goodsSpuPo.getId()))
                        continue;
                    ifNull = false;
                    if (getSkuVo.getSkuSn() != null && getSkuVo.getSkuSn().length() > 0) {
                        //TODO 商品状态与其余条件筛选
                        example.or().andGoodsSpuIdEqualTo(goodsSpuPo.getId()).andSkuSnEqualTo(getSkuVo.getSkuSn()).andDisabledEqualTo((byte) 0).andStateEqualTo((byte) 4);
                    } else {
                        example.or().andGoodsSpuIdEqualTo(goodsSpuPo.getId()).andDisabledEqualTo((byte) 0).andStateEqualTo((byte) 4);
                    }

                }
                if (ifNull) {
                    view = new ArrayList<>();
                }
            }
        } else {
            //TODO 商品状态与其余条件筛选
            if (getSkuVo.getGoodsSpuId() != null && getSkuVo.getSkuSn() != null && getSkuVo.getSkuSn().length() > 0)
                example.or().andGoodsSpuIdEqualTo(getSkuVo.getGoodsSpuId()).andSkuSnEqualTo(getSkuVo.getSkuSn()).andDisabledEqualTo((byte) 0).andStateEqualTo((byte) 4);
            else {
                if (getSkuVo.getGoodsSpuId() != null)
                    example.or().andGoodsSpuIdEqualTo(getSkuVo.getGoodsSpuId()).andDisabledEqualTo((byte) 0).andStateEqualTo((byte) 4);
                else if (getSkuVo.getSkuSn() != null && getSkuVo.getSkuSn().length() > 0)
                    example.or().andSkuSnEqualTo(getSkuVo.getSkuSn()).andDisabledEqualTo((byte) 0).andStateEqualTo((byte) 4);
                else
                    example.or().andDisabledEqualTo((byte) 0).andStateEqualTo((byte) 4);
            }
        }
        if (!ifNull) {

            PageHelper.startPage(getSkuVo.getPage(), getSkuVo.getPageSize());
            raw = goodsSkuPoMapper.selectByExample(example);

        }
        view = raw.stream().map(sku -> new ReturnGoodsSkuVo(sku, selectFloatPrice(sku.getId()))).collect(Collectors.toList());

        return StatusWrap.of(PageWrap.of(PageInfo.of(raw), view));
    }

    public ReturnGoodsSkuVo getSingleSimpleSku(Integer id) {
        GoodsSkuPo po = goodsSkuPoMapper.selectByPrimaryKey(id.longValue());
        //TODO 商品状态
        if (po == null || po.getDisabled() != (byte) 0 || po.getState() != (byte) 4) return null;
        return new ReturnGoodsSkuVo(po, selectFloatPrice(po.getId()));
    }

    public ReturnGoodsSkuVo getSingleSimpleSkuPLUS(Integer id) {
        GoodsSkuPo po = goodsSkuPoMapper.selectByPrimaryKey(id.longValue());
        if (po == null) return null;
        return new ReturnGoodsSkuVo(po, selectFloatPrice(po.getId()));
    }

    public GoodsSkuPo getSkuPoById(Integer id) {
        return goodsSkuPoMapper.selectByPrimaryKey(id.longValue());
    }

    public GoodsSpuPo getSpuPoById(Long id) {
        return goodsSpuPoMapper.selectByPrimaryKey(id);
    }

    public ResponseEntity<StatusWrap> getSkuDetailedById(Long id) {
        try {
            //TODO 商品状态判断
            GoodsSkuPo skuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
            if (skuPo == null) {
                return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
            }
            ReturnWholeGoodsSkuVo skuVo = new ReturnWholeGoodsSkuVo(skuPo, selectFloatPrice(skuPo.getId()));
            //spu信息
            GoodsSkuPoExample example = new GoodsSkuPoExample();
            GoodsSkuPoExample.Criteria criteria = example.createCriteria();
            GoodsSpuPo spuPo = goodsSpuPoMapper.selectByPrimaryKey(skuPo.getGoodsSpuId());
            if (spuPo == null) {
                return StatusWrap.just(Status.SKU_NOT_HAVE_SPU);
            }
            BrandPo brandPo = brandPoMapper.selectByPrimaryKey(spuPo.getBrandId());
            GoodsCategoryPo goodsCategoryPo = goodsCategoryPoMapper.selectByPrimaryKey(spuPo.getCategoryId());

            //TODO 查询运费模板
            FreightModelDTO freightModelDTO;
            freightModelDTO = freightServiceInterface.getFreightModelById(spuPo.getFreightId());

            ShopPo shopPo = shopPoMapper.selectByPrimaryKey(spuPo.getShopId());
            //TODO 商品状态判断
            example.or().andGoodsSpuIdEqualTo(skuPo.getGoodsSpuId());
            List<GoodsSkuPo> skus = goodsSkuPoMapper.selectByExample(example);
            List<ReturnGoodsSkuVo> vos = skus.stream().map(sku -> new ReturnGoodsSkuVo(sku, selectFloatPrice(sku.getId()))).collect(Collectors.toList());
            ReturnGoodsSpuVo vo = new ReturnGoodsSpuVo(spuPo);
            if (brandPo != null) vo.setBrand(brandPo);

            if (goodsCategoryPo != null) vo.setCategory(goodsCategoryPo);

            if (freightModelDTO != null) vo.setFreightModelDTO(freightModelDTO);

            if (shopPo != null) vo.setShop(shopPo);

            vo.setSkuList(vos);
            skuVo.setSpu(vo);
            return StatusWrap.of(skuVo);
        } catch (DataAccessException e) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public ResponseEntity<StatusWrap> createSku(Long shopId, Long goodsSpuId, CreateSkuVo vo) {
//        GoodsSpuPoExample example = new GoodsSpuPoExample();
//        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        GoodsSkuPoExample example1 = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria1 = example1.createCriteria();
        //判断是否有spu
        GoodsSpuPo spuPo = goodsSpuPoMapper.selectByPrimaryKey(goodsSpuId);
        if (spuPo == null) return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!spuPo.getShopId().equals(shopId)) return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
//        example.or().andShopIdEqualTo(shopId).andIdEqualTo(goodsSpuId);
//        List<GoodsSpuPo> spuPo = goodsSpuPoMapper.selectByExample(example);
//        if (spuPo == null || spuPo.size() <= 0) {
//            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
//        }
        //判断规格重复
        example1.or().andSkuSnEqualTo(vo.getSn()).andGoodsSpuIdEqualTo(goodsSpuId);
        List<GoodsSkuPo> skuPo = goodsSkuPoMapper.selectByExample(example1);
        if (skuPo != null && skuPo.size() > 0) {
            return StatusWrap.just(Status.SKUSN_SAME);
        }

        GoodsSkuPo po = vo.asNewSku().toGoodsSkuPo();
        //TODO 商品状态
        po.setDisabled((byte) 0);
        po.setState((byte) 4);
        po.setGoodsSpuId(goodsSpuId);
        int ret = (goodsSkuPoMapper.insertSelective(po));
        if (ret != 0) {
            ReturnGoodsSkuVo returnGoodsSkuVo = new ReturnGoodsSkuVo(po, selectFloatPrice(po.getId()));
            return StatusWrap.of(returnGoodsSkuVo, HttpStatus.CREATED);
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> uploadSkuImg(Long shopId, Long skuId, String img) {
        GoodsSkuPo skuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (skuPo == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpuPo spuPo = goodsSpuPoMapper.selectByPrimaryKey(skuPo.getGoodsSpuId());
        if (spuPo == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        //TODO 此处进行了权限校验
        if (!spuPo.getShopId().equals(shopId)) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (skuPo.getImageUrl() != null) {
            File file = new File(skuPo.getImageUrl());
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
        skuPo.setImageUrl(img);
        skuPo.setGmtModified(LocalDateTime.now());
        int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(skuPo);
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> deleteSku(Long shopId, Long skuId) {
        logger.debug("delete: shopId " + shopId + ", skuId " + skuId);
        GoodsSkuPo skuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (skuPo == null) {
            logger.debug("skuId not found");
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        if (!judgeResource(skuPo, shopId)) {
            logger.debug("sku don't belong to shop");
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        //TODO 商品状态修改,删除商品商品已删除时的错误码
        if (skuPo.getState().intValue() == 6)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);/*StatusWrap.ok();*/
        skuPo.setState((byte) 6);
        skuPo.setGmtModified(LocalDateTime.now());
        int ret = (goodsSkuPoMapper.updateByPrimaryKeySelective(skuPo));
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> updateSku(Long shopId, Long skuId, ModifySkuVo modifySkuVo) {
        GoodsSkuPo skuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (skuPo == null) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        if (!judgeResource(skuPo, shopId)) return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        GoodsSkuPo po = modifySkuVo.asNewSku().toGoodsSkuPo();
        po.setId(skuId);
        po.setGmtModified(LocalDateTime.now());
        int ret = (goodsSkuPoMapper.updateByPrimaryKeySelective(po));
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> putGoodsOnSale(Long shopId, Long skuId) {
        GoodsSkuPo skuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (skuPo == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!judgeResource(skuPo, shopId)) return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        //TODO 商品状态修改
        if (skuPo.getState().intValue() == 4)
            return StatusWrap.just(Status.STATE_NOCHANGE);
        skuPo.setState((byte) 4);
        skuPo.setGmtModified(LocalDateTime.now());
        int ret = (goodsSkuPoMapper.updateByPrimaryKeySelective(skuPo));
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> putOffGoodsOnSale(Long shopId, Long skuId) {
        GoodsSkuPo skuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (skuPo == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!judgeResource(skuPo, shopId)) return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        //TODO 商品状态修改
        if (skuPo.getState().intValue() == 0) {
            return StatusWrap.just(Status.STATE_NOCHANGE);
        }

        skuPo.setState((byte) 0);
        skuPo.setGmtModified(LocalDateTime.now());
        int ret = (goodsSkuPoMapper.updateByPrimaryKeySelective(skuPo));
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    public ResponseEntity<StatusWrap> addFloatingPrice(Long shopId, Long userId, String userName, Long skuId, FloatPricesGetVo vo) {
        FloatPricePoExample example = new FloatPricePoExample();
        FloatPricePoExample.Criteria criteria = example.createCriteria();

        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);

        if (goodsSkuPo == null) return StatusWrap.just(Status.SPU_NOTOPERABLE);
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(goodsSkuPo.getGoodsSpuId());
        if (goodsSpuPo == null) return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!goodsSpuPo.getShopId().equals(shopId)) return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        if (goodsSkuPo.getInventory() < vo.getQuantity())
            return StatusWrap.just(Status.SKU_NOTENOUGH);
        if (vo.getQuantity() < 0 || vo.getBeginTime().isBefore(LocalDateTime.now()) || vo.getEndTime().isBefore(LocalDateTime.now()))
            return StatusWrap.just(Status.FIELD_NOTVALID);


        FloatPricePo po = vo.toFloatPricePo(skuId, vo);

        po.setGmtCreate(LocalDateTime.now());
        po.setGmtModified(LocalDateTime.now());
        po.setValid((byte) 1);
        po.setCreatedBy(userId);
        po.setInvalidBy(userId);
        example.or().andGoodsSkuIdEqualTo(skuId).andValidEqualTo((byte) 1);

        List<FloatPricePo> floatPo = floatPricePoMapper.selectByExample(example);
        if (floatPo == null || floatPo.size() <= 0) {
            int ret = floatPricePoMapper.insertSelective(po);
            if (ret != 0) {
                FloatPricesReturnVo floatPricesReturnVo = new FloatPricesReturnVo(po);
                floatPricesReturnVo.setCreatedBy(userId, userName);
                floatPricesReturnVo.setModifiedBy(userId, userName);
                return StatusWrap.of(floatPricesReturnVo, HttpStatus.CREATED);
            } else {
                return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
            }
        }
        for (FloatPricePo floatPricePo : floatPo) {
            //TODO 无法判断两个浮动价格时间完全一样或开始与结束时间与已有活动有一个相同的情况
            if ((po.getBeginTime().isAfter(floatPricePo.getBeginTime()) && po.getEndTime().isBefore(floatPricePo.getEndTime())) || (po.getBeginTime().isAfter(floatPricePo.getBeginTime()) && po.getBeginTime().isBefore(floatPricePo.getEndTime()) && po.getEndTime().isAfter(floatPricePo.getEndTime())) || (po.getBeginTime().isBefore(floatPricePo.getBeginTime()) && po.getEndTime().isAfter(floatPricePo.getEndTime())) || (po.getBeginTime().isBefore(floatPricePo.getBeginTime()) && po.getEndTime().isBefore(floatPricePo.getEndTime()) && po.getEndTime().isAfter(floatPricePo.getBeginTime())))
                return StatusWrap.just(Status.SKUPRICE_CONFLICT);
        }

        int ret = floatPricePoMapper.insertSelective(po);
        if (ret != 0) {
            FloatPricesReturnVo floatPricesReturnVo = new FloatPricesReturnVo(po);
            return StatusWrap.of(floatPricesReturnVo);
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    //TODO 未将用户id与name存入
    public ResponseEntity<StatusWrap> invalidFloatPrice(Long userId, Long floatId) {
        FloatPricePoExample example = new FloatPricePoExample();
        FloatPricePoExample.Criteria criteria = example.createCriteria();
        FloatPricePo po = floatPricePoMapper.selectByPrimaryKey(floatId);
        if (po == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (po.getValid().intValue() == 0)
            return StatusWrap.ok();
        po.setValid((byte) 0);
        po.setGmtModified(LocalDateTime.now());
        po.setInvalidBy(userId);
        int ret = floatPricePoMapper.updateByPrimaryKeySelective(po);
        if (ret != 0) {
            return StatusWrap.ok();
        } else {
            return StatusWrap.just(Status.DATABASE_OPERATION_ERROR);
        }
    }

    /*
       接口实现
     */
    public FloatPricePo selectFloatPriceForCustomer(Long id) {
        FloatPricePoExample example = new FloatPricePoExample();
        FloatPricePoExample.Criteria criteria = example.createCriteria();
        example.or().andGoodsSkuIdEqualTo(id).andValidEqualTo((byte) 1);
        List<FloatPricePo> pos = floatPricePoMapper.selectByExample(example);


        if (pos == null || pos.size() <= 0) return null;
        for (FloatPricePo po : pos) {
            if (LocalDateTime.now().isAfter(po.getBeginTime()) && LocalDateTime.now().isBefore(po.getEndTime())) {
                return po;
            }
        }
        return null;
    }

    public GoodsSkuPo selectGoodsForCustomer(Long skuId) {
        GoodsSkuPo skuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (skuPo == null || skuPo.getState() != (byte) 4 || skuPo.getDisabled() == (byte) 1) return null;
        else return skuPo;
    }


    public Boolean compareInventoryBySkuId(Long skuId, Integer amount) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return false;
        }
        FloatPricePo floatPricePo = selectFloatPriceForCustomer(skuId);
        Integer inventory;
        if (floatPricePo != null) inventory = floatPricePo.getQuantity();
        else inventory = skuPo.getInventory();
        return amount <= inventory;
    }

    public Boolean deductInventory(Long skuId, Integer amount) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return false;
        }
        FloatPricePo floatPricePo = selectFloatPriceForCustomer(skuId);
        int ret = 0;
        if (floatPricePo != null) {
            if (amount <= floatPricePo.getQuantity()) {
                if (amount <= skuPo.getInventory()) {
                    skuPo.setInventory(skuPo.getInventory() - amount);
                    goodsSkuPoMapper.updateByPrimaryKeySelective(skuPo);
                    floatPricePo.setQuantity(floatPricePo.getQuantity() - amount);
                    ret = floatPricePoMapper.updateByPrimaryKeySelective(floatPricePo);
                }
            }
        } else {
            if (amount <= skuPo.getInventory()) {
                skuPo.setInventory(skuPo.getInventory() - amount);
                ret = goodsSkuPoMapper.updateByPrimaryKeySelective(skuPo);
            }
        }

        return ret != 0;
    }

    public Boolean increaseInventory(Long skuId, Integer amount) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return false;
        } else {
            skuPo.setInventory(skuPo.getInventory() + amount);
            int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(skuPo);
            return ret != 0;
        }
    }

    public Long findPriceBySkuId(Long skuId) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return null;
        } else {
            Long price = selectFloatPrice(skuId);
            return selectFloatPrice(skuId).intValue() == -1 ? skuPo.getOriginalPrice() : price;
        }
    }

    public String getGoodsNameBySkuId(Long skuId) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return null;
        } else return skuPo.getName();
    }

    public GoodsSkuDTO getSkuById(Long skuId) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return null;
        }

        GoodsSkuDTO goodsSkuDTO = new GoodsSkuDTO();
        goodsSkuDTO.setId(skuPo.getId());
        goodsSkuDTO.setName(skuPo.getName());
        goodsSkuDTO.setSkuSn(skuPo.getSkuSn());
        goodsSkuDTO.setImgUrl(skuPo.getImageUrl());
        goodsSkuDTO.setInventory(skuPo.getInventory());
        goodsSkuDTO.setOriginalPrice(skuPo.getOriginalPrice().intValue());
        Long price = selectFloatPrice(skuId);
        if (price.equals((long) -1)) goodsSkuDTO.setPrice(skuPo.getOriginalPrice().intValue());
        else goodsSkuDTO.setPrice(price.intValue());
        goodsSkuDTO.setDisable(skuPo.getDisabled() == (byte) 1);

        return goodsSkuDTO;
    }

    public Long getShopIdBySkuId(Long skuId) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return null;
        }
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(skuPo.getGoodsSpuId());
        if (goodsSpuPo == null) return null;
        return goodsSpuPo.getShopId();
    }

    public Boolean anbleChange(Long newGoodSkuId, Long goodSkuId) {
        if (newGoodSkuId.equals(goodSkuId)) return true;
        GoodsSkuPo goodsSkuPo1 = goodsSkuPoMapper.selectByPrimaryKey(newGoodSkuId);
        GoodsSkuPo goodsSkuPo2 = goodsSkuPoMapper.selectByPrimaryKey(goodSkuId);
        if (goodsSkuPo1 == null || goodsSkuPo2 == null) return false;
        return goodsSkuPo1.getGoodsSpuId().equals(goodsSkuPo2.getGoodsSpuId());
    }

    public GoodsInfoDTO getGoodsInfoDTOBySkuId(Long skuId) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(skuId);
        if (skuPo == null) {
            return null;
        }
        GoodsSpuPo spuPo = goodsSpuPoMapper.selectByPrimaryKey(skuPo.getGoodsSpuId());
        if (spuPo == null) {
            return null;
        }
        GoodsInfoDTO goodsInfoDTO = new GoodsInfoDTO();
        goodsInfoDTO.setWeight(skuPo.getWeight());
        goodsInfoDTO.setShopId(spuPo.getShopId());
        goodsInfoDTO.setFreightId(spuPo.getFreightId());
        return goodsInfoDTO;
    }

    public GoodsSkuInfo getGoodsSkuInfoAlone(Long goodsSkuId) {
        GoodsSkuPo skuPo = selectGoodsForCustomer(goodsSkuId);
        if (skuPo == null) {
            return null;
        }
        GoodsSkuInfo goodsSkuInfo = new GoodsSkuInfo();
        goodsSkuInfo.setSkuName(skuPo.getName());
        Long price = selectFloatPrice(goodsSkuId);
        if (price.equals((long) -1)) goodsSkuInfo.setPrice(skuPo.getOriginalPrice());
        else goodsSkuInfo.setPrice(price);
        return goodsSkuInfo;
    }
}
