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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@RestController
public class ShopController {
    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    @GetMapping(path = "/shops/states", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> getShopStates() {
        return StatusWrap.of(Arrays.asList(Shop.State.values()));
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping(path = "/shops", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> applyForNewShop(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @RequestBody ShopCreatorValidation vo
    ) {
        logger.debug("create shop: userId = " + userId + ", departId = " + departId);
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
    @PutMapping(path = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> modifyShopInfo(
            @PathVariable Long shopId,
            @Depart @ApiIgnore Long departId,
            @RequestBody ShopCreatorValidation vo
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
    @DeleteMapping(path = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
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
    @PutMapping(path = "/shops/{zero}/newshops/{shopId}/audit", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> auditNewShop(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long zero,
            @PathVariable Long shopId,
            @RequestBody ShopAuditionValidation vo
    ) {
        if (vo.getConclusion() == null)
            return StatusWrap.just(Status.FIELD_NOTVALID);
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
    @PutMapping(path = "/shops/{shopId}/onshelves", produces = "application/json;charset=UTF-8")
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
    @PutMapping(path = "/shops/{shopId}/offshelves", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> bringShopOffline(
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId) {
        if (!shopId.equals(departId)) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return shopService.bringOffline(shopId);
    }
}
