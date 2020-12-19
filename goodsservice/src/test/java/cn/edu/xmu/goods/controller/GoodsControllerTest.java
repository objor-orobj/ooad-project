package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.util.SqlScript;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GoodsServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GoodsControllerTest {
    @Autowired
    private MockMvc mvc;

    public GoodsControllerTest() {
        SqlScript.run("/goods-schema.sql");
        SqlScript.run("/cn/edu/xmu/goods/controller/GoodsControllerTest.sql");
    }

    /**
     * 1
     * 无需登录
     * 获得商品SKU的所有状态
     **/
    @Test
    @Order(1)
    public void getGoodsSpuStates() throws Exception {
        String responseString = this.mvc.perform(get("/skus/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"code\": 0,\n" +
                "      \"name\": \"未上架\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 4,\n" +
                "      \"name\": \"上架\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 6,\n" +
                "      \"name\": \"已删除\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 2
     * 无需登录
     * 查询sku
     * 四个参数均不为空
     **/
    @Test
    @Order(2)
    public void getGoodsSkus1() throws Exception {
        String responseString = this.mvc.perform(get("/skus?shopId=1&skuSn=boxiang-f0001&spuId=800&spuSn=boxiang-f0001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\",\n" +
                "    \"data\": {\n" +
                "        \"page\": 1,\n" +
                "        \"pageSize\": 1,\n" +
                "        \"total\": 1,\n" +
                "        \"pages\": 1,\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"id\": 801,\n" +
                "                \"name\": \"1-13测试\",\n" +
                "                \"skuSn\": \"boxiang-f0001\",\n" +
                "                \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "                \"inventory\": 66,\n" +
                "                \"originalPrice\": 66,\n" +
                "                \"price\": 66,\n" +
                "                \"disabled\": false\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 3
     * 无需登录
     * 查询sku
     * shopId与spuSn为空,另外两个不为空
     **/
    @Test
    @Order(3)
    public void getGoodsSkus2() throws Exception {
        String responseString = this.mvc.perform(get("/skus?shopId=&skuSn=boxiang-f0001&spuId=800&spuSn="))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\",\n" +
                "    \"data\": {\n" +
                "        \"page\": 1,\n" +
                "        \"pageSize\": 1,\n" +
                "        \"total\": 1,\n" +
                "        \"pages\": 1,\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"id\": 801,\n" +
                "                \"name\": \"1-13测试\",\n" +
                "                \"skuSn\": \"boxiang-f0001\",\n" +
                "                \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "                \"inventory\": 66,\n" +
                "                \"originalPrice\": 66,\n" +
                "                \"price\": 66,\n" +
                "                \"disabled\": false\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /** 4
     * 无需登录
     * 获得sku的详细信息
     **/
    //TODO 需要运费模板以及足迹服务
//    @Test
//    @Order(4)
//    public void getSkuDetailedById() throws Exception {
//        String responseString=this.mvc.perform(get("/skus/800"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(responseString);
//        String expectedResponse ="";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//    }

    /**
     * 5
     * 需登录
     * 管理员添加新的SKU到SPU里
     **/
//    @Test
//    public void createSku() throws Exception {
//        ResultActions resultActions=null;
//        resultActions=
//        String responseString=this.mvc.perform(post("/shops/1/spus/800/skus"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(responseString);
//        String expectedResponse ="";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//    }
//
//    @Test
//    void uploadSkuImg() {
//    }
//
//    @Test
//    void deleteSku() {
//    }
//
//    @Test
//    void updateSku() {
//    }
//
//    @Test
//    void getSpuById() {
//    }
//
//    @Test
//    void getSkuBySid() {
//    }
//
//    @Test
//    void createSpu() {
//    }
//
//    @Test
//    void uploadSpuImg() {
//    }
//
//    @Test
//    void updateSpu() {
//    }
//
//    @Test
//    void deleteSpu() {
//    }
//
//    @Test
//    void putGoodsOnSale() {
//    }
//
//    @Test
//    void putOffGoodsOnSale() {
//    }
//
//    @Test
//    void addFloatingPrice() {
//    }
//
//    @Test
//    void invalidFloatPrice() {
//    }
    
    @Test
    void createBrand1() throws Exception{
        String contentJson ="{\n" +
                "  \"detail\": \"string\",\n" +
                "  \"name\": \"brand2\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/shops/1/brands")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                // "    \"id\": 121,\n" +
                "    \"name\": \"brand2\",\n" +
                "    \"detail\": \"string\",\n" +
                "    \"imageUrl\": null,\n" +
                //"    \"gmtCreate\": \"2020-12-18T16:22:26.3093649\",\n" +
                "    \"gmtModified\": null\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,false);
    }

    @Test
    void createBrand2() throws Exception{
        String contentJson ="{\n" +
                "  \"detail\": \"string\",\n" +
                "  \"name\": \"戴荣华\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/shops/1/brands")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 926,\n" +
                "  \"errmsg\": \"品牌名重复\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void createGoodsCategory1() throws Exception{
        String contentJson ="{\n" +
                "  \"name\": \"brand2\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/shops/1/categories/0/subcategories")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                //"    \"id\": 145,\n" +
                "    \"pid\": 0,\n" +
                "    \"name\": \"brand2\",\n" +
                //"    \"gmtCreate\": \"2020-12-18T17:14:09.3190436\",\n" +
                "    \"gmtModified\": null\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,false);
    }

    @Test
    void createGoodsCategory2() throws Exception{
        String contentJson ="{\n" +
                "  \"name\": \"艺术品\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/shops/1/categories/0/subcategories")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 927,\n" +
                "  \"errmsg\": \"分类名重复\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void createGoodsCategory3() throws Exception{
        String contentJson ="{\n" +
                "  \"name\": \"brand1\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/shops/1/categories/2/subcategories")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 928,\n" +
                "  \"errmsg\": \"父分类不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void uploadBrandImage() {

    }

    @Test
    void getAllBrands() throws Exception{
        String responseString=this.mvc.perform(get("/brands").queryParam("page","1").queryParam("pageSize","5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 5,\n" +
                "    \"total\": 50,\n" +
                "    \"pages\": 10,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 71,\n" +
                "        \"name\": \"戴荣华\",\n" +
                "        \"detail\": null,\n" +
                "        \"imageUrl\": null,\n" +
                "        \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "        \"gmtModified\": \"2020-12-10T22:36:01\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 72,\n" +
                "        \"name\": \"范敏祺\",\n" +
                "        \"detail\": null,\n" +
                "        \"imageUrl\": null,\n" +
                "        \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "        \"gmtModified\": \"2020-12-10T22:36:01\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 73,\n" +
                "        \"name\": \"黄卖九\",\n" +
                "        \"detail\": null,\n" +
                "        \"imageUrl\": null,\n" +
                "        \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "        \"gmtModified\": \"2020-12-10T22:36:01\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 74,\n" +
                "        \"name\": \"李进\",\n" +
                "        \"detail\": null,\n" +
                "        \"imageUrl\": null,\n" +
                "        \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "        \"gmtModified\": \"2020-12-10T22:36:01\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 75,\n" +
                "        \"name\": \"李菊生\",\n" +
                "        \"detail\": null,\n" +
                "        \"imageUrl\": null,\n" +
                "        \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "        \"gmtModified\": \"2020-12-10T22:36:01\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void getGoodsCategory1() throws Exception{
        String responseString=this.mvc.perform(get("/categories/122/subcategories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 123,\n" +
                "      \"pid\": 122,\n" +
                "      \"name\": \"大师原作\",\n" +
                "      \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "      \"gmtModified\": \"2020-12-10T22:36:01\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 124,\n" +
                "      \"pid\": 122,\n" +
                "      \"name\": \"艺术衍生品\",\n" +
                "      \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "      \"gmtModified\": \"2020-12-10T22:36:01\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void getGoodsCategory2() throws Exception{
        String responseString=this.mvc.perform(get("/categories/139/subcategories"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void modifyBrand1() throws Exception{
        String contentJson ="{\n" +
                "  \"detail\": \"string\",\n" +
                "  \"name\": \"string\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/brands/128")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void modifyBrand2() throws Exception{
        String contentJson ="{\n" +
                "  \"detail\": \"string\",\n" +
                "  \"name\": \"aaa\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/brands/128")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 926,\n" +
                "  \"errmsg\": \"品牌名重复\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void modifyBrand3() throws Exception{
        String contentJson ="{\n" +
                "  \"detail\": \"string\",\n" +
                "  \"name\": \"aaa\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/brands/130")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }
    
    @Test
    void modifyGoodsCategory1() throws Exception{
        String contentJson ="{\n" +
                "  \"name\": \"aa\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/categories/145")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void modifyGoodsCategory2() throws Exception{
        String contentJson ="{\n" +
                "  \"name\": \"艺术品\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/categories/128")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 929,\n" +
                "  \"errmsg\": \"分类名重复\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void modifyGoodsCategory3() throws Exception{
        String contentJson ="{\n" +
                "  \"name\": \"艺术\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/categories/1")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void deleteBrand1() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/brands/128"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void deleteBrand2() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/brands/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void deleteGoodsCategory1() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/categories/145"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void deleteGoodsCategory2() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void addSpuToBrand1() throws Exception{
        String responseString = this.mvc.perform(post("/shops/1/spus/680/brands/103"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void addSpuToBrand2() throws Exception{
        String responseString = this.mvc.perform(post("/shops/1/spus/699/brands/102"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void addSpuToBrand3() throws Exception{
        String responseString = this.mvc.perform(post("/shops/1/spus/663/brands/105"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 930,\n" +
                "  \"errmsg\": \"已加入品牌\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }
    
    @Test
    void addSpuToCategory1() throws Exception{
        String responseString = this.mvc.perform(post("/shops/1/spus/680/categories/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void addSpuToCategory2() throws Exception{
        String responseString = this.mvc.perform(post("/shops/1/spus/680/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void addSpuToCategory3() throws Exception{
        String responseString = this.mvc.perform(post("/shops/1/spus/680/categories/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void removeSpuFromBrand1() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/spus/680/brands/102"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void removeSpuFromBrand2() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/spus/680/brands/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void removeSpuFromBrand3() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/spus/680/brands/103"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 932,\n" +
                "  \"errmsg\": \"未加入品牌\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void removeSpuFromCategory1() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/spus/680/categories/122"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void removeSpuFromCategory2() throws Exception{
        String responseString = this.mvc.perform(delete("/shops/1/spus/680/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void removeSpuFromCategory3() throws Exception {
        String responseString = this.mvc.perform(delete("/shops/1/spus/680/categories/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\n" +
                "  \"errno\": 933,\n" +
                "  \"errmsg\": \"未加入分类\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    
    private String login(String userName, String password) throws Exception {
        String token = new JwtHelper().createToken(1L, 0L, 3600);
        return token;
    }
}
