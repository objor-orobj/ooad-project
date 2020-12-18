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
    @Autowired
    private CouponService couponService;

    @GetMapping("/coupons/states")
    public ResponseEntity<StatusWrap> getCouponStates() {
        return StatusWrap.of(Arrays.asList(Coupon.State.values()));
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping("/shops/{shopId}/couponactivities")
    public ResponseEntity<StatusWrap> createCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @Validated @RequestBody CouponActivityCreatorValidation couponActivityVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        // shop owner only, no super user
        if (!departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.createActivityOfShop(couponActivityVo, shopId, userId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping("/shops/{shopId}/couponactivities/{couponActivityId}")
    public ResponseEntity<StatusWrap> modifyCouponActivities(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long couponActivityId,
            @Validated @RequestBody CouponActivityModifierValidation couponActivityVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.modifyActivityInfo(couponActivityId, couponActivityVo, userId, departId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "img", required = true, dataType = "file", paramType = "formData")
    })
    @Audit
    @PostMapping("/shops/{shopId}/couponactivities/{couponActivityId}/uploadImg")
    public ResponseEntity<StatusWrap> uploadCouponActivityImage(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long couponActivityId,
            @RequestParam("img") MultipartFile file
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.setActivityImage(couponActivityId, file, userId, departId);
    }

    @GetMapping("/couponactivities")
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
    @GetMapping("/shops/{shopId}/couponactivities/invalid")
    public ResponseEntity<StatusWrap> getHiddenCouponActivitiesOfShopPaged(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return couponService.selectHiddenActivitiesOfShop(shopId, page, pageSize);
    }

    @GetMapping("/couponactivities/{activityId}/skus")
    public ResponseEntity<StatusWrap> getItemsOfCouponActivity(
            @PathVariable Long activityId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return couponService.selectItemsPaged(activityId, page, pageSize);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @GetMapping("/shops/{shopId}/couponactivities/{activityId}")
    public ResponseEntity<StatusWrap> getDetailedCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.selectActivity(activityId, departId);
    }


    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping("/shops/{shopId}/couponactivities/{activityId}/onshelves")
    public ResponseEntity<StatusWrap> bringCouponActivityOnline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.bringActivityOnline(activityId, departId);
    }


    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping("/shops/{shopId}/couponactivities/{id}/offshelves")
    public ResponseEntity<StatusWrap> bringCouponActivityOffline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.bringActivityOffline(id, departId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping("/shops/{shopId}/couponactivities/{id}")
    public ResponseEntity<StatusWrap> deleteCouponActivity(
            @PathVariable Long shopId,
            @PathVariable Long id,
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.deleteActivity(id, departId);
    }


    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping("/shops/{shopId}/couponactivities/{activityId}/skus")
    public ResponseEntity<StatusWrap> addItemsToCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId,
            @RequestBody List<Long> skuIds
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.addItemsToActivity(activityId, skuIds, departId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping("/shops/{shopId}/couponskus/{activityId}")
    public ResponseEntity<StatusWrap> removeItemFromCouponActivity(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long activityId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return couponService.removeItem(activityId, departId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @GetMapping("/coupons")
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
    @PostMapping("/couponactivities/{activityId}/usercoupons")
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
