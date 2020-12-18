package cn.edu.xmu.goods.controller;


import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.*;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @ApiOperation(value="管理员新增品牌")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            //@ApiImplicitParam(paramType="path",dataType="int",name="id",value="shop id",required=true),
            @ApiImplicitParam(paramType="body",dataType = "BrandVo",name="vo",value="可修改的品牌信息",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PostMapping("/shops/{id}/brands")
    public Object createBrand(@Validated @RequestBody BrandVo vo
                              //@LoginUser Long userId,
                              //@PathVariable Long id
    ){
        return brandService.createBrand(vo);
    }

    @ApiOperation(value="管理员新增商品类目")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="goodsCategory id",required=true),
            //@ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="body",dataType = "GoodsCategoryVo",name="vo",value="类目详细信息",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PostMapping("/shops/{shopId}/categories/{id}/subcategories")
    public  Object createGoodsCategory(@Validated @RequestBody GoodsCategoryVo vo,
                                       //@LoginUser Long userId,
                                       @PathVariable Long id
                                       //PathVariable Long shopId
    ){
        return goodsCategoryService.createGoodsCategory(id,vo);
    }

    @ApiOperation(value="上传品牌图片")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            //@ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="id",required=true),
            @ApiImplicitParam(paramType="body",dataType = "String",name="img",value="imageUrl",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PostMapping("/shops/{shopId}/brands/{id}/uploadImg")
    public Object uploadBrandImage( //@LoginUser Long userId,
                                    @PathVariable Long id,
                                    //@PathVariable Long shopId,
                                    @RequestParam String imageUrl){
        return brandService.uploadBrandImage(id,imageUrl);
    }

    @ApiOperation(value="查看所有品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping("/brands")
    public Object getAllBrands(@RequestParam(required=false,defaultValue="1")Integer page,
                               @RequestParam(required=false,defaultValue="10")Integer pageSize){
        return brandService.getAllBrands(page,pageSize);
    }

    @ApiOperation(value="查询商品分类关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="id",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping("/categories/{id}/subcategories")
    public Object getGoodsCategory(@PathVariable Long id){
        return goodsCategoryService.getGoodsCategory(id);
    }

    @ApiOperation(value="管理员修改品牌")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            //@ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="id",required=true),
            @ApiImplicitParam(paramType="body",dataType = "BrandVo",name="vo",value="可修改的品牌信息",required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping("/shops/{shopId}/brands/{id}")
    public Object modifyBrand(@PathVariable Long id,
                              //@PathVariable Long shopId,
                              @RequestBody BrandVo vo){
        return brandService.modifyBrand(id,vo);
    }

    @ApiOperation(value="管理员修改商品类目信息")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            //@ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="id",required=true),
            @ApiImplicitParam(paramType="body",dataType = "GoodsCategoryVo",name="vo",value="商品类目详细信息",required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PutMapping("/shops/{shopId}/categories/{id}")
    public Object modifyGoodsCategory(@PathVariable Long id,
                                      //@PathVariable Long shopId,
                                      @RequestBody GoodsCategoryVo vo){
        return goodsCategoryService.modifyGoodsCategory(id,vo);
    }

    @ApiOperation(value="管理员删除品牌")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="id",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @DeleteMapping("/shops/{shopId}/brands/{id}")
    public Object deleteBrand(@PathVariable Long id,
                              @PathVariable Long shopId){
        return brandService.deleteBrand(id);
    }

    @ApiOperation(value="管理员删除商品类目信息")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="id",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @DeleteMapping("/shops/{shopId}/categories/{id}")
    public Object deleteGoodsCategory(@PathVariable Long id,
                                      @PathVariable Long shopId){
        return goodsCategoryService.deleteGoodsCategory(id);
    }

    @ApiOperation(value="将SPU加入品牌")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="spuId",value="SPU id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="brand id",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PostMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object addSpuToBrand(@PathVariable Long id,
                                @PathVariable Long spuId,
                                @PathVariable Long shopId){
        return brandService.addSpuToBrand(id,spuId,shopId);
    }

    @ApiOperation(value="将SPU加入分类")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="spuId",value="SPU id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="category id",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @PostMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object addSpuToCategory(@PathVariable Long id,
                                   @PathVariable Long spuId,
                                   @PathVariable Long shopId){
        return goodsCategoryService.addSpuToCategory(id,spuId,shopId);
    }

    @ApiOperation(value="将SPU移除品牌")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="spuId",value="SPU id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="brand id",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object removeSpuFromBrand(@PathVariable Long id,
                                     @PathVariable Long spuId,
                                     @PathVariable Long shopId){
        return brandService.removeSpuFromBrand(id,spuId);
    }

    @ApiOperation(value = "将SPU移出分类")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path",dataType="int",name="shopId",value="shop id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="spuId",value="SPU id",required=true),
            @ApiImplicitParam(paramType="path",dataType="int",name="id",value="category id",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object removeSpuFromCategory(@PathVariable Long id,
                                        @PathVariable Long spuId,
                                        @PathVariable Long shopId){
        return goodsCategoryService.removeSpuFromCategory(id,spuId);
    }

    @ApiOperation(value="获得商品SKU的所有状态")
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/skus/states")
    public Object getGoodsSpuStates() {
        return StatusWrap.of(Arrays.asList(GoodsSpu.State.values()));
    }

    @ApiOperation(value="查询sku")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "shopId", value = "商铺id"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "skuSn", value = "商品SKUsn"),
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "spuId", value = "商品SPUsn"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "spuSn", value = "商品SPUsn"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目"),
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
    })
    @GetMapping(path = "/skus")
    public Object getGoodsSkus(@RequestParam(required = false) Long shopId,
                               @RequestParam(required = false) String skuSn,
                               @RequestParam(required = false) Long spuId,
                               @RequestParam(required = false) String spuSn,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer pageSize) {
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        GetGoodsSkuVo getSkuVo = new GetGoodsSkuVo(shopId, skuSn, spuId, spuSn, page, pageSize);
        return goodsService.getGoodsSkus(getSkuVo);
    }

    //TODO 缺少运费模板
    @ApiOperation(value="获得sku的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id")
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
    })
    @GetMapping(path = "/skus/{id}")
    public Object getSkuDetailedById(@PathVariable Long id) {
        return goodsService.getSkuDetailedById(id);
    }

    @Audit
    @ApiOperation(value="管理员添加新的SKU到SPU里")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "商店id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "spu id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CreateSkuVo", name = "vo", value = "新建需要的SKU信息")
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在"),
            @ApiResponse(code=901,message="商品规格重复")
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
    @ApiOperation(value="管理员或店家逻辑删除SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "shop id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku_id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
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
        return goodsService.deleteSku(id);
    }

    @Audit
    @ApiOperation(value="管理员或店家修改SKU信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "shop id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku_id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "ModifySkuVo", name = "vo", value = "可修改的SKU信息")
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
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
        return goodsService.updateSku(id, vo);
    }

    //缺少运费模板
    @ApiOperation(value="查看一条商品SPU的详细信息（无需登录）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "商品SPU ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
    })
    @GetMapping(path = "/spus/{id}")
    public Object getSpuById(@PathVariable Long id) {
        return goodsService.getSpuById(id);
    }

    //TODO 查看分享商品信息,调用分享活动api
    @Audit
    @ApiOperation(value="查看一条分享商品SPU的详细信息（需登录）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "sid", value = "分享ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "商品SkU ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
    })
    @GetMapping(path = "/share/{sid}/skus/{id}")
    public Object getSkuBySid(@LoginUser @ApiIgnore Long userId,
                              @Depart @ApiIgnore Long departId,
                              @PathVariable Long sid,
                              @PathVariable Long id) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return goodsService.getSkuBySid(sid,id);
    }

    //是否要判断Spu重复
    @Audit
    @ApiOperation(value="店家新建商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "GoodsSpuVo", name = "vo", value = "SPU详细信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
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
    @ApiOperation(value="店家修改商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "spu id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "GoodsSpuVo", name = "vo", value = "SPU详细信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
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
    @ApiOperation(value="店家逻辑删除商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "spu id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
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
    @ApiOperation(value="店家商品上架")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
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
        return goodsService.putGoodsOnSale( id);
    }

    @Audit
    @ApiOperation(value="店家商品下架")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功"),
            @ApiResponse(code=504,message="操作的资源id不存在")
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
        return goodsService.putOffGoodsOnSale( id);
    }

    @Audit
    @ApiOperation(value="管理员新增商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "sku id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "FloatPricesGetVo", name = "vo", value = "可修改的信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
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
        return goodsService.addFloatingPrice(userId,id, vo);
    }

    @Audit
    @ApiOperation(value="管理员失效商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "浮动价格 id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code=0,message="成功")
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
        return goodsService.invalidFloatPrice(userId,id);
    }
}
