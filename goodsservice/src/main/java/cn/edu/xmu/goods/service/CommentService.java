package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CommentDao;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.vo.CommentConfirmVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-07 19:53
 */
@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public ResponseEntity<StatusWrap> getSkuComments(Long skuId, Integer page, Integer pageSize){
        return commentDao.getSkuComments(skuId,page,pageSize);
    }

    public ResponseEntity<StatusWrap> getSelfComments(Long id, Integer page, Integer pageSize){
        return commentDao.getSelfComments(id,page,pageSize);
    }

    public ResponseEntity<StatusWrap> getShopComments(Integer state, Integer page, Integer pageSize){
        return commentDao.getShopComments(state,page,pageSize);
    }

    public Object createComment(Long customerId,Long id,CommentVo vo) {return commentDao.createComment(customerId,id,vo);}

    public Object confirmComment(Long commentId, CommentConfirmVo vo){
            return commentDao.confirmComment(commentId,vo);
    }

}
