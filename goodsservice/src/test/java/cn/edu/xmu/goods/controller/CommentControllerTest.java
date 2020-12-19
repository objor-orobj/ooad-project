package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes= GoodsServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    public void getCommentState() throws Exception{
        String responseString=this.mvc.perform(get("/comments/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"code\": 0,\n" +
                "      \"name\": \"未审核\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 1,\n" +
                "      \"name\": \"评论成功\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 2,\n" +
                "      \"name\": \"未通过\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void getSkuComments1() throws Exception{
        String responseString=this.mvc.perform(get("/skus/278/comments"))
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
    void getSkuComments2() throws Exception{
        String responseString=this.mvc.perform(get("/skus/273/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 1,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"goodsSkuId\": 273,\n" +
                "        \"type\": 0,\n" +
                "        \"content\": \"good\",\n" +
                "        \"state\": 1,\n" +
                "        \"gmtCreate\": \"2020-12-18T11:53:54\",\n" +
                "        \"gmtModified\": \"2020-12-18T11:54:43\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void getSelfComments() throws Exception{
        String responseString=this.mvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 2,\n" +
                "    \"total\": 2,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"goodsSkuId\": 273,\n" +
                "        \"type\": 0,\n" +
                "        \"content\": \"good\",\n" +
                "        \"state\": 1,\n" +
                "        \"gmtCreate\": \"2020-12-18T11:53:54\",\n" +
                "        \"gmtModified\": \"2020-12-18T11:54:43\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"goodsSkuId\": 35,\n" +
                "        \"type\": 1,\n" +
                "        \"content\": \"gsg\",\n" +
                "        \"state\": 1,\n" +
                "        \"gmtCreate\": \"2020-12-18T11:56:54\",\n" +
                "        \"gmtModified\": \"2020-12-19T11:56:49\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void getSelfComments2() throws Exception{
        String responseString=this.mvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void getShopComments1() throws Exception{
        String responseString=this.mvc.perform(get("/shops/1/comments/all")
                .queryParam("state","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 2,\n" +
                "    \"total\": 2,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"goodsSkuId\": 273,\n" +
                "        \"type\": 0,\n" +
                "        \"content\": \"good\",\n" +
                "        \"state\": 1,\n" +
                "        \"gmtCreate\": \"2020-12-18T11:53:54\",\n" +
                "        \"gmtModified\": \"2020-12-18T11:54:43\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"goodsSkuId\": 35,\n" +
                "        \"type\": 1,\n" +
                "        \"content\": \"gsg\",\n" +
                "        \"state\": 1,\n" +
                "        \"gmtCreate\": \"2020-12-18T11:56:54\",\n" +
                "        \"gmtModified\": \"2020-12-19T11:56:49\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void getShopComments2() throws Exception{
        String responseString=this.mvc.perform(get("/shops/1/comments/all")
                .queryParam("state","4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 503,\n" +
                "  \"errmsg\": \"字段不合法\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void createComment1() throws Exception{
        String contentJson ="{\n" +
                "  \"content\": \"string\",\n" +
                "  \"type\": 0\n" +
                "}";
        String responseString = this.mvc.perform(post("/orderitems/454/comments")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                //"    \"id\": 12,\n" +
                "    \"customerId\": 1,\n" +
                "    \"goodsSkuId\": null,\n" +
                "    \"type\": 0,\n" +
                "    \"content\": \"string\",\n" +
                "    \"state\": 0,\n" +
                "    \"gmtModified\": null\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,false);
    }

    @Test
    void createComment2() throws Exception{
        String contentJson ="{\n" +
                "  \"content\": \"string\",\n" +
                "  \"type\": 0\n" +
                "}";
        String responseString = this.mvc.perform(post("/orderitems/4/comments")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 920,\n" +
                "  \"errmsg\": \"已评论\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }

    @Test
    void confirmComment1() throws Exception{
        String contentJson ="{\n" +
                "  \"conclusion\": true\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/comments/4/confirm")
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
    void confirmComment2() throws Exception{
        String contentJson ="{\n" +
                "  \"conclusion\": true\n" +
                "}";
        String responseString = this.mvc.perform(put("/shops/1/comments/2/confirm")
                .contentType("application/json;charset=UTF-8")
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse="{\n" +
                "  \"errno\": 919,\n" +
                "  \"errmsg\": \"评论已审核\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,responseString,true);
    }
}