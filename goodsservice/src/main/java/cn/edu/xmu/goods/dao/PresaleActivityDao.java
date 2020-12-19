package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPoExample;
import cn.edu.xmu.goods.model.vo.PresaleActivityCreateVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityInVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityModifyVo;
import cn.edu.xmu.goods.model.vo.ReturnGoodsSkuVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PresaleActivityDao {
    @Autowired(required = false)
    PresaleActivityPoMapper presaleActivityPoMapper;
    @Autowired(required = false)
    private GoodsSkuPoMapper goodsSkuPoMapper;
    @Autowired(required = false)
    private GoodsSpuPoMapper goodsSpuPoMapper;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private GoodsSkuDao goodsSkuDao;

    public ResponseEntity<StatusWrap> createPresaleActivity(PresaleActivity presaleActivity) {
        PresaleActivityPo po = presaleActivity.getPresaleActivityPo();
        GoodsSkuPo goodsSku = goodsSkuPoMapper.selectByPrimaryKey(po.getGoodsSkuId());
        GoodsSpuPo goodsSpu = goodsSpuPoMapper.selectByPrimaryKey(goodsSku.getGoodsSpuId());
        if (goodsSpu.getShopId() != po.getShopId() && po.getShopId() != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        int ret = presaleActivityPoMapper.insert(po);
        if (ret != 0) {
            return StatusWrap.of(po, HttpStatus.CREATED);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public PresaleActivityPo getPresaleActivityById(Long Id) {

        PresaleActivityPo presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(Id);

        return presaleActivityPo;
    }

    public ResponseEntity<StatusWrap> getPresaleActivity(PresaleActivityInVo vo) {
        List<PresaleActivityPo> presaleActivityList = null;
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(vo.getPage(), vo.getPageSize());
        if (vo.getTimeline() != null) {
            if (vo.getGoodsSkuId() != null) {
                switch (vo.getTimeline()) {
                    case 0:
                        criteria.andGoodsSkuIdEqualTo(vo.getGoodsSkuId())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDateTime.now());
                    case 1:
                        criteria.andGoodsSkuIdEqualTo(vo.getGoodsSkuId())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDate.now().plusDays(1).atTime(LocalTime.MIN))
                                .andBeginTimeLessThan(LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
                    case 2:
                        criteria.andGoodsSkuIdEqualTo(vo.getGoodsSkuId())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now())
                                .andEndTimeGreaterThan(LocalDateTime.now());
                    case 3:
                        criteria.andGoodsSkuIdEqualTo(vo.getGoodsSkuId())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now());
                }
            } else if (vo.getShopid() != null) {
                switch (vo.getTimeline()) {
                    case 0:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDateTime.now());
                    case 1:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDate.now().plusDays(1).atTime(LocalTime.MIN))
                                .andBeginTimeLessThan(LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
                    case 2:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now())
                                .andEndTimeGreaterThan(LocalDateTime.now());
                    case 3:
                        criteria.andShopIdEqualTo(vo.getShopid())
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now());
                }
            } else {
                switch (vo.getTimeline()) {
                    case 0:
                        criteria
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDateTime.now());
                    case 1:
                        criteria
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeGreaterThan(LocalDate.now().plusDays(1).atTime(LocalTime.MIN))
                                .andBeginTimeLessThan(LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
                    case 2:
                        criteria
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now())
                                .andEndTimeGreaterThan(LocalDateTime.now());
                    case 3:
                        criteria
                                .andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue())
                                .andBeginTimeLessThan(LocalDateTime.now());
                }
            }
        } else {
            if (vo.getGoodsSkuId() != null) {
                criteria.andGoodsSkuIdEqualTo(vo.getGoodsSkuId()).andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue());
            } else if (vo.getShopid() != null) {
                criteria.andShopIdEqualTo(vo.getShopid()).andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue());
            } else {
                criteria.andStateEqualTo(PresaleActivity.State.ONLINE.getCode().byteValue());
            }
        }
        try {
            presaleActivityList = presaleActivityPoMapper.selectByExample(example);
        } catch (
                DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getPresaleActivity: ").append(e.getMessage());
        }

        if (null == presaleActivityList || presaleActivityList.isEmpty()) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        } else {
            List<PresaleActivityCreateVo> presaleActivities = presaleActivityList.stream().map(
                    x -> {
                        Shop shop = shopDao.select(x.getShopId());
                        ReturnGoodsSkuVo goodsSku = goodsSkuDao.getSingleSimpleSku(x.getGoodsSkuId().intValue());
                        return new PresaleActivityCreateVo(shop, goodsSku, x);
                    }
            ).collect(Collectors.toList());
            PageInfo<PresaleActivityCreateVo> presaleActivityPageInfo = PageInfo.of(presaleActivities);
            return StatusWrap.of(presaleActivityPageInfo);
        }
    }

    public ResponseEntity<StatusWrap> getallPresaleAcitvity(PresaleActivityInVo vo) {
        if(vo.getGoodsSkuId() != null) {
            if (goodsSkuDao.getShopIdBySkuId(vo.getGoodsSkuId().longValue()) != vo.getShopid()) {
                return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
            }
        }
        List<PresaleActivityPo> presaleActivityList = null;
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(vo.getPage(), vo.getPageSize());
        if (vo.getState() != null) {
            if (vo.getGoodsSkuId() != null) {
                criteria.andStateEqualTo(vo.getState().byteValue()).andGoodsSkuIdEqualTo(vo.getGoodsSkuId());

            } else {
                criteria.andStateEqualTo(vo.getState().byteValue()).andShopIdEqualTo(vo.getShopid());
            }
        } else {
            if (vo.getGoodsSkuId() != null) {
                criteria.andGoodsSkuIdEqualTo(vo.getGoodsSkuId());

            } else {
                criteria.andShopIdEqualTo(vo.getShopid());
            }
        }
        try {
            presaleActivityList = presaleActivityPoMapper.selectByExample(example);
        } catch (
                DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getallPresaleActivity: ").append(e.getMessage());
        }

        if (null == presaleActivityList || presaleActivityList.isEmpty()) {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        } else {
            List<PresaleActivityCreateVo> presaleActivities = presaleActivityList.stream().map(
                    x -> {
                        Shop shop = shopDao.select(x.getShopId());
                        ReturnGoodsSkuVo goodsSku = goodsSkuDao.getSingleSimpleSkuPLUS(x.getGoodsSkuId().intValue());
                        return new PresaleActivityCreateVo(shop, goodsSku, x);
                    }
            ).collect(Collectors.toList());
            PageInfo<PresaleActivityCreateVo> presaleActivityPageInfo = PageInfo.of(presaleActivities);
            return StatusWrap.of(presaleActivityPageInfo);
        }
    }

    public ResponseEntity<StatusWrap> modifyPresaleActivity(Long id, PresaleActivityModifyVo vo) {
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po.getState() != PresaleActivity.State.OFFLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.PRESALE_STATENOTALLOW);
        }
        if (po.getShopId() != vo.getShopId() && vo.getShopId() != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if(vo.getBeginTime().isBefore(LocalDateTime.now())
                || vo.getPayTime().isBefore(LocalDateTime.now())
                || vo.getEndTime().isBefore(LocalDateTime.now())
                || vo.getQuantity()<0
                || vo.getAdvancePayPrice()<0
                || vo.getRestPayPrice()<0
        )
        {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        po.setQuantity(vo.getQuantity());
        po.setAdvancePayPrice(vo.getAdvancePayPrice());
        po.setRestPayPrice(vo.getRestPayPrice());
        po.setName(vo.getName());
        po.setBeginTime(vo.getBeginTime());
        po.setPayTime(vo.getPayTime());
        po.setEndTime(vo.getEndTime());
        po.setGmtModified(LocalDateTime.now());
        int ret = presaleActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public boolean deductPresaleActivityquantity(Long id, Integer quantity) {
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po.getQuantity() < quantity) {
            return false;
        }
        if (po.getState() != PresaleActivity.State.ONLINE.getCode().byteValue()) {
            return false;
        }
        po.setQuantity(po.getQuantity() - quantity);
        po.setGmtModified(LocalDateTime.now());
        int ret = presaleActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean plusPresaleActivityquantity(Long id, Integer quantity) {
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po.getState() != PresaleActivity.State.ONLINE.getCode().byteValue()) {
            return false;
        }
        po.setQuantity(po.getQuantity() + quantity);
        po.setGmtModified(LocalDateTime.now());
        int ret = presaleActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return true;
        } else {
            return false;
        }
    }

    public Long getearnestBySkuId(Long id) {
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        return po.getAdvancePayPrice();
    }

    public ResponseEntity<StatusWrap> PtoONLINE(Long shopId, Long id) {
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po.getShopId() != shopId && shopId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getState() != PresaleActivity.State.OFFLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.PRESALE_STATENOTALLOW);
        }
        po.setGmtModified(LocalDateTime.now());
        po.setState(PresaleActivity.State.ONLINE.getCode().byteValue());
        int ret = presaleActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public ResponseEntity<StatusWrap> PtoOFFLINE(Long shopId, Long id) {
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po.getShopId() != shopId && shopId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getState() != PresaleActivity.State.ONLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.PRESALE_STATENOTALLOW);
        }

        po.setGmtModified(LocalDateTime.now());
        po.setState(PresaleActivity.State.OFFLINE.getCode().byteValue());
        int ret = presaleActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }

    public ResponseEntity<StatusWrap> deletePresaleActivityById(Long shopId, Long id) {
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po.getShopId() != shopId && shopId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getState() != PresaleActivity.State.OFFLINE.getCode().byteValue()) {
            return StatusWrap.just(Status.PRESALE_STATENOTALLOW);
        }
        po.setGmtModified(LocalDateTime.now());
        po.setState(PresaleActivity.State.DELETE.getCode().byteValue());
        int ret = presaleActivityPoMapper.updateByPrimaryKeySelective(po);

        if (ret != 0) {
            return StatusWrap.just(Status.OK);
        } else {
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
    }
}
