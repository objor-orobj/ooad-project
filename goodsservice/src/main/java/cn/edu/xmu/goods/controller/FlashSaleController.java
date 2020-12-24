package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.ro.FlashSaleItemExtendedView;
import cn.edu.xmu.goods.model.vo.FlashSaleCreatorValidation;
import cn.edu.xmu.goods.model.vo.FlashSaleItemCreatorValidation;
import cn.edu.xmu.goods.model.vo.FlashSaleModifierValidation;
import cn.edu.xmu.goods.service.FlashSaleService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class FlashSaleController {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleController.class);
    @Autowired
    private FlashSaleService flashSaleService;

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping(
            path = "/shops/{shopId}/timesegments/{timeSegmentId}/flashsales",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> createFlashSale(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long timeSegmentId,
            @RequestBody FlashSaleCreatorValidation flashSaleVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.createWithinTimeSegment(flashSaleVo, timeSegmentId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping(path = "/shops/{shopId}/flashsales/{flashSaleId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<StatusWrap> modifyFlashSaleInfo(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId,
            @RequestBody FlashSaleModifierValidation FlashSaleVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.modifyInfo(flashSaleId, FlashSaleVo);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping(
            path = "/shops/{shopId}/flashsales/{flashSaleId}/onshelves",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> bringFlashSaleOnline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.bringOnline(flashSaleId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping(
            path = "/shops/{shopId}/flashsales/{flashSaleId}/offshelves",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> bringFlashSaleOffline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.bringOffline(flashSaleId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping(
            path = "/shops/{shopId}/flashsales/{flashSaleId}",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> cancelFlashSale(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.forceCancel(flashSaleId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping(
            path = "/shops/{shopId}/flashsales/{flashSaleId}/flashitems",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> addItemToFlashSale(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId,
            @RequestBody FlashSaleItemCreatorValidation itemVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.insertItem(flashSaleId, itemVo);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping(
            path = "/shops/{shopId}/flashsales/{flashSaleId}/flashitems/{flashSaleItemId}",
            produces = "application/json;charset=UTF-8"
    )
    public ResponseEntity<StatusWrap> removeFlashSaleItemById(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId,
            @PathVariable Long flashSaleItemId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.removeItem(flashSaleItemId);
    }

    @GetMapping(
            path = "/timesegments/{timeSegmentId}/flashsales",
//            produces = "application/stream+json;charset=UTF-8"
            produces = "application/json;charset=UTF-8"
    )
    public Flux<FlashSaleItemExtendedView> getFlashSaleItemsWithinTimeSegment(
            @PathVariable Long timeSegmentId
    ) {
        return flashSaleService.getFlashSaleItemsWithinTimeSegment(timeSegmentId);
    }

    @GetMapping(
            path = "/flashsales/current",
//            produces = "application/stream+json;charset=UTF-8"
            produces = "application/json;charset=UTF-8"
    )
    public Flux<FlashSaleItemExtendedView> getCurrentFlashSaleItems() {
        return flashSaleService.getCurrentFlashSaleItems();
    }
}
