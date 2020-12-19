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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        SqlScript.run("/cn/edu/xmu/goods/controller/PresaleControllerTest.sql");
    }


    private String login(String userName, String password) throws Exception {
        String token = new JwtHelper().createToken(0L, 1L, 3600);
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
                "    \"pageSize\": 1,\n" +
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
                "    \"pageSize\": 2,\n" +
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
                "    \"pageSize\": 1,\n" +
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
                "    \"pageSize\": 1,\n" +
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
                "    \"pageSize\": 3,\n" +
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
                "    \"pageSize\": 2,\n" +
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
                "    \"pageSize\": 1,\n" +
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
