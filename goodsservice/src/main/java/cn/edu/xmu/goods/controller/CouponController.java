package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.vo.CouponActivityCreatorValidation;
import cn.edu.xmu.goods.model.vo.CouponActivityModifierValidation;
import cn.edu.xmu.goods.service.CouponService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@RestController
public class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);
    @Autowired
    private CouponService couponService;

    @GetMapping(path = "/coupons/states", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> getCouponStates() {
        return StatusWrap.of(Arrays.asList(Coupon.State.values()));
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping(path = "/shops/{shopId}/couponactivities", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> createCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @Validated @RequestBody CouponActivityCreatorValidation couponActivityVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        logger.debug("create coupon activity: shopId " + shopId + ", departId " + departId + ", userId " + userId);
        return couponService.createActivityOfShop(couponActivityVo, shopId, userId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping(
            path = "/shops/{shopId}/couponactivities/{couponActivityId}",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> modifyCouponActivities(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long couponActivityId,
            @Validated @RequestBody CouponActivityModifierValidation couponActivityVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.modifyActivityInfo(couponActivityId, couponActivityVo, userId, departId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "img", required = true, dataType = "file", paramType = "formData")
    })
    @Audit
    @PostMapping(
            path = "/shops/{shopId}/couponactivities/{couponActivityId}/uploadImg",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> uploadCouponActivityImage(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long couponActivityId,
            @RequestParam("img") MultipartFile file
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.setActivityImage(couponActivityId, file, userId, departId);
    }

    @GetMapping(path = "/couponactivities", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> userGetAvailableCouponActivitiesPaged(
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Integer timeline,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return couponService.selectOnlineActivities(shopId, timeline, page, pageSize);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @GetMapping(path = "/shops/{shopId}/couponactivities/invalid", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> getHiddenCouponActivitiesOfShopPaged(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.selectHiddenActivitiesOfShop(shopId, page, pageSize);
    }

    @GetMapping(path = "/couponactivities/{activityId}/skus", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> getItemsOfCouponActivity(
            @PathVariable Long activityId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return couponService.selectItemsPaged(activityId, page, pageSize);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @GetMapping(path = "/shops/{shopId}/couponactivities/{activityId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> getDetailedCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.selectActivity(activityId, departId);
    }


    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping(
            path = "/shops/{shopId}/couponactivities/{activityId}/onshelves",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> bringCouponActivityOnline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.bringActivityOnline(activityId, departId);
    }


    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping(
            path = "/shops/{shopId}/couponactivities/{id}/offshelves",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> bringCouponActivityOffline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.bringActivityOffline(id, departId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping(path = "/shops/{shopId}/couponactivities/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> deleteCouponActivity(
            @PathVariable Long shopId,
            @PathVariable Long id,
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.deleteActivity(id, departId);
    }


    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping(
            path = "/shops/{shopId}/couponactivities/{activityId}/skus",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> addItemsToCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId,
            @RequestBody List<Long> skuIds
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.addItemsToActivity(activityId, skuIds, departId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping(path = "/shops/{shopId}/couponskus/{activityId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> removeItemFromCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.removeItem(activityId, departId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @GetMapping(path = "/coupons", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> buyerFetchItsCouponsPaged(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @RequestParam(required = false) Integer state,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.selectCouponByUser(userId, page, pageSize, Coupon.State.fromCode(state));
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping(path = "/couponactivities/{activityId}/usercoupons", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> userClaimCoupon(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long activityId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.userClaimCoupon(activityId, userId);
    }
}
