package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.GrouponService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
public class GrouponController {
    @Autowired
    private GrouponService grouponService;

    @ApiOperation(value="管理员新增SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "商品Spu_id", required = true),
            @ApiImplicitParam(paramType="body",dataType = "PresaleActivityVo",name="vo",value="团购活动信息",required=true)

    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PostMapping(path = "/shops/{shopId}/spus/{id}/groupons")
    public Object createGrouponActivity(@PathVariable Long shopId,
                                        @PathVariable Long id,
                                        @Validated @RequestBody GrouponActivityVo vo) {
        vo.setShopId(shopId);
        vo.setGoodsSpuId(id);
        vo.setState(GrouponActivity.State.OFFLINE.getCode().byteValue());
        return grouponService.createGrouponActivity(new GrouponActivity(vo));
    }

    @ApiOperation(value="获得团购活动的所有状态")
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/groupons/states")
    public List<GrouponActivity.State> getGrouponActivityStates() {
        return Arrays.asList(GrouponActivity.State.values());
    }

    @ApiOperation(value="查询所有有效的预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "shopId", value = "商铺id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "spu_id", value = "商品Spu_id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/groupons")
    public Object getGrouponActivity(@RequestParam(required = false) Long shopId,
                                     @RequestParam(required = false) Long spu_id,
                                     @RequestParam(required = false) Integer timeline,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer pageSize) {
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        GrouponActivityInVo grouponActivityInVo= new GrouponActivityInVo(shopId,spu_id,null,null,null,timeline,page,pageSize);
        return grouponService.getGrouponActivity(grouponActivityInVo);
    }

    @ApiOperation(value="管理员查询SPU所有团购活动(包括下线的)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "spu_id", value = "商品Spu_id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "beginTime", value = "活动开始时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "endTime", value = "活动结束时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/shops/{id}/groupons")
    public Object  getallGrouponActivity(@PathVariable Long id,
                                         @RequestParam(required = false) Long spu_id,
                                         @RequestParam(required = false) Integer state,
                                         @RequestParam(required = false) LocalDateTime beginTime,
                                         @RequestParam(required = false) LocalDateTime endTime,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer pageSize) {
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        GrouponActivityInVo grouponActivityInVo =new GrouponActivityInVo(id,spu_id,beginTime,endTime,state,null,page,pageSize);
        return grouponService.getallGrouponActivity(grouponActivityInVo);
    }

    @GetMapping(path = "/shops/{shopId}/spus/{id}/groupons")
    public Object getGrouponActivityByspuid(@PathVariable Long id)
    {
        return grouponService.getGrouponActivitybyspuid(id);
    }

    @ApiOperation(value="管理员修改团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "团购活动id", required = true),
            @ApiImplicitParam(paramType="body",dataType = "PresaleActivityModifyVo",name="vo",value="预售活动修改信息",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping(path = "/shops/{shopId}/groupons/{id}")
    public Object modifyGrouponActivity(@PathVariable Long id, @Validated @RequestBody GrouponActivityVo vo) {
        return grouponService.modifyGrouponActivityById(id, vo);
    }

    @ApiOperation(value="管理员上线团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "团购活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping(path = "/shops/{shopId}/groupons/{id}/onshelves")
    public Object ONLINEGrouponActivity(@PathVariable Long shopId,@PathVariable Long id)
    {
        return grouponService.GtoONLINE(shopId,id);
    }

    @ApiOperation(value="管理员下线团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "团购活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping(path = "/shops/{shopId}/groupons/{id}/offshelves")
    public Object OFFLINEGrouponActivity(@PathVariable Long shopId,@PathVariable Long id)
    {
        return grouponService.GtoOFFLINE(shopId,id);
    }

    @ApiOperation(value="管理员删除团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "团购活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @DeleteMapping(path = "/shops/{shopId}/groupons/{id}")
    public Object deleteGrouponActivity(@PathVariable Long shopId,@PathVariable Long id) {
        return grouponService.deleteGrouponActivity(shopId,id);
    }
}
