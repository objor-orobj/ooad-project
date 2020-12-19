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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private String login(String userName, String password) throws Exception {
        String token = new JwtHelper().createToken(1L, 0L, 3600);
        return token;
    }
}