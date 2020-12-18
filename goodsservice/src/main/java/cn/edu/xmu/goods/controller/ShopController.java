package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.ShopAuditionValidation;
import cn.edu.xmu.goods.model.vo.ShopCreatorValidation;
import cn.edu.xmu.goods.service.ShopService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@RestController
public class ShopController {
    @Autowired
    private ShopService shopService;

    @GetMapping("/shops/states")
    public ResponseEntity<StatusWrap> getShopStates() {
        return StatusWrap.of(Arrays.asList(Shop.State.values()));
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping("/shops")
    public ResponseEntity<StatusWrap> applyForNewShop(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @Validated @RequestBody ShopCreatorValidation vo
    ) {
        if (userId == null || departId == null || departId == -2) {
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        }
        if (departId > 0) {
            return StatusWrap.just(Status.USER_HAS_SHOP);
        }
        userId = 1L;
        return shopService.createByUser(vo, userId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @Audit
    @PutMapping("/shops/{shopId}")
    public ResponseEntity<StatusWrap> modifyShopInfo(
            @PathVariable Long shopId,
            @Depart @ApiIgnore Long departId,
            @Validated @RequestBody ShopCreatorValidation vo
    ) {
        if (!shopId.equals(departId)) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return shopService.modifyInfo(shopId, vo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @Audit
    @DeleteMapping("/shops/{shopId}")
    public ResponseEntity<StatusWrap> forceCloseShop(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId
    ) {
        if (userId == null || departId == null) {
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        }
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return shopService.forceClose(shopId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @Audit
    @PutMapping("/shops/{zero}/newshops/{shopId}/audit")
    public ResponseEntity<StatusWrap> auditNewShop(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long zero,
            @PathVariable Long shopId,
            @Validated @RequestBody ShopAuditionValidation vo
    ) {
        if (userId == null || departId == null) {
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        }
        if (departId != 0 || zero != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return shopService.audit(shopId, vo.getConclusion());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @Audit
    @PutMapping("/shops/{shopId}/onshelves")
    public ResponseEntity<StatusWrap> bringShopOnline(
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId
    ) {
        if (!shopId.equals(departId)) {
            return StatusWrap.of(Status.RESOURCE_ID_OUTSCOPE);
        }
        return shopService.bringOnline(shopId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @Audit
    @PutMapping("/shops/{shopId}/offshelves")
    public ResponseEntity<StatusWrap> bringShopOffline(
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId) {
        if (!shopId.equals(departId)) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return shopService.bringOffline(shopId);
    }
}
