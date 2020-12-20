package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.util.SqlScript;
import cn.edu.xmu.ooad.util.JwtHelper;
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

@SpringBootTest(classes = GoodsServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GrouponControllerTest {
    @Autowired
    private MockMvc mvc;

    public GrouponControllerTest() {
        SqlScript.run("/goods-schema.sql");
        SqlScript.run("/cn/edu/xmu/goods/controller/GrouponControllerTest.sql");
    }


    private String login(String userName, String password) throws Exception {
        String token = new JwtHelper().createToken(0L, 0L, 3600);
        return token;
    }

    @Test
    public void getgrouponStates() throws Exception {
        String responseString = this.mvc.perform(get("/groupons/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\"errno\": 0, \"data\": [{ \"name\": \"已下线\", \"code\": 0 },{ \"name\": \"已上线\", \"code\": 1 },{ \"name\": \"已删除\", \"code\": 2 }],\"errmsg\": \"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getgroupon() throws Exception {
        String responseString = this.mvc.perform(get("/groupons?shopId=2&spu_id=273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"儿童节\",\n" +
                "        \"beginTime\": \"2020-06-01T11:57:39\",\n" +
                "        \"endTime\": \"2020-06-02T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getgroupon1() throws Exception {
        String responseString = this.mvc.perform(get("/groupons?shopId=4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 2,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 11,\n" +
                "        \"name\": \"华岁筛筛\",\n" +
                "        \"beginTime\": \"2020-12-21T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 13,\n" +
                "        \"name\": \"华阿啊岁\",\n" +
                "        \"beginTime\": \"2020-06-01T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getgroupon2() throws Exception {
        String responseString = this.mvc.perform(get("/groupons?timeline=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"儿童节\",\n" +
                "        \"beginTime\": \"2020-06-01T11:57:39\",\n" +
                "        \"endTime\": \"2020-06-02T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getgroupon3() throws Exception {
        String responseString = this.mvc.perform(get("/groupons?timeline=4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 503,\n" +
                "  \"errmsg\": \"字段不合法\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getgroupon4() throws Exception {
        String responseString = this.mvc.perform(get("/groupons?shopId=4&timeline=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": []\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallgroupon() throws Exception {
        String token = this.login("13088admin", "123456");
        String responseString = this.mvc.perform(get("/shops/4/groupons?state=0").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 12,\n" +
                "        \"name\": \"华岁\",\n" +
                "        \"beginTime\": \"2020-12-21T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallgroupon1() throws Exception {
        String token = this.login("13088admin", "123456");
        String responseString = this.mvc.perform(get("/shops/4/groupons").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 3,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 11,\n" +
                "        \"name\": \"华岁筛筛\",\n" +
                "        \"beginTime\": \"2020-12-21T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 12,\n" +
                "        \"name\": \"华岁\",\n" +
                "        \"beginTime\": \"2020-12-21T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 13,\n" +
                "        \"name\": \"华阿啊岁\",\n" +
                "        \"beginTime\": \"2020-06-01T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallgroupon2() throws Exception {
        String token = this.login("13088admin", "123456");
        String responseString = this.mvc.perform(get("/shops/4/groupons?spu_id=188").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 2,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 11,\n" +
                "        \"name\": \"华岁筛筛\",\n" +
                "        \"beginTime\": \"2020-12-21T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 12,\n" +
                "        \"name\": \"华岁\",\n" +
                "        \"beginTime\": \"2020-12-21T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallgroupon3() throws Exception {
        String token = this.login("13088admin", "123456");
        String responseString = this.mvc.perform(get("/shops/4/groupons?spu_id=188&state=1").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 11,\n" +
                "        \"name\": \"华岁筛筛\",\n" +
                "        \"beginTime\": \"2020-12-21T11:57:39\",\n" +
                "        \"endTime\": \"2020-12-25T11:57:39\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void getallgroupon4() throws Exception {
        String token = this.login("13088admin", "123456");
        String responseString = this.mvc.perform(get("/shops/4/groupons?state=2").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"page\": 1,\n" +
                "    \"pageSize\": 10,\n" +
                "    \"total\": 0,\n" +
                "    \"pages\": 0,\n" +
                "    \"list\": []\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void creategroupon() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"strategy\": \"满减\",\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/4/spus/189/groupons")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 14,\n" +
                "    \"name\": null,\n" +
                "    \"beginTime\": \"2021-12-19T17:25:09.444\",\n" +
                "    \"endTime\": \"2022-12-19T17:25:09.444\",\n" +
                "    \"state\": 0,\n" +
                "    \"shopId\": 4,\n" +
                "    \"goodsSpuId\": 189,\n" +
                "    \"strategy\": \"满减\",\n" +
//                "    \"gmtCreate\": \"2020-12-20T01:26:29.424864\",\n" +
                "    \"gmtModified\": null\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    @Test
    public void creategroupon1() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/4/spus/189/groupons")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 503,\n" +
                "  \"errmsg\": \"字段不合法\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    @Test
    public void creategroupon3() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"strategy\": \"满减\",\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/4/spus/273/groupons")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 505,\n" +
                "  \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    @Test
    public void creategroupon4() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"strategy\": \"满减\",\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(post("/shops/4/spus/122/groupons")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 504,\n" +
                "  \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    @Test
    public void modifygroupon() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"strategy\": \"JOCK\",\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(put("/shops/1/groupons/1")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void modifygroupon1() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"strategy\": \"JOCK\",\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(put("/shops/4/groupons/1")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 505,\n" +
                "  \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void modifygroupon2() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"strategy\": \"JOCK\",\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(put("/shops/4/groupons/11")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 907,\n" +
                "  \"errmsg\": \"团购活动状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void modifygroupon3() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(put("/shops/1/groupons/1")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 503,\n" +
                "  \"errmsg\": \"字段不合法\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void modifygroupon4() throws Exception{
        String token = this.login("13088admin","123456");
        String requiredJson = "{\n" +
                "  \"beginTime\": \"2021-12-19T17:25:09.444Z\",\n" +
                "  \"endTime\": \"2022-12-19T17:25:09.444Z\"\n" +
                "}";
        String responseString=this.mvc.perform(put("/shops/1/groupons/20")
                .header("authorization",token)
                .contentType("application/json;charset=UTF-8")
                .content(requiredJson))
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
    public void ONLINE() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/1/groupons/1/onshelves")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void ONLINE1() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/1/groupons/4/onshelves")
                .header("authorization",token))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 505,\n" +
                "  \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void ONLINE2() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/4/groupons/11/onshelves")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 907,\n" +
                "  \"errmsg\": \"团购活动状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void ONLINE3() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/4/groupons/20/onshelves")
                .header("authorization",token))
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
    public void OFFLINE() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/4/groupons/11/offshelves")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void OFFLINE1() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/4/groupons/1/offshelves")
                .header("authorization",token))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 505,\n" +
                "  \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void OFFLINE2() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/4/groupons/12/offshelves")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 907,\n" +
                "  \"errmsg\": \"团购活动状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void OFFLINE3() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(put("/shops/4/groupons/20/offshelves")
                .header("authorization",token))
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
    public void DeleteGroupon() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(delete("/shops/1/groupons/1")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void DeletePresale1() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(delete("/shops/4/groupons/11")
                .header("authorization",token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 907,\n" +
                "  \"errmsg\": \"团购活动状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void DeletePresale2() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(delete("/shops/4/groupons/1")
                .header("authorization",token))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        String expectedResponse = "{\n" +
                "  \"errno\": 505,\n" +
                "  \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
    @Test
    public void DeletePresale3() throws Exception{
        String token = this.login("13088admin","123456");
        String responseString=this.mvc.perform(delete("/shops/1/groupons/20")
                .header("authorization",token))
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
