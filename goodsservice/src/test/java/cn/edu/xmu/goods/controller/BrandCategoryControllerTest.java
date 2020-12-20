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

@SpringBootTest(classes = GoodsServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BrandCategoryControllerTest {
    @Autowired
    private MockMvc mvc;

    /*public BrandCategoryControllerTest() {
        SqlScript.run("/goods-schema.sql");
        SqlScript.run("/cn/edu/xmu/goods/controller/BrandCategoryControllerTest.sql");
    }*/

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
}