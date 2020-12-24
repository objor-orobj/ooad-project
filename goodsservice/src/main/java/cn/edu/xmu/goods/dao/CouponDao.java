package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.controller.ShopController;
import cn.edu.xmu.goods.mapper.CouponActivityPoMapper;
import cn.edu.xmu.goods.mapper.CouponPoMapper;
import cn.edu.xmu.goods.mapper.CouponSkuPoMapper;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.ro.CouponActivityShrunkView;
import cn.edu.xmu.goods.model.ro.CouponUserView;
import cn.edu.xmu.goods.model.PageWrap;
import cn.edu.xmu.goods.model.vo.ReturnGoodsSkuVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.bloom.BloomFilterHelper;
import cn.edu.xmu.ooad.util.bloom.RedisBloomFilter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.edu.xmu.goods.model.Status.*;
import static cn.edu.xmu.goods.model.Status.COUPON_END;

@Repository
public class CouponDao implements InitializingBean {
    @Autowired
    private CouponActivityPoMapper couponActivityPoMapper;
    @Autowired
    private CouponSkuPoMapper couponSkuPoMapper;
    @Autowired
    private CouponPoMapper couponPoMapper;
    @Autowired
    private GoodsSkuDao goodsSkuDao;
//    @Autowired
//    private RedisTemplate<String, Serializable> redis;

    private static final Logger logger = LoggerFactory.getLogger(CouponDao.class);

//    @Value("${goods-service.coupon-activity.redis-timeout}")
//    private Integer timeout;

//    private RedisBloomFilter<CharSequence> bloom;
//
//    @Value("${goods-service.coupon-activity.bloom-estimated-coupon-qty}")
//    private Integer bloomSize;
//
//    @Value("${goods-service.coupon-activity.bloom-acceptable-error-rate}")
//    private Double bloomErrorRate;

    @Override
    public void afterPropertiesSet() throws Exception {
//        BloomFilterHelper<CharSequence> helper = new BloomFilterHelper<>(
//                Funnels.stringFunnel(Charsets.UTF_8), bloomSize, bloomErrorRate
//        );
//        bloom = new RedisBloomFilter<>(redis, helper);
    }

    public CouponActivity createActivity(@NotNull CouponActivity activity) {
        CouponActivityPo po = activity.toCouponActivityPo();
        try {
            couponActivityPoMapper.insert(po);
//            if (activity.getType() == CouponActivity.Type.LIMITED_INVENTORY) {
//                logger.debug("creating pre-generated coupons");
//                for (int i = 0; i < activity.getQuantity(); ++i) {
//                    CouponPo justPo = new CouponPo();
//                    justPo.setName(activity.getName());
//                    justPo.setCouponSn(Integer.valueOf(i).toString());
//                    justPo.setActivityId(activity.getId());
//                    justPo.setState((byte) 0);
//                    justPo.setGmtCreate(LocalDateTime.now());
//                    couponPoMapper.insert(justPo);
//                    logger.debug("created coupon: id " + justPo.getId());
//                }
//            }
        } catch (DataAccessException exception) {
            logger.debug("error creating activity");
            return null;
        }
        logger.debug("created activity: id " + po.getId());
        return new CouponActivity(po);
    }

    /*
     * the one, and the only one entrance to getting CouponActivity by id
     * automatically cache into redis
     * automatically load Coupon if needed
     */
    public CouponActivity selectActivity(@NotNull Long id) {
//        CouponActivity value, cache;
//        // try fetch from redis
//        cache = (CouponActivity) redis.opsForValue().get("CA" + id);
//        if (cache != null) {
//            logger.debug("redis hit");
//            // getId() == null if activity non-existent
//            value = cache.getId().equals(-1L) ? null : cache;
//            logger.debug("cache: id " + cache.getId());
//        } else {
//            logger.debug("redis miss");
//            // try fetch from database
//            CouponActivityPo po;
//            try {
//                po = couponActivityPoMapper.selectByPrimaryKey(id);
//            } catch (DataAccessException exception) {
//                logger.debug("database exception");
//                return null;
//            }
//            // save null-ID CouponActivity into redis if non-existent
//            if (po == null) {
//                logger.debug("database not found");
//                value = null;
//                cache = new CouponActivity();
//                cache.setId(-1L);
//            } else {
//                logger.debug("database hit");
//                // save CouponActivity into redis
//                cache = value = new CouponActivity(po);
//                if (value.getQuantity() > 0 // if Coupon needed
//                        && value.getCouponTime().isBefore(LocalDateTime.now().plusSeconds(timeout)) // soon starting
//                        && value.getBeginTime().isBefore(LocalDateTime.now())// activity online
//                        && value.getEndTime().isAfter(LocalDateTime.now()) // activity online
//                        && value.getState() != CouponActivity.State.DELETED // not deleted
//                ) {
//                    logger.debug("loading coupons");
//                    CouponPoExample example = new CouponPoExample();
//                    CouponPoExample.Criteria criteria = example.createCriteria();
//                    criteria.andActivityIdEqualTo(value.getId());
//                    // fetch list of existing Coupons
//                    List<CouponPo> couponPoList;
//                    try {
//                        couponPoList = couponPoMapper.selectByExample(example);
//                    } catch (DataAccessException exception) {
//                        return null;
//                    }
//                    logger.debug("coupons in total: " + value.getQuantity());
//                    logger.debug("coupons taken: " + couponPoList.size());
//                    // save number of existing Coupons into redis
//                    redis.opsForValue().set("CN" + id, couponPoList.size(), timeout, TimeUnit.SECONDS);
//                    // clean up previous bloom
//                    redis.delete("CU" + id);
//                    // load userId into bloom
//                    for (CouponPo couponPo : couponPoList)
//                        bloom.addByBloomFilter("CU" + id, couponPo.getCustomerId().toString());
//                }
//            }
//        }
//        redis.opsForValue().set("CA" + id, cache, timeout, TimeUnit.SECONDS);
//        return value;
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) return null;
        return new CouponActivity(po);
    }

    /*
     * the one, and the only one entrance to updating CouponActivity by id
     * automatically invalidate redis cache
     */
    public CouponActivity updateActivity(@NotNull CouponActivity activity) {
        try {
            couponActivityPoMapper.updateByPrimaryKeySelective(activity.toCouponActivityPo());
        } catch (DataAccessException exception) {
            return null;
        }
//        redis.delete("CA" + activity.getId());
        return activity;
    }


    public PageWrap selectActivitiesPaged(
            Long shopId,
            Integer timeline,
            Integer page,
            Integer pageSize,
            Boolean invalid
    ) {
        logger.debug("shopId: " + shopId);
        logger.debug("timeline: " + timeline);
        logger.debug("page: " + page);
        logger.debug("pageSize: " + pageSize);
        logger.debug("invalid: " + invalid);
        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        // 按店铺查询
        if (shopId != null)
            criteria.andShopIdEqualTo(shopId);
        // 管理员查看下线活动，用户查看上线活动
        if (invalid)
            criteria.andStateEqualTo(CouponActivity.State.OFFLINE.getCode().byteValue());
        else
            criteria.andStateEqualTo(CouponActivity.State.ONLINE.getCode().byteValue());
        // 按时间线
        if (timeline != null) {
            switch (timeline) {
                case 0:
                    criteria.andBeginTimeGreaterThan(LocalDateTime.now());
                    break;
                case 1:
                    criteria.andBeginTimeBetween(
                            LocalDate.now().plusDays(1).atTime(LocalTime.MIN),
                            LocalDate.now().plusDays(1).atTime(LocalTime.MAX)
                    );
                    break;
                case 2:
                    criteria.andBeginTimeLessThan(LocalDateTime.now())
                            .andEndTimeGreaterThan(LocalDateTime.now());
                    break;
                case 3:
                    criteria.andEndTimeLessThan(LocalDateTime.now());
                    break;
            }
        }
        List<CouponActivityPo> activityPos;
        PageHelper.startPage(page, pageSize);
        try {
            activityPos = couponActivityPoMapper.selectByExample(example);
        } catch (DataAccessException exception) {
            return null;
        }
        PageInfo<CouponActivityPo> info = PageInfo.of(activityPos);
        List<CouponActivity> activities = activityPos.stream().map(CouponActivity::new).collect(Collectors.toList());
//        for (CouponActivity activity : activities)
//            redis.opsForValue().set("CA" + activity.getId(), activity, timeout, TimeUnit.SECONDS);
        List<CouponActivityShrunkView> view
                = activities.stream().map(CouponActivityShrunkView::new).collect(Collectors.toList());
        return PageWrap.of(info, view);
    }

    public PageWrap selectItemsPaged(Long id, Integer pageNum, Integer pageSize) {
        CouponSkuPoExample example = new CouponSkuPoExample();
        CouponSkuPoExample.Criteria criteria = example.createCriteria();
        criteria.andActivityIdEqualTo(id);
        PageHelper.startPage(pageNum, pageSize);
        List<CouponSkuPo> items;
        try {
            items = couponSkuPoMapper.selectByExample(example);
        } catch (DataAccessException exception) {
            return null;
        }
        if (items == null || items.size() <= 0)
            return null;
        PageInfo<CouponSkuPo> info = PageInfo.of(items);
        List<ReturnGoodsSkuVo> view = items.stream().map(
                item -> goodsSkuDao.getSingleSimpleSkuPLUS(item.getSkuId().intValue())
        ).collect(Collectors.toList());
        return PageWrap.of(info, view);
    }

    public List<Long> selectItemIdsOfActivity(Long activityId) {
        List<CouponSkuPo> items;
        CouponSkuPoExample example = new CouponSkuPoExample();
        CouponSkuPoExample.Criteria criteria = example.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        try {
            items = couponSkuPoMapper.selectByExample(example);
        } catch (DataAccessException exception) {
            return null;
        }
        if (items == null || items.size() <= 0)
            return new ArrayList<>();
        return items.stream().map(CouponSkuPo::getSkuId).collect(Collectors.toList());
    }

    /*
     * return coupon_sku of given primary key
     * null on database failure
     * po of id 0 if not found
     */
    public CouponSkuPo selectItem(Long id) {
        CouponSkuPo po;
        try {
            po = couponSkuPoMapper.selectByPrimaryKey(id);
        } catch (DataAccessException exception) {
            return null;
        }
        if (po == null) {
            CouponSkuPo skuPo = new CouponSkuPo();
            skuPo.setId(0L);
            return skuPo;
        }
        return po;
    }

    /*
     *  unconditionally create item of given activity and sku
     *  no state, no ownership, no nothing checked
     *  return null on database error
     */
    public CouponSkuPo insertItem(Long activityId, Long skuId) {
        CouponSkuPo record = new CouponSkuPo();
        record.setActivityId(activityId);
        record.setSkuId(skuId);
        record.setGmtCreate(LocalDateTime.now());
        try {
            couponSkuPoMapper.insert(record);
        } catch (DataAccessException exception) {
            return null;
        }
        return record;
    }

    /*
     * DANGER ZONE !!!
     * remove by primary key
     */
    public Long deleteItem(Long itemId) {
        try {
            couponSkuPoMapper.deleteByPrimaryKey(itemId);
        } catch (DataAccessException exception) {
            return null;
        }
        return itemId;
    }

    public PageInfo<CouponUserView> selectCouponOfUser(
            Long userId,
            Integer page,
            Integer pageSize,
            Coupon.State state
    ) {
        CouponPoExample example = new CouponPoExample();
        CouponPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        if (state != null) criteria.andStateEqualTo(state.getCode().byteValue());
        PageHelper.startPage(page, pageSize);
        List<CouponPo> coupons;
        try {
            coupons = couponPoMapper.selectByExample(example);
        } catch (DataAccessException exception) {
            return null;
        }
        List<CouponUserView> view = coupons.stream().map(
                po -> new CouponUserView(
                        new Coupon(po),
                        selectActivity(po.getActivityId())
                )
        ).collect(Collectors.toList());
        return PageInfo.of(view);
    }

    public ResponseEntity<StatusWrap> tryClaimCoupon(Long activityId, Long userId) {
        // load activity
        CouponActivity activity = selectActivity(activityId);
        logger.debug("try claim: activityId " + activity + ", userId " + userId);
        // no such activity
        if (activity == null) {
            logger.debug("no such activity");
            return StatusWrap.just(RESOURCE_ID_NOTEXIST);
        }
        logger.debug("activity.quantity: " + activity.getQuantity());
        // no need for coupon
        if (activity.getQuantity().equals(0))
            return StatusWrap.just(COUPON_NO_NEED);
        logger.debug("activity.couponTime: " + activity.getCouponTime());
        // coupon time not met
        if (activity.getCouponTime().isAfter(LocalDateTime.now()))
            return StatusWrap.just(COUPON_NOTBEGIN);
        logger.debug("activity.endTime: " + activity.getEndTime());
        // activity ended
        if (activity.getEndTime().isBefore(LocalDateTime.now()))
            return StatusWrap.just(COUPON_END);
//        boolean exist = bloom.includeByBloomFilter("CU" + activityId, userId.toString());
//        logger.debug("in bloom: " + exist);
        // user already has coupon
        CouponPoExample example = new CouponPoExample();
        CouponPoExample.Criteria criteria = example.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        List<CouponPo> taken;
        try {
            taken = couponPoMapper.selectByExample(example);
        } catch (DataAccessException exception) {
            return StatusWrap.just(INTERNAL_SERVER_ERR);
        }
        List<Long> userIds = taken.stream().map(CouponPo::getCustomerId).collect(Collectors.toList());
        boolean exist = userIds.contains(userId);
        if (exist)
            return StatusWrap.just(COUPON_ALREADY_CLAIMED);
        if (activity.getType() == CouponActivity.Type.SINGLE_MAXIMUM) {
            return claimUnlimited(activity, userId);
        } else {
            if (userIds.size() >= activity.getQuantity())
                return StatusWrap.just(COUPON_FINISH);
            return claimLimited(activity, userId);
        }
    }

    // CouponActivity.Type.SINGLE_MAXIMUM
    private ResponseEntity<StatusWrap> claimUnlimited(CouponActivity activity, Long userId) {
        logger.debug("claim with no condition");
        List<Coupon> coupons = new ArrayList<>();
        // this should've been:
        // generate id and po and return coupons immediately
        // write to database with mq async
        for (int i = 0; i < activity.getQuantity(); i++) {
            CouponPo justPo = new CouponPo();
            justPo.setName(activity.getName());
            justPo.setCustomerId(userId);
            justPo.setActivityId(activity.getId());
            justPo.setBeginTime(LocalDateTime.now());
            justPo.setEndTime(
                    activity.getValidTerm().equals(0)
                            ? activity.getEndTime()
                            : LocalDateTime.now().plusDays(activity.getValidTerm())
            );
            justPo.setState((byte) 1);
            justPo.setGmtCreate(LocalDateTime.now());
            justPo.setCouponSn(activity.getId() + userId.toString() + i);
            try {
                couponPoMapper.insert(justPo);
            } catch (DataAccessException exception) {
                return StatusWrap.just(INTERNAL_SERVER_ERR);
            }
            coupons.add(new Coupon(justPo));
        }
        List<String> view = coupons.stream().map(Coupon::getCouponSn).collect(Collectors.toList());
        return StatusWrap.of(view, HttpStatus.CREATED);
    }

    // CouponActivity.Type.LIMITED_INVENTORY
    private ResponseEntity<StatusWrap> claimLimited(CouponActivity activity, Long userId) {
        logger.debug("limited number of coupons");
//        Long incremented = redis.opsForValue().increment("CN" + activity.getId());
//        if (incremented == null) {
//            return StatusWrap.just(INTERNAL_SERVER_ERR);
//        }
//        logger.debug("current taken in redis: " + incremented);
//        logger.debug("maximum: " + activity.getQuantity());
//        if (incremented > activity.getQuantity())
//            return StatusWrap.just(COUPON_FINISH);
        // this should've been:
        // generate id and po and return coupons immediately
        // write to database with mq async
//        CouponPoExample example = new CouponPoExample();
//        CouponPoExample.Criteria criteria = example.createCriteria();
//        criteria.andActivityIdEqualTo(activity.getId()).andStateEqualTo((byte) 0);
//        List<CouponPo> couponPoList;
//        try {
//            couponPoList = couponPoMapper.selectByExample(example);
//        } catch (DataAccessException exception) {
//            return StatusWrap.just(INTERNAL_SERVER_ERR);
//        }
//        if (couponPoList == null)
//            return StatusWrap.just(INTERNAL_SERVER_ERR);
//        // this should not happened if perfect
//        if (couponPoList.size() <= 0)
//            return StatusWrap.just(COUPON_FINISH);
//        CouponPo his = couponPoList.get(0);
        CouponPo his = new CouponPo();
        his.setActivityId(activity.getId());
        his.setCustomerId(userId);
        his.setBeginTime(LocalDateTime.now());
        his.setEndTime(
                activity.getValidTerm().equals(0)
                        ? activity.getEndTime()
                        : LocalDateTime.now().plusDays(activity.getValidTerm())
        );
        his.setState(Coupon.State.TAKEN.getCode().byteValue());
        his.setCouponSn(activity.getId() + userId.toString());
        his.setGmtCreate(LocalDateTime.now());
        try {
            couponPoMapper.insert(his);
        } catch (DataAccessException exception) {
            exception.printStackTrace();
            return StatusWrap.just(INTERNAL_SERVER_ERR);
        }
        List<String> listed = Collections.singletonList(his.getCouponSn());
        activity.setQuantity(activity.getQuantity() - 1);
        updateActivity(activity);
        return StatusWrap.of(listed, HttpStatus.CREATED);
    }

    // for interface
    public List<CouponActivity> selectApplicableActivityOfGoods(Long skuId) {
        // find coupon_spu's of given skuId
        CouponSkuPoExample couponSkuExample = new CouponSkuPoExample();
        CouponSkuPoExample.Criteria couponSkuCriteria = couponSkuExample.createCriteria();
        couponSkuCriteria.andSkuIdEqualTo(skuId);
        List<CouponSkuPo> itemPos;
        try {
            itemPos = couponSkuPoMapper.selectByExample(couponSkuExample);
        } catch (DataAccessException exception) {
            return null;
        }
        // return empty list if no such activity
        if (itemPos.size() == 0)
            return new ArrayList<>();
        // preserve only activityId
        List<Long> activityIds = itemPos.stream().map(CouponSkuPo::getActivityId).collect(Collectors.toList());
        // load coupon_activities of activityIds
        CouponActivityPoExample activityExample = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria activityCriteria = activityExample.createCriteria();
        activityCriteria.andIdIn(activityIds)
                .andStateEqualTo(CouponActivity.State.ONLINE.getCode().byteValue())// online only
                .andEndTimeGreaterThan(LocalDateTime.now());// not expired
        List<CouponActivityPo> activityPos;
        try {
            activityPos = couponActivityPoMapper.selectByExample(activityExample);
        } catch (DataAccessException exception) {
            return null;
        }
        // return empty list if no eligible ones
        if (activityPos.size() == 0)
            return new ArrayList<>();
        return activityPos.stream().map(CouponActivity::new).collect(Collectors.toList());
    }

    // for interface
    public List<Coupon> selectCouponOfActivitiesOwnedByUser(List<Long> activityIds, Long userId) {
        CouponPoExample example = new CouponPoExample();
        CouponPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userId) // owned by user
                .andActivityIdIn(activityIds) // belong to activities
                .andStateEqualTo(Coupon.State.TAKEN.getCode().byteValue()) // usable
                .andEndTimeGreaterThan(LocalDateTime.now());// not expired
        List<CouponPo> couponPos;
        try {
            couponPos = couponPoMapper.selectByExample(example);
        } catch (DataAccessException exception) {
            return null;
        }
        // if empty
        if (couponPos.size() == 0)
            return new ArrayList<>();
        return couponPos.stream().map(Coupon::new).collect(Collectors.toList());
    }

    // for interface
    public Coupon selectCoupon(@NotNull Long couponId) {
        CouponPo po;
        try {
            po = couponPoMapper.selectByPrimaryKey(couponId);
        } catch (DataAccessException exception) {
            return null;
        }
        return new Coupon(po);
    }

    public Coupon updateCoupon(@NotNull Coupon coupon) {
        CouponPo po = coupon.toCouponPo();
        try {
            couponPoMapper.updateByPrimaryKeySelective(po);
        } catch (DataAccessException exception) {
            return null;
        }
        return new Coupon(po);
    }
}