package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.GrouponService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestController
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class GrouponController {
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private GrouponService grouponService;

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PostMapping(path = "/shops/{shopId}/spus/{id}/groupons")
    public Object createGrouponActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @PathVariable Long id,
            @RequestBody GrouponActivityVo vo) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
//      目前api中无法为团购活动设置name
//        if (vo.getName() == null || vo.getName().isBlank() || vo.getName().isEmpty())
//            return StatusWrap.just(Status.FIELD_NOTVALID);
//      若departId=0时无视shopid则改为departid，，此时会出现shopid为0的活动
        vo.setShopId(shopId);
        vo.setGoodsSpuId(id);
        vo.setState(GrouponActivity.State.OFFLINE.getCode().byteValue());
        return grouponService.createGrouponActivity(new GrouponActivity(vo));
    }

    @GetMapping(path = "/groupons/states")
    public Object getGrouponActivityStates() {
        return StatusWrap.of(Arrays.asList(GrouponActivity.State.values()));
    }

    @GetMapping(path = "/groupons")
    public Object getGrouponActivity(
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Long spu_id,
            @RequestParam(required = false) Integer timeline,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        GrouponActivityInVo grouponActivityInVo = new GrouponActivityInVo(shopId, spu_id, null, null, null, timeline, page, pageSize);
        return grouponService.getGrouponActivity(grouponActivityInVo);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @GetMapping(path = "/shops/{shopId}/groupons")
    public Object getallGrouponActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @RequestParam(required = false) Long spu_id,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) String beginStr,
            @RequestParam(required = false) String endStr,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LocalDateTime beginTime = null, endTime = null;
        try {
            if (beginStr != null) {
                beginTime = LocalDateTime.parse(beginStr);
            }
            if (endStr != null) {
                endTime = LocalDateTime.parse(endStr);
            }
        } catch (Exception e) {
            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyy-MMM-dd HH:mm:ss");
                if (beginStr != null) {
                    beginTime = LocalDateTime.parse(beginStr, format);
                }
                if (endStr != null) {
                    endTime = LocalDateTime.parse(endStr, format);
                }
            } catch (Exception ee) {
                return StatusWrap.just(Status.FIELD_NOTVALID);
            }
        }
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        GrouponActivityInVo grouponActivityInVo = new GrouponActivityInVo(shopId, spu_id, beginTime, endTime, state, null, page, pageSize);
        return grouponService.getallGrouponActivity(grouponActivityInVo);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping(path = "/shops/{shopId}/groupons/{id}")
    public Object modifyGrouponActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @PathVariable Long id,
            @RequestBody GrouponActivityVo vo) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        //若departId=0时无视shopid则改为departid
        vo.setShopId(shopId);
        return grouponService.modifyGrouponActivityById(id, vo);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping(path = "/shops/{shopId}/groupons/{id}/onshelves")
    public Object ONLINEGrouponActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @PathVariable Long id) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        //若departId=0时无视shopid则改为departid
        return grouponService.GtoONLINE(shopId, id);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping(path = "/shops/{shopId}/groupons/{id}/offshelves")
    public Object OFFLINEGrouponActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @PathVariable Long id) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        //若departId=0时无视shopid则改为departid
        return grouponService.GtoOFFLINE(shopId, id);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @DeleteMapping(path = "/shops/{shopId}/groupons/{id}")
    public Object deleteGrouponActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @PathVariable Long id) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        //若departId=0时无视shopid则改为departid
        return grouponService.deleteGrouponActivity(shopId, id);
    }
}
