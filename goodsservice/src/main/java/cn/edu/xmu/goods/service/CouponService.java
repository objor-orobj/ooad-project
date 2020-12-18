package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CouponDao;
import cn.edu.xmu.goods.dao.GoodsSkuDao;
import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.dto.CouponActivityDTO;
import cn.edu.xmu.goods.model.po.CouponPoExample;
import cn.edu.xmu.goods.model.po.CouponSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.ro.*;
import cn.edu.xmu.goods.model.vo.CouponActivityCreatorValidation;
import cn.edu.xmu.goods.model.vo.CouponActivityModifierValidation;
import cn.edu.xmu.goods.model.PageWrap;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.privilegeservice.client.IUserService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.goods.model.Status.*;

@Service
public class CouponService implements CouponServiceInterface {
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private ShopDao shopDao;
    @DubboReference
    private IUserService userService;
    @Autowired
    private GoodsSkuDao goodsSkuDao;

    @Value("${goods-service.image-pool.dav-url}")
    private String davUrl;

    @Value("${goods-service.image-pool.dav-username}")
    private String davUsername;

    @Value("${goods-service.image-pool.dav-password}")
    private String davPassword;

    @Value("${goods-service.image-pool.max-file-size}")
    private Integer maxFileSize;

    public ResponseEntity<StatusWrap> createActivityOfShop(
            CouponActivityCreatorValidation vo,
            Long shopId,
            Long userId
    ) {
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        String userName = userService.getUserName(userId);
        if (userName == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        CouponActivity create = new CouponActivity(vo);
        create.setShopId(shopId);
        create.setCreatorId(userId);
        create.setGmtCreated(LocalDateTime.now());
        CouponActivity saved = couponDao.createActivity(create);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.of(new CouponActivityExtendedView(
                saved,
                new ShopIdAndNameView(shop),
                new UserIdAndView(userId, userName),
                null
        ));
    }

    public ResponseEntity<StatusWrap> modifyActivityInfo(
            Long id,
            CouponActivityModifierValidation vo,
            Long userId,
            Long departId
    ) {
        CouponActivity origin = couponDao.selectActivity(id);
        if (origin == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!departId.equals(0L) && !departId.equals(origin.getShopId()))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        if (origin.getState() != CouponActivity.State.OFFLINE)
            return StatusWrap.just(Status.COUPON_ACTIVITY_STATE_DENIED);
        CouponActivity modified = new CouponActivity(vo);
        modified.setId(id);
        modified.setModifierId(userId);
        modified.setGmtModified(LocalDateTime.now());
        CouponActivity saved = couponDao.updateActivity(modified);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringActivityOnline(Long id, Long departId) {
        CouponActivity activity = couponDao.selectActivity(id);
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!departId.equals(0L) && !departId.equals(activity.getShopId()))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        if (activity.getState() == CouponActivity.State.ONLINE)
            return StatusWrap.ok();
        if (activity.getState() != CouponActivity.State.OFFLINE)
            return StatusWrap.just(Status.COUPON_ACTIVITY_STATE_DENIED);
        activity.setState(CouponActivity.State.ONLINE);
        activity.setGmtCreated(LocalDateTime.now());
        CouponActivity saved = couponDao.updateActivity(activity);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringActivityOffline(Long id, Long departId) {
        CouponActivity activity = couponDao.selectActivity(id);
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!departId.equals(0L) && !departId.equals(activity.getShopId()))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        if (activity.getState() == CouponActivity.State.OFFLINE)
            return StatusWrap.ok();
        if (activity.getState() != CouponActivity.State.ONLINE)
            return StatusWrap.just(Status.COUPON_ACTIVITY_STATE_DENIED);
        activity.setState(CouponActivity.State.OFFLINE);
        activity.setGmtCreated(LocalDateTime.now());
        CouponActivity saved = couponDao.updateActivity(activity);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> deleteActivity(Long id, Long departId) {
        CouponActivity activity = couponDao.selectActivity(id);
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (!departId.equals(0L) && !departId.equals(activity.getShopId()))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        if (activity.getState() == CouponActivity.State.DELETED)
            return StatusWrap.ok();
        if (activity.getState() != CouponActivity.State.OFFLINE)
            return StatusWrap.just(Status.COUPON_ACTIVITY_STATE_DENIED);
        activity.setState(CouponActivity.State.DELETED);
        activity.setGmtCreated(LocalDateTime.now());
        CouponActivity saved = couponDao.updateActivity(activity);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> setActivityImage(Long activityId, MultipartFile file, Long userId, Long reqDepartId) {
        CouponActivity activity = couponDao.selectActivity(activityId);
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (activity.getState() != CouponActivity.State.OFFLINE)
            return StatusWrap.just(Status.COUPON_ACTIVITY_STATE_DENIED);
        String delete = activity.getImageUrl();
        ReturnObject<?> ret;
        try {
            ret = ImgHelper.remoteSaveImg(file, maxFileSize, davUsername, davPassword, davUrl);
        } catch (IOException ioException) {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
        if (ret.getCode() != ResponseCode.OK) {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
        String image = ret.getData().toString();
        activity.setImageUrl(image);
        activity.setModifierId(userId);
        activity.setGmtModified(LocalDateTime.now());
        CouponActivity saved = couponDao.updateActivity(activity);
        if (saved == null) {
            ImgHelper.deleteRemoteImg(image, davUsername, davPassword, davUrl);
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
        ImgHelper.deleteRemoteImg(delete, davUsername, davPassword, davUrl);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> selectOnlineActivities(
            Long shopId,
            Integer timeline,
            Integer page,
            Integer pageSize
    ) {
        PageWrap shrunk = couponDao.selectActivitiesPaged(
                shopId,
                timeline,
                page,
                pageSize,
                false
        );
        if (shrunk == null) return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.of(shrunk);
    }

    public ResponseEntity<StatusWrap> selectHiddenActivitiesOfShop(Long shopId, Integer page, Integer pageSize) {
        PageWrap shrunk = couponDao.selectActivitiesPaged(
                shopId,
                null,
                page,
                pageSize,
                true
        );
        if (shrunk == null) return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.of(shrunk);
    }

    public ResponseEntity<StatusWrap> selectItemsPaged(Long id, Integer pageNum, Integer pageSize) {
        CouponActivity activity = couponDao.selectActivity(id);
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (activity.getState() != CouponActivity.State.ONLINE)
            return StatusWrap.just(RESOURCE_ID_NOTEXIST);
        PageWrap paged = couponDao.selectItemsPaged(id, pageNum, pageSize);
        if (paged == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.of(paged);
    }


    public ResponseEntity<StatusWrap> selectActivity(Long activityId, Long visitorDepartId) {
        CouponActivity activity = couponDao.selectActivity(activityId);
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if ((activity.getState() != CouponActivity.State.ONLINE && visitorDepartId == -2)
                || (visitorDepartId > 0 && !activity.getShopId().equals(visitorDepartId))) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        Shop shop = shopDao.select(activity.getShopId());
        String creatorName = userService.getUserName(activity.getCreatorId());
        String modifierName;
        if (creatorName == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        UserIdAndView creator = new UserIdAndView(activity.getCreatorId(), creatorName);
        UserIdAndView modifier = null;
        if (activity.getModifierId() != null) {
            modifierName = userService.getUserName(activity.getModifierId());
            if (modifierName == null)
                return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
            modifier = new UserIdAndView(activity.getModifierId(), modifierName);
        }
        return StatusWrap.of(new CouponActivityExtendedView(
                activity,
                new ShopIdAndNameView(shop),
                creator,
                modifier
        ));
    }

    public ResponseEntity<StatusWrap> addItemsToActivity(Long activityId, List<Long> skuIds, Long departId) {
        CouponActivity activity = couponDao.selectActivity(activityId);
        // activity not exist
        if (activity == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        // shop admin has no right beyond his own shop
        if (!departId.equals(0L) && !departId.equals(activity.getShopId()))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        // load existing items
        List<Long> exist = couponDao.selectItemIdsOfActivity(activityId);
        // database error
        if (exist == null)
            return StatusWrap.just(INTERNAL_SERVER_ERR);
        // load sku data
        for (Long skuId : skuIds) {
            // if sku already exists
            if (exist.contains(skuId))
                return StatusWrap.just(COUPON_ACTIVITY_ITEM_DUPLICATED);
            // if sku id not exist
            GoodsSkuPo po = goodsSkuDao.getSkuPoById(skuId.intValue());
            if (po == null)
                return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
            // if sku offline
            if (po.getDisabled().equals((byte) 1))
                return StatusWrap.just(GOODS_STATE_DENIED);
            // if bad shop owner
            if (!departId.equals(0L) && !departId.equals(goodsSkuDao.getShopIdBySkuId(skuId)))
                return StatusWrap.just(RESOURCE_ID_OUTSCOPE);
        }
        // keep track of newly inserted items
        List<Long> written = new ArrayList<>();
        for (Long skuId : skuIds) {
            CouponSkuPo saved = couponDao.insertItem(activityId, skuId);
            // if error inserting item
            if (saved == null) {
                for (Long rollback : written)
                    couponDao.deleteItem(rollback);
                return StatusWrap.just(INTERNAL_SERVER_ERR);
            }
            written.add(saved.getId());
        }
        // seems good
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> removeItem(Long itemId, Long departId) {
        CouponSkuPo po = couponDao.selectItem(itemId);
        // database error
        if (po == null)
            return StatusWrap.just(INTERNAL_SERVER_ERR);
        // not exist
        if (po.getId() == 0)
            return StatusWrap.just(RESOURCE_ID_NOTEXIST);
        // no right
        Long shopId = goodsSkuDao.getShopIdBySkuId(po.getSkuId());
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(RESOURCE_ID_OUTSCOPE);
        // database error
        Long delete = couponDao.deleteItem(itemId);
        if (delete == null)
            return StatusWrap.just(INTERNAL_SERVER_ERR);
        // seems ok
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> selectCouponByUser(
            Long userId,
            Integer page,
            Integer pageSize,
            Coupon.State state
    ) {
        PageInfo<CouponUserView> paged = couponDao.selectCouponOfUser(userId, page, pageSize, state);
        if (paged == null)
            return StatusWrap.just(INTERNAL_SERVER_ERR);
        return StatusWrap.of(paged);
    }

    public ResponseEntity<StatusWrap> userClaimCoupon(Long activityId, Long userId) {
        return couponDao.tryClaimCoupon(activityId, userId);
    }

    @Override
    public ArrayList<CouponActivityDTO> getCouponActivityAlone(Long userId, Long goodsSkuId) {
        List<CouponActivity> all = couponDao.selectApplicableActivityOfGoods(goodsSkuId);
        // exception occurred
        if (all == null)
            return null;
        // if non-existent
        if (all.size() == 0)
            return new ArrayList<>();
        // no need for coupon
        List<CouponActivity> eligible = all.stream()
                .filter(activity -> activity.getQuantity().equals(0))
                .collect(Collectors.toList());
        List<Long> eligibleIds = eligible.stream().map(CouponActivity::getId).collect(Collectors.toList());
        // coupon needed
        List<CouponActivity> needCoup = all.stream()
                .filter(activity -> !activity.getQuantity().equals(0))
                .collect(Collectors.toList());
        List<Long> needCoupIds = needCoup.stream().map(CouponActivity::getId).collect(Collectors.toList());
        // user owned coupons
        List<Coupon> owned = couponDao.selectCouponOfActivitiesOwnedByUser(needCoupIds, userId);
        List<Long> ownedIds = owned.stream().map(Coupon::getActivityId).collect(Collectors.toList());
        // intersection of
        //   activities that requires a coupon
        //   and activities that user owns a coupon
        ownedIds.retainAll(needCoupIds);
        // union of
        //   activities that requires no coupon
        //   and activities that user owns coupon of
        eligibleIds.addAll(ownedIds);
        // load activity
        eligible = all.stream().filter(activity -> eligibleIds.contains(activity.getId())).collect(Collectors.toList());
        // load dto
        ArrayList<CouponActivityDTO> view =
                eligible.stream().map(po -> {
                    CouponActivityDTO dto = new CouponActivityDTO();
                    dto.setId(po.getId());
                    dto.setName(po.getName());
                    dto.setBeginTime(po.getBeginTime());
                    dto.setEndTIme(po.getEndTime());
                    return dto;
                }).collect(Collectors.toCollection(ArrayList::new));
        return view;
    }
}
