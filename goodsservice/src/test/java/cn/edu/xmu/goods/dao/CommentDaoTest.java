package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.model.StatusWrap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes= GoodsServiceApplication.class)
@Transactional
class CommentDaoTest {

    @Autowired CommentDao commentDao;

    @Test
    void getSkuComments1() {
        Long skuId=273L;
        Integer page=1;
        Integer pageSize=10;
        ResponseEntity<StatusWrap> commentInfo=commentDao.getSkuComments(skuId,page,pageSize);
        assertEquals(200,commentInfo.getStatusCodeValue());
    }

    @Test
    void getSkuComments2() {
        Long skuId=2L;
        Integer page=1;
        Integer pageSize=10;
        ResponseEntity<StatusWrap> commentInfo=commentDao.getSkuComments(skuId,page,pageSize);
        assertEquals(504,commentInfo.getStatusCodeValue());
    }

    @Test
    void getSelfComments() {
    }

    @Test
    void getShopComments() {
    }

    @Test
    void createComment() {
    }

    @Test
    void confirmComment() {
    }
}