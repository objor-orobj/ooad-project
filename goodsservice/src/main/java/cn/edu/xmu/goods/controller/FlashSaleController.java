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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class FlashSaleController {
    @Autowired
    private FlashSaleService flashSaleService;

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping("/shops/{shopId}/timesegments/{timeSegmentId}/flashsales")
    public ResponseEntity<StatusWrap> createFlashSale(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long timeSegmentId,
            @Validated @RequestBody FlashSaleCreatorValidation flashSaleVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.createWithinTimeSegment(flashSaleVo, timeSegmentId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping("/shops/{shopId}/flashsales/{flashSaleId}")
    public ResponseEntity<StatusWrap> modifyFlashSaleInfo(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId,
            @Validated @RequestBody FlashSaleModifierValidation FlashSaleVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.modifyInfo(flashSaleId, FlashSaleVo);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping("/shops/{shopId}/flashsales/{flashSaleId}/onshelves")
    public ResponseEntity<StatusWrap> bringFlashSaleOnline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.bringOnline(flashSaleId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PutMapping("/shops/{shopId}/flashsales/{flashSaleId}/offshelves")
    public ResponseEntity<StatusWrap> bringFlashSaleOffline(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.bringOffline(flashSaleId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping("/shops/{shopId}/flashsales/{flashSaleId}")
    public ResponseEntity<StatusWrap> cancelFlashSale(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.forceCancel(flashSaleId);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @PostMapping("/shops/{shopId}/flashsales/{flashSaleId}/flashitems")
    public ResponseEntity<StatusWrap> addItemToFlashSale(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId,
            @Validated @RequestBody FlashSaleItemCreatorValidation itemVo
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.insertItem(flashSaleId, itemVo);
    }

    @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header")
    @Audit
    @DeleteMapping("/shops/{shopId}/flashsales/{flashSaleId}/flashitems/{flashSaleItemId}")
    public ResponseEntity<StatusWrap> removeFlashSaleItemById(
            @LoginUser @ApiIgnore Long userId,
            @Depart @ApiIgnore Long departId,
            @PathVariable Long shopId,
            @PathVariable Long flashSaleId,
            @PathVariable Long flashSaleItemId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return flashSaleService.removeItem(flashSaleItemId);
    }

    @GetMapping(path = "/timesegments/{timeSegmentId}/flashsales", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<FlashSaleItemExtendedView> getFlashSaleItemsWithinTimeSegment(
            @PathVariable Long timeSegmentId
    ) {
        return flashSaleService.getFlashSaleItemsWithinTimeSegment(timeSegmentId);
    }

    @GetMapping(path = "/flashsale/current", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<FlashSaleItemExtendedView> getCurrentFlashSaleItems() {
        return flashSaleService.getCurrentFlashSaleItems();
    }
}
