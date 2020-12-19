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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@RestController
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class PresaleController {
    @Autowired
    private PresaleService presaleService;

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PostMapping(path = "/shops/{shopId}/skus/{id}/presales")
    public Object createPresaleActivity(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable Long shopId,
            @PathVariable Long id,
            @RequestBody PresaleActivityVo vo) {
        if (vo.getName() == null || vo.getName().isEmpty() || vo.getName().isBlank())
            return StatusWrap.just(Status.FIELD_NOTVALID);
        vo.setState(PresaleActivity.State.OFFLINE.getCode().byteValue());
        return presaleService.createPresaleActivity(shopId, id, vo);
    }

    @GetMapping(path = "/presales/states")
    public Object getPresaleActivityStates() {
        return StatusWrap.of(Arrays.asList(PresaleActivity.State.values()));
    }

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
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @GetMapping(path = "/shops/{shopId}/presales")
    public Object getallPresaleActivity(
            @PathVariable Long shopId,
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Integer state,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(shopId) || !departId.equals(0L))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        PresaleActivityInVo presaleActivityInVo = new PresaleActivityInVo(shopId, skuId, state, null, page, pageSize);
        return presaleService.getallPresaleActivity(presaleActivityInVo);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping(path = "/shops/{shopId}/presales/{id}")
    public Object modifyPresaleActivity(@PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityModifyVo vo) {
        vo.setShopId(shopId);
        if (vo.getName() == null || vo.getName().isEmpty() || vo.getName().isBlank())
            return StatusWrap.just(Status.FIELD_NOTVALID);
        return presaleService.modifyPresaleActivityById(id, vo);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping(path = "/shops/{shopId}/presales/{id}/onshelves")
    public Object ONLINEPresaleActivity(@PathVariable Long shopId, @PathVariable Long id) {
        return presaleService.PtoONLINE(shopId, id);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping(path = "/shops/{shopId}/presales/{id}/offshelves")
    public Object OFFLINEPresaleActivity(@PathVariable Long shopId, @PathVariable Long id) {
        return presaleService.PtoOFFLINE(shopId, id);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @DeleteMapping(path = "/shops/{shopId}/presales/{id}")
    public Object deletePresaleActivity(@PathVariable Long shopId, @PathVariable Long id) {
        return presaleService.deletePresaleActivity(shopId, id);
    }

}
