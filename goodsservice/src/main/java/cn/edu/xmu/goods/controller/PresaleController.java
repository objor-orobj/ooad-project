package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.PresaleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class PresaleController {
    @Autowired
    private PresaleService presaleService;

    @ApiOperation(value="管理员新增SKU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "商品Sku_id", required = true),
            @ApiImplicitParam(paramType="body",dataType = "PresaleActivityVo",name="vo",value="预售活动信息",required=true)

    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PostMapping(path = "/shops/{shopId}/skus/{id}/presales")
    public Object createPresaleActivity(@PathVariable Long shopId,
                                        @PathVariable Long id,
                                        @Validated @RequestBody PresaleActivityVo vo) {
        vo.setState(PresaleActivity.State.OFFLINE.getCode().byteValue());
        return presaleService.createPresaleActivity(shopId,id,vo);
    }

    @ApiOperation(value="获得预售活动的所有状态")
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/presales/states")
    public  Object getPresaleActivityStates(){
        return StatusWrap.of(Arrays.asList(PresaleActivity.State.values()));
    }

    @ApiOperation(value="查询所有有效的预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "shopId", value = "商铺id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "skuId", value = "商品Sku_id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/presales")
    public Object getPresaleActivity(@RequestParam(required = false) Long shopId,
                                                                      @RequestParam(required = false) Integer timeline,
                                                                      @RequestParam(required = false) Long skuId,
                                                                      @RequestParam(required = false) Integer page,
                                                                      @RequestParam(required = false) Integer pageSize){
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        PresaleActivityInVo presaleActivityInVo = new PresaleActivityInVo(shopId,skuId,null,timeline,page,pageSize);
        return presaleService.getPresaleActivity(presaleActivityInVo);
    }

    @ApiOperation(value="管理员查询SPU所有预售活动(包括下线的)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "skuId", value = "商品Sku_id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/shops/{shopId}/presales")
    public Object getallPresaleActivity(@PathVariable Long shopId,
                                                                         @RequestParam(required = false) Long skuId,
                                                                         @RequestParam(required = false) Integer state,
                                                                         @RequestParam(required = false) Integer page,
                                                                         @RequestParam(required = false) Integer pageSize) {
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        PresaleActivityInVo presaleActivityInVo = new PresaleActivityInVo(shopId,skuId,state,null,page,pageSize);
        return presaleService.getallPresaleActivity(presaleActivityInVo);
    }

    @ApiOperation(value="管理员修改SKU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true),
            @ApiImplicitParam(paramType="body",dataType = "PresaleActivityModifyVo",name="vo",value="预售活动修改信息",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping(path = "/shops/{shopId}/presales/{id}")
    public Object modifyPresaleActivity(@PathVariable Long shopId ,@PathVariable Long id, @Validated @RequestBody PresaleActivityModifyVo vo)
    {
        vo.setShopId(shopId);
        return presaleService.modifyPresaleActivityById(id,vo);
    }

    @ApiOperation(value="管理员上线预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping(path = "/shops/{shopId}/presales/{id}/onshelves")
    public Object ONLINEPresaleActivity(@PathVariable Long shopId,@PathVariable Long id)
    {
        return presaleService.PtoONLINE(shopId,id);
    }

    @ApiOperation(value="管理员下线预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping(path = "/shops/{shopId}/presales/{id}/offshelves")
    public Object OFFLINEPresaleActivity(@PathVariable Long shopId,@PathVariable Long id)
    {
        return presaleService.PtoOFFLINE(shopId,id);
    }

    @ApiOperation(value="管理员删除预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "预售活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @DeleteMapping(path = "/shops/{shopId}/presales/{id}")
    public Object deletePresaleActivity(@PathVariable Long shopId,@PathVariable Long id) {
        return presaleService.deletePresaleActivity(shopId,id);
    }

}
