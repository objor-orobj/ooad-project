package cn.edu.xmu.goods.controller;


import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.BrandService;
import cn.edu.xmu.goods.service.GoodsCategoryService;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
//
@RestController
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PostMapping("/shops/{shopId}/brands")
    public Object createBrand(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @RequestBody BrandVo vo,
            @PathVariable Long shopId) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return brandService.createBrand(vo);
    }

    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PostMapping("/shops/{shopId}/categories/{id}/subcategories")
    public Object createGoodsCategory(@RequestBody GoodsCategoryVo vo,
                                      @Depart @ApiIgnore Long departId,
                                      @LoginUser @ApiIgnore Long userId,
                                      @PathVariable("id") Long id,
                                      @PathVariable("shopId") Long shopId) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return goodsCategoryService.createGoodsCategory(id, vo);
    }

    @Audit
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "img", required = true, dataType = "file", paramType = "formData")
    })
    @PostMapping("/shops/{shopId}/brands/{id}/uploadImg")
    public Object uploadBrandImage(@Depart @ApiIgnore Long departId,
                                   @LoginUser @ApiIgnore Long userId,
                                   @PathVariable Long id,
                                   @PathVariable Long shopId,
                                   @RequestParam String imageUrl) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return brandService.uploadBrandImage(id, imageUrl);
    }

    @GetMapping("/brands")
    public Object getAllBrands(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        return brandService.getAllBrands(page, pageSize);
    }

    @GetMapping("/categories/{id}/subcategories")
    public Object getGoodsCategory(@PathVariable Long id) {
        return goodsCategoryService.getGoodsCategory(id);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping("/shops/{shopId}/brands/{id}")
    public Object modifyBrand(@Depart @ApiIgnore Long departId,
                              @LoginUser @ApiIgnore Long userId,
                              @PathVariable("id") Long id,
                              @PathVariable("shopId") Long shopId,
                              @RequestBody BrandVo vo) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return brandService.modifyBrand(id, vo);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PutMapping("/shops/{shopId}/categories/{id}")
    public Object modifyGoodsCategory(@PathVariable Long id,
                                      @PathVariable Long shopId,
                                      @Depart @ApiIgnore Long departId,
                                      @LoginUser @ApiIgnore Long userId,
                                      @RequestBody GoodsCategoryVo vo) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return goodsCategoryService.modifyGoodsCategory(id, vo);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @DeleteMapping("/shops/{shopId}/brands/{id}")
    public Object deleteBrand(@PathVariable Long id,
                              @Depart @ApiIgnore Long departId,
                              @LoginUser @ApiIgnore Long userId,
                              @PathVariable Long shopId) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return brandService.deleteBrand(id);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @DeleteMapping("/shops/{shopId}/categories/{id}")
    public Object deleteGoodsCategory(@PathVariable Long id,
                                      @PathVariable Long shopId,
                                      @Depart @ApiIgnore Long departId,
                                      @LoginUser @ApiIgnore Long userId
    ) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return goodsCategoryService.deleteGoodsCategory(id);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PostMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object addSpuToBrand(@PathVariable Long id,
                                @PathVariable Long spuId,
                                @PathVariable Long shopId,
                                @Depart @ApiIgnore Long departId,
                                @LoginUser @ApiIgnore Long userId) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return brandService.addSpuToBrand(id, spuId);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @PostMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object addSpuToCategory(@PathVariable("id") Long id,
                                   @PathVariable("spuId") Long spuId,
                                   @PathVariable("shopId") Long shopId,
                                   @Depart @ApiIgnore Long departId,
                                   @LoginUser @ApiIgnore Long userId) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return goodsCategoryService.addSpuToCategory(id, spuId);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object removeSpuFromBrand(@PathVariable Long id,
                                     @PathVariable Long spuId,
                                     @PathVariable Long shopId,
                                     @Depart @ApiIgnore Long departId,
                                     @LoginUser @ApiIgnore Long userId) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return brandService.removeSpuFromBrand(id, spuId);
    }


    @Audit
    @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", required = true)
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object removeSpuFromCategory(@PathVariable Long id,
                                        @PathVariable Long spuId,
                                        @PathVariable Long shopId,
                                        @Depart @ApiIgnore Long departId,
                                        @LoginUser @ApiIgnore Long userId) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(0L) && !departId.equals(shopId))
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        return goodsCategoryService.removeSpuFromCategory(id, spuId);
    }

    @ApiOperation(value = "获得商品SKU的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping(path = "/skus/states", produces = "application/json;charset=UTF-8")
    public Object getGoodsSpuStates() {
        return StatusWrap.of(Arrays.asList(GoodsSpu.State.values()));
    }

    @ApiOperation(value = "查询sku", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "shopId", value = "商铺id"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "skuSn", value = "商品SKUsn"),
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "spuId", value = "商品SPUsn"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "spuSn", value = "商品SPUsn"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping(path = "/skus", produces = "application/json;charset=UTF-8")
    public Object getGoodsSkus(@RequestParam(required = false) Long shopId,
                               @RequestParam(required = false) String skuSn,
                               @RequestParam(required = false) Long spuId,
                               @RequestParam(required = false) String spuSn,
                               @RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        GetGoodsSkuVo getSkuVo = new GetGoodsSkuVo(shopId, skuSn, spuId, spuSn, page, pageSize);
        return goodsService.getGoodsSkus(getSkuVo);
    }

//    @DubboReference(version = "0.0.1")
//    private FootprintServiceInterface footprint;

    @ApiOperation(value = "获得sku的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @GetMapping(path = "/skus/{id}", produces = "application/json;charset=UTF-8")
    public Object getSkuDetailedById(@PathVariable Long id) {
//        footprint.addFootprint(1L, id);
        System.out.println("TEST");
        return goodsService.getSkuDetailedById(id);
    }

    @Audit
    @ApiOperation(value = "管理员添加新的SKU到SPU里")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "商店id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "spu id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CreateSkuVo", name = "vo", value = "新建需要的SKU信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在"),
            @ApiResponse(code = 901, message = "商品规格重复")
    })
    @PostMapping(path = "/shops/{shopId}/spus/{id}/skus")
    public Object createSku(@LoginUser @ApiIgnore Long userId,
                            @Depart @ApiIgnore Long departId,
                            @PathVariable Long shopId,
                            @PathVariable Long id,
                            @Validated @RequestBody CreateSkuVo vo) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.createSku(shopId, id, vo);
    }

    //TODO addImgHelper
    @PostMapping(path = "/shops/{shopId}/skus/{id}/uploadImg")
    public Object uploadSkuImg(@LoginUser @ApiIgnore Long userId,
                               @Depart @ApiIgnore Long departId,
                               @PathVariable Long shopId,
                               @PathVariable Long id,
                               @RequestParam String img) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.uploadSkuImg(shopId, id, img);
    }

    @Audit
    @ApiOperation(value = "管理员或店家逻辑删除SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "shop id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku_id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @DeleteMapping(path = "/shops/{shopId}/skus/{id}")
    public Object deleteSku(@LoginUser @ApiIgnore Long userId,
                            @Depart @ApiIgnore Long departId,
                            @PathVariable Long shopId,
                            @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.deleteSku(shopId, id);
    }

    @Audit
    @ApiOperation(value = "管理员或店家修改SKU信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "shop id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku_id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "ModifySkuVo", name = "vo", value = "可修改的SKU信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @PutMapping(path = "/shops/{shopId}/skus/{id}")
    public Object updateSku(@LoginUser @ApiIgnore Long userId,
                            @Depart @ApiIgnore Long departId,
                            @PathVariable Long shopId,
                            @PathVariable Long id,
                            @Validated @RequestBody ModifySkuVo vo) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.updateSku(shopId, id, vo);
    }

    //缺少运费模板
    //冲业绩
    @ApiOperation(value = "无需登录查看一条商品SPU的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "商品SPU ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @GetMapping(path = "/spus/{id}")
    public Object getSpuById(@PathVariable Long id) {
        return goodsService.getSpuById(id);
    }

    //TODO 查看分享商品信息,调用分享活动api，以及token登录问题
    @ApiOperation(value = "需登录查看一条分享商品SPU的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "sid", value = "分享ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "商品SkU ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @GetMapping(path = "/share/{sid}/skus/{id}")
    @Audit
    public Object getSkuBySid(
            @LoginUser Long loginId,
            @PathVariable Long sid,
            @PathVariable Long id) {
        if (loginId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        logger.debug("sid: " + sid + " id:" + id + " login user: " + loginId);
        return goodsService.getSkuBySid(sid, loginId, id);
    }

    //是否要判断Spu重复
    @Audit
    @ApiOperation(value = "店家新建商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "GoodsSpuVo", name = "vo", value = "SPU详细信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @PostMapping(path = "/shops/{id}/spus")
    public Object createSpu(@LoginUser @ApiIgnore Long userId,
                            @Depart @ApiIgnore Long departId,
                            @PathVariable Long id,
                            @Validated @RequestBody GoodsSpuVo vo) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!id.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.createSpu(id, vo);
    }

    //TODO addImgHelper
    @PostMapping(path = "/shops/{shopId}/spus/{id}/uploadImg")
    public Object uploadSpuImg(@PathVariable Long shopId,
                               @PathVariable Long id,
                               @RequestParam String img) {
        return goodsService.uploadSpuImg(shopId, id, img);
    }

    @Audit
    @ApiOperation(value = "店家修改商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "spu id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "GoodsSpuVo", name = "vo", value = "SPU详细信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @PutMapping(path = "/shops/{shopId}/spus/{id}")
    public Object updateSpu(@LoginUser @ApiIgnore Long userId,
                            @Depart @ApiIgnore Long departId,
                            @PathVariable Long shopId,
                            @PathVariable Long id,
                            @Validated @RequestBody GoodsSpuVo vo) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.updateSpu(id, vo);
    }

    @Audit
    @ApiOperation(value = "店家逻辑删除商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "spu id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @DeleteMapping(path = "/shops/{shopId}/spus/{id}")
    public Object deleteSpu(@LoginUser @ApiIgnore Long userId,
                            @Depart @ApiIgnore Long departId,
                            @PathVariable Long shopId,
                            @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.deleteSpu(shopId, id);
    }

    @Audit
    @ApiOperation(value = "店家商品上架")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @PutMapping(path = "/shops/{shopId}/skus/{id}/onshelves")
    public Object putGoodsOnSale(@LoginUser @ApiIgnore Long userId,
                                 @Depart @ApiIgnore Long departId,
                                 @PathVariable Long shopId,
                                 @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.putGoodsOnSale(shopId, id);
    }

    @Audit
    @ApiOperation(value = "店家商品下架")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @PutMapping(path = "/shops/{shopId}/skus/{id}/offshelves")
    public Object putOffGoodsOnSale(@LoginUser @ApiIgnore Long userId,
                                    @Depart @ApiIgnore Long departId,
                                    @PathVariable Long shopId,
                                    @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.putOffGoodsOnSale(shopId, id);
    }

    @Audit
    @ApiOperation(value = "管理员新增商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "FloatPricesGetVo", name = "vo", value = "可修改的信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PostMapping(path = "/shops/{shopId}/skus/{id}/floatPrices")
    public Object addFloatingPrice(@LoginUser @ApiIgnore Long userId,
                                   @Depart @ApiIgnore Long departId,
                                   @PathVariable Long shopId,
                                   @PathVariable Long id,
                                   @Validated @RequestBody FloatPricesGetVo vo) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.addFloatingPrice(shopId, userId, id, vo);
    }

    @Audit
    @ApiOperation(value = "管理员失效商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "浮动价格 id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @DeleteMapping(path = "/shops/{shopId}/floatPrices/{id}")
    public Object invalidFloatPrice(@LoginUser @ApiIgnore Long userId,
                                    @Depart @ApiIgnore Long departId,
                                    @PathVariable Long shopId,
                                    @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!shopId.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return goodsService.invalidFloatPrice(shopId, userId, id);
    }
}
