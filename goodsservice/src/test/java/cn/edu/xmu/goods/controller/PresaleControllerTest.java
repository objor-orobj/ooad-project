package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.service.PresaleService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GoodsServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PresaleControllerTest
{
    @Autowired
    private MockMvc mvc;

/*
    public void createPresale() throws Exception{
        String token = null;

        String contentJson = "{\n" +
                "  \"name\": \"TESTActivity\",\n" +
                "  \"advancePayPrice\": 250,\n" +
                "  \"restPayPrice\": 350,\n" +
                "  \"quantity\": 100,\n" +
                "  \"beginTime\": \"2020-12-12T05:16:59\",\n" +
                "  \"payTime\": \"2021-12-12T05:16:59\",\n" +
                "  \"endTime\": \"2021-06-12T05:16:59\"\n" +
                "}";

        String responseString = this.mvc.perform(
                put("/shops/1/skus/477/presales")
                        .header("authorization", token)
                        .contentType("application/json;charset=UTF-8").content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"TESTActivity\",\n" +
                "    \"BeginTime\": \"2020-12-12T05:16:59\",\n" +
                "    \"payTime\": \"2021-12-12T05:16:59\",\n" +
                "    \"endTime\": \"2021-06-12T05:16:59\",\n" +
                "    \"goodsSku\": {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\",\n" +
                "      \"skuSn\": \"string\",\n" +
                "      \"imageUrl\": \"string\",\n" +
                "      \"inventory\": 0,\n" +
                "      \"originalPrice\": 0,\n" +
                "      \"price\": 0,\n" +
                "      \"disable\": false\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    },\n" +
                "    \"state\": \"string\",\n" +
                "    \"quantity\": 0,\n" +
                "    \"advancePayPrice\": 0,\n" +
                "    \"restPayPrice\": 0,\n" +
                "    \"gmtCreate\": \"string\",\n" +
                "    \"gmtModified\": \"string\"\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

        @Test
    public void getPresaleGoodsNot() throws Exception{
        String responseString = this.mvc.perform(
                put("/presales")
                        .queryParam("skuId","99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedResponse = "{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void createPresaleGoods() throws Exception{
        String token = null;

        String contentJson = "{\n" +
                "  \"name\": \"TESTActivity\",\n" +
                "  \"advancePayPrice\": 250,\n" +
                "  \"restPayPrice\": 350,\n" +
                "  \"quantity\": 100,\n" +
                "  \"beginTime\": \"2021-12-12T05:16:59\",\n" +
                "  \"payTime\": \"2022-12-12T05:16:59\",\n" +
                "  \"endTime\": \"2022-06-12T05:16:59\"\n" +
                "}";

        String responseString = this.mvc.perform(
                put("/shops/1/skus/499/presales")
                        .header("authorization", token)
                        .contentType("application/json;charset=UTF-8").content(contentJson))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedResponse = "{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
 */

    @Test
    public void getpresaleStates() throws Exception{
        String responseString=this.mvc.perform(get("/presales/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\"errno\": 0, \"data\": [{ \"name\": \"已下线\", \"code\": 0 },{ \"name\": \"已上线\", \"code\": 1 },{ \"name\": \"已删除\", \"code\": 2 }],\"errmsg\": \"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

}
