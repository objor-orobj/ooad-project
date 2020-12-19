package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.PresaleService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class PresaleController {
    @Autowired
    private PresaleService presaleService;

    @Audit
    @ApiOperation(value = "管理员新增SKU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "商品Sku_id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "PresaleActivityVo", name = "vo", value = "预售活动信息", required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PostMapping(path = "/shops/{shopId}/skus/{id}/presales")
    public Object createPresaleActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @PathVariable Long id,
            @RequestBody PresaleActivityVo vo) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        if (vo.getName() == null || vo.getName().isEmpty() || vo.getName().isBlank())
            return StatusWrap.just(Status.FIELD_NOTVALID);
        vo.setState(PresaleActivity.State.OFFLINE.getCode().byteValue());
//      若departId=0时无视shopid则改为departid，此时会出现shopid为0的活动
        return presaleService.createPresaleActivity(shopId, id, vo);
    }

    @ApiOperation(value = "获得预售活动的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping(path = "/presales/states")
    public Object getPresaleActivityStates() {
        return StatusWrap.of(Arrays.asList(PresaleActivity.State.values()));
    }

    @ApiOperation(value = "查询所有有效的预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "shopId", value = "商铺id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "skuId", value = "商品Sku_id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping(path = "/presales")
    public Object getPresaleActivity(
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Integer timeline,
            @RequestParam(required = false) Long skuId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PresaleActivityInVo presaleActivityInVo = new PresaleActivityInVo(shopId, skuId, null, timeline, page, pageSize);
        return presaleService.getPresaleActivity(presaleActivityInVo);
    }

    @Audit
    @ApiOperation(value = "管理员查询SPU所有预售活动(包括下线的)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "skuId", value = "商品Sku_id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping(path = "/shops/{shopId}/presales")
    public Object getallPresaleActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Integer state,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
//        page = (page == null) ? 1 : page;
//        pageSize = (pageSize == null) ? 10 : pageSize;
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        PresaleActivityInVo presaleActivityInVo = new PresaleActivityInVo(shopId, skuId, state, null, page, pageSize);
        return presaleService.getallPresaleActivity(presaleActivityInVo);
    }

    @Audit
    @ApiOperation(value = "管理员修改SKU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "PresaleActivityModifyVo", name = "vo", value = "预售活动修改信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PutMapping(path = "/shops/{shopId}/presales/{id}")
    public Object modifyPresaleActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityModifyVo vo) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        if (vo.getName() == null || vo.getName().isEmpty() || vo.getName().isBlank())
            return StatusWrap.just(Status.FIELD_NOTVALID);
        vo.setShopId(shopId);
        return presaleService.modifyPresaleActivityById(id, vo);
    }

    @Audit
    @ApiOperation(value = "管理员上线预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PutMapping(path = "/shops/{shopId}/presales/{id}/onshelves")
    public Object ONLINEPresaleActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,@PathVariable Long shopId, @PathVariable Long id) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return presaleService.PtoONLINE(shopId, id);
    }

    @Audit
    @ApiOperation(value = "管理员下线预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PutMapping(path = "/shops/{shopId}/presales/{id}/offshelves")
    public Object OFFLINEPresaleActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,@PathVariable Long shopId, @PathVariable Long id) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return presaleService.PtoOFFLINE(shopId, id);
    }

    @Audit
    @ApiOperation(value = "管理员删除预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @DeleteMapping(path = "/shops/{shopId}/presales/{id}")
    public Object deletePresaleActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,@PathVariable Long shopId, @PathVariable Long id) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) && !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return presaleService.deletePresaleActivity(shopId, id);
    }

}
