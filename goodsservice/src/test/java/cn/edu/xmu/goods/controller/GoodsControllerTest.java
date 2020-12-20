package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.util.SqlScript;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Test
    @Order(5)
    public void createSku() throws Exception {
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"configuration\": \"创建测试\",\n" +
                "  \"detail\": \"创建测试\",\n" +
                "  \"imageUrl\": \"创建测试\",\n" +
                "  \"inventory\": 0,\n" +
                "  \"name\": \"创建测试\",\n" +
                "  \"originalPrice\": 10,\n" +
                "  \"sn\": \"boxiang-0001\",\n" +
                "  \"weight\": 10\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/1/spus/800/skus")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 20682,\n" +
                "    \"name\": \"创建测试\",\n" +
                "    \"skuSn\": \"boxiang-0001\",\n" +
                "    \"imageUrl\": \"创建测试\",\n" +
                "    \"inventory\": 0,\n" +
                "    \"originalPrice\": 10,\n" +
                "    \"price\": 10,\n" +
                "    \"disabled\": false\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /** 6
     * 需登录
     * 异常路径
     * 管理员添加新的SKU到SPU里,商品规格重复
     **/
    @Test
    @Order(6)
    public void createSku2() throws Exception {
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"configuration\": \"创建测试\",\n" +
                "  \"detail\": \"创建测试\",\n" +
                "  \"imageUrl\": \"创建测试\",\n" +
                "  \"inventory\": 0,\n" +
                "  \"name\": \"创建测试\",\n" +
                "  \"originalPrice\": 10,\n" +
                "  \"sn\": \"boxiang-f0001\",\n" +
                "  \"weight\": 10\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/1/spus/800/skus")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 901,\n" +
                "  \"errmsg\": \"商品规格重复\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

//TODO 上传照片
//    @Test
//    @Order(7)
//    void uploadSkuImg() {
//    }

    /** 8
     * 需登录
     * 管理员或店家逻辑删除SKU
     **/
    @Test
    @Order(8)
    public void deleteSku() throws Exception {
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(delete("/shops/1/skus/20681")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /** 9
     * 需登录
     * 管理员或店家修改SKU信息
     **/
    @Test
    @Order(9)
    void updateSku() throws Exception {
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"configuration\": \"修改测试\",\n" +
                "  \"detail\": \"修改测试\",\n" +
                "  \"inventory\": 20,\n" +
                "  \"name\": \"修改测试\",\n" +
                "  \"originalPrice\": 20,\n" +
                "  \"weight\": 20\n" +
                "}";

        String responseString=this.mvc.perform(put("/shops/1/skus/801")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    //TODO 需要运费模板
//    @Test
//    @Order(10)
//    public void getSpuById() throws Exception {
//        String responseString=this.mvc.perform(get("/spus/800"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(responseString);
//        String expectedResponse ="";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//    }
//
    //TODO 需要分享活动模块以及运费模板
//    @Test
//    @Order(11)
//    public void getSkuBySid() throws Exception {
//        String responseString=this.mvc.perform(get("/share/1/skus/801"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(responseString);
//        String expectedResponse ="";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
//    }
//
    /** 12
     * 需登录
     * 店家新建商品SPU
     **/
    @Test
    @Order(12)
    public void createSpu() throws Exception {
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"decription\": \"创建测试\",\n" +
                "  \"name\": \"创建测试\",\n" +
                "  \"specs\": \"创建测试\"\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/1/spus")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 20684,\n" +
                "    \"name\": \"创建测试\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": null,\n" +
                "      \"name\": null,\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": {\n" +
                "      \"id\": null,\n" +
                "      \"name\": null\n" +
                "    },\n" +
                "    \"freightModelDTO\": {\n" +
                "      \"id\": null,\n" +
                "      \"name\": null,\n" +
                "      \"type\": null,\n" +
                "      \"unit\": null,\n" +
                "      \"defaultModel\": null,\n" +
                "      \"gmtCreate\": null,\n" +
                "      \"gmtModified\": null\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": null,\n" +
                "      \"name\": null\n" +
                "    },\n" +
                "    \"goodsSn\": null,\n" +
                "    \"detail\": \"创建测试\",\n" +
                "    \"imageUrl\": null,\n" +
                "    \"spec\": \"创建测试\",\n" +
                "    \"skuList\": null,\n" +
                "    \"gmtModified\": null,\n" +
                "    \"disabled\": false\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

//TODO 上传照片
//    @Test
//    @Order(13)
//    void uploadSpuImg() {
//    }
//
    /** 14
     * 需登录
     * 店家修改商品SPU
     **/
    @Test
    @Order(14)
    public void updateSpu() throws Exception {
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"decription\": \"修改测试\",\n" +
                "  \"name\": \"修改测试\",\n" +
                "  \"specs\": \"修改测试\"\n" +
                "}";

        String responseString=this.mvc.perform(put("/shops/1/spus/800")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
//
    /** 15
     * 需登录
     * 店家逻辑删除商品SPU
     **/
    @Test
    @Order(15)
    public void deleteSpu() throws Exception {
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(delete("/shops/1/spus/281")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
//
    /** 16
     * 需登录
     * 店家商品下架
     **/
    @Test
    @Order(16)
    public void putOffGoodsOnSale() throws Exception {
        String token = this.login("13088admin","123456");

        String responseString=this.mvc.perform(put("/shops/1/skus/282/offshelves")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /** 17
     * 需登录
     * 店家商品上架
     **/
    @Test
    @Order(17)
    public void putGoodsOnSale() throws Exception {
        String token = this.login("13088admin","123456");

        String responseString=this.mvc.perform(put("/shops/1/skus/282/onshelves")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
//
    /** 18
     * 需登录
     * 管理员新增商品价格浮动
     * 异常路径：浮动价格库存大于商品SKU库存
     **/
    @Test
    @Order(18)
    public void addFloatingPrice() throws Exception {
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"activityPrice\":5,\n" +
                "  \"beginTime\": \"2020-12-19T13:23:01.260Z\",\n" +
                "  \"endTime\": \"2020-12-29T13:23:01.260Z\",\n" +
                "  \"quantity\": 5\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/1/skus/282/floatPrices")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 921,\n" +
                "  \"errmsg\": \"浮动价格库存大于商品SKU库存\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /** 19
     * 需登录
     * 管理员新增商品价格浮动
     **/
    @Test
    @Order(19)
    public void addFloatingPrice2() throws Exception {
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"activityPrice\":5,\n" +
                "  \"beginTime\": \"2020-12-19T13:23:01.260Z\",\n" +
                "  \"endTime\": \"2020-12-29T13:23:01.260Z\",\n" +
                "  \"quantity\": 5\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/1/skus/801/floatPrices")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 9002,\n" +
                "    \"activityPrice\": 5,\n" +
                "    \"quantity\": 5,\n" +
                "    \"createdBy\": {\n" +
                "      \"id\": 1,\n" +
                "      \"userName\": \"13088admin\"\n" +
                "    },\n" +
                "    \"invalidBy\": {\n" +
                "      \"id\": null,\n" +
                "      \"userName\": null\n" +
                "    },\n" +
                "    \"valid\": true\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
//
    /** 20
     * 需登录
     * 管理员失效商品价格浮动
     **/
    @Test
    @Order(20)
    public void invalidFloatPrice() throws Exception {
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(delete("/shops/1/floatPrices/9001")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    private String login(String userName, String password) throws Exception{
        //String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGlzIGlzIGEgdG9rZW4iLCJhdWQiOiJNSU5JQVBQIiwidG9rZW5JZCI6IjIwMjAxMjE5MjMxNDIzMTlCIiwiaXNzIjoiT09BRCIsImRlcGFydElkIjowLCJleHAiOjE2MDgzOTQ0NjMsInVzZXJJZCI6MSwiaWF0IjoxNjA4MzkwODYzfQ.gFPnGpits9BDC_Ppf64I9JICrox14rDJTwyiKIpX410";
        String token = new JwtHelper().createToken(1L, 0L, 360000);
        return token;
    }
}
