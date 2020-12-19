package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.service.PresaleService;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.assertj.core.internal.bytebuddy.asm.Advice;
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
    private String login(String userName, String password) throws Exception
    {
        String token = new JwtHelper().createToken(0L, 1L, 3600);
        return token;
    }

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
    @Test
    public void getpresales1() throws Exception{
        String responseString=this.mvc.perform(get("/presales?shopId=2&skuId=273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 1,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"七夕节\",\n" +
                "        \"beginTime\": \"2020-06-01T11:57:39\",\n" +
                "        \"payTime\": null,\n" +
                "        \"endTime\": \"2020-06-02T11:57:39\",\n" +
                "        \"state\": \"1\",\n" +
                "        \"shop\": {\n" +
                "          \"id\": 2,\n" +
                "          \"name\": \"Adidas\"\n" +
                "        },\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 273,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 980000,\n" +
                "          \"price\": 980000,\n" +
                "          \"disabled\": false\n" +
                "        },\n" +
                "        \"quantity\": 0,\n" +
                "        \"advancePayPrice\": 0,\n" +
                "        \"restPayPrice\": 0,\n" +
                "        \"gmtCreate\": \"2020-06-01T11:57:39\",\n" +
                "        \"gmtModified\": \"2020-06-01T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getpresales2() throws Exception{
        String responseString=this.mvc.perform(get("/presales?shopId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 3,\n" +
                "    \"total\": 3,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 3100,\n" +
                "        \"name\": null,\n" +
                "        \"beginTime\": \"2021-01-04T00:39:21\",\n" +
                "        \"payTime\": \"2023-08-02T00:39:16\",\n" +
                "        \"endTime\": \"2021-01-30T00:39:25\",\n" +
                "        \"state\": \"1\",\n" +
                "        \"shop\": {\n" +
                "          \"id\": 1,\n" +
                "          \"name\": \"Nike\"\n" +
                "        },\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 3311,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5862230d20162.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 3344,\n" +
                "          \"price\": 3344,\n" +
                "          \"disabled\": false\n" +
                "        },\n" +
                "        \"quantity\": 43,\n" +
                "        \"advancePayPrice\": 3,\n" +
                "        \"restPayPrice\": 432,\n" +
                "        \"gmtCreate\": \"2020-12-16T00:39:46\",\n" +
                "        \"gmtModified\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3105,\n" +
                "        \"name\": null,\n" +
                "        \"beginTime\": \"2025-12-14T00:43:58\",\n" +
                "        \"payTime\": \"2025-12-10T00:43:55\",\n" +
                "        \"endTime\": \"2020-12-26T00:43:51\",\n" +
                "        \"state\": \"1\",\n" +
                "        \"shop\": {\n" +
                "          \"id\": 1,\n" +
                "          \"name\": \"Nike\"\n" +
                "        },\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 3311,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5862230d20162.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 3344,\n" +
                "          \"price\": 3344,\n" +
                "          \"disabled\": false\n" +
                "        },\n" +
                "        \"quantity\": null,\n" +
                "        \"advancePayPrice\": 7,\n" +
                "        \"restPayPrice\": 867,\n" +
                "        \"gmtCreate\": \"2020-12-16T00:44:02\",\n" +
                "        \"gmtModified\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3107,\n" +
                "        \"name\": null,\n" +
                "        \"beginTime\": \"2027-12-17T00:45:21\",\n" +
                "        \"payTime\": \"2027-12-08T00:45:16\",\n" +
                "        \"endTime\": \"2020-12-26T00:45:12\",\n" +
                "        \"state\": \"1\",\n" +
                "        \"shop\": {\n" +
                "          \"id\": 1,\n" +
                "          \"name\": \"Nike\"\n" +
                "        },\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 3311,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5862230d20162.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 3344,\n" +
                "          \"price\": 3344,\n" +
                "          \"disabled\": false\n" +
                "        },\n" +
                "        \"quantity\": 876,\n" +
                "        \"advancePayPrice\": 5,\n" +
                "        \"restPayPrice\": 876,\n" +
                "        \"gmtCreate\": \"2020-12-16T00:45:24\",\n" +
                "        \"gmtModified\": null\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getpresales3() throws Exception{
        String responseString=this.mvc.perform(get("/presales?shopId=2&timeline=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 1,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"七夕节\",\n" +
                "        \"beginTime\": \"2020-06-01T11:57:39\",\n" +
                "        \"payTime\": null,\n" +
                "        \"endTime\": \"2020-06-02T11:57:39\",\n" +
                "        \"state\": \"1\",\n" +
                "        \"shop\": {\n" +
                "          \"id\": 2,\n" +
                "          \"name\": \"Adidas\"\n" +
                "        },\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 273,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 980000,\n" +
                "          \"price\": 980000,\n" +
                "          \"disabled\": false\n" +
                "        },\n" +
                "        \"quantity\": 0,\n" +
                "        \"advancePayPrice\": 0,\n" +
                "        \"restPayPrice\": 0,\n" +
                "        \"gmtCreate\": \"2020-06-01T11:57:39\",\n" +
                "        \"gmtModified\": \"2020-06-01T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getpresales4() throws Exception{
        String responseString=this.mvc.perform(get("/presales?skuId=274"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallpresales() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(get("/shops/1/presales?skuId=273&state=1").header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 1,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"七夕节\",\n" +
                "        \"beginTime\": \"2020-06-01T11:57:39\",\n" +
                "        \"payTime\": null,\n" +
                "        \"endTime\": \"2020-06-02T11:57:39\",\n" +
                "        \"state\": \"1\",\n" +
                "        \"shop\": {\n" +
                "          \"id\": 2,\n" +
                "          \"name\": \"Adidas\"\n" +
                "        },\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 273,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 980000,\n" +
                "          \"price\": 980000,\n" +
                "          \"disabled\": false\n" +
                "        },\n" +
                "        \"quantity\": 0,\n" +
                "        \"advancePayPrice\": 0,\n" +
                "        \"restPayPrice\": 0,\n" +
                "        \"gmtCreate\": \"2020-06-01T11:57:39\",\n" +
                "        \"gmtModified\": \"2020-06-01T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallpresales1() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(get("/shops/2/presales?state=0").header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 1,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"双十二\",\n" +
                "        \"beginTime\": \"2020-12-29T11:57:39\",\n" +
                "        \"payTime\": null,\n" +
                "        \"endTime\": \"2020-12-30T11:57:39\",\n" +
                "        \"state\": \"0\",\n" +
                "        \"shop\": {\n" +
                "          \"id\": 2,\n" +
                "          \"name\": \"Adidas\"\n" +
                "        },\n" +
                "        \"goodsSku\": {\n" +
                "          \"id\": 273,\n" +
                "          \"name\": \"+\",\n" +
                "          \"skuSn\": null,\n" +
                "          \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "          \"inventory\": 1,\n" +
                "          \"originalPrice\": 980000,\n" +
                "          \"price\": 980000,\n" +
                "          \"disabled\": false\n" +
                "        },\n" +
                "        \"quantity\": 0,\n" +
                "        \"advancePayPrice\": 0,\n" +
                "        \"restPayPrice\": 0,\n" +
                "        \"gmtCreate\": \"2020-12-09T11:57:39\",\n" +
                "        \"gmtModified\": \"2020-12-09T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallpresales2() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(get("/shops/3/presales").header("authorization",token))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void createpresale() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(get("/shops/3/presales").header("authorization",token))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
