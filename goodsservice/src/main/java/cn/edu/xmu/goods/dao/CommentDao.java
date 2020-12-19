package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CommentPoMapper;
import cn.edu.xmu.goods.model.PageWrap;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.CommentPo;
import cn.edu.xmu.goods.model.po.CommentPoExample;
import cn.edu.xmu.goods.model.vo.CommentConfirmVo;
import cn.edu.xmu.goods.model.vo.CommentRetVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.order.service.OrderServiceInterface;
import cn.edu.xmu.other.model.dto.CustomerDTO;
import cn.edu.xmu.other.service.UserServiceInterface;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-07 19:54
 */
@Repository
public class CommentDao {
    @Autowired
    private CommentPoMapper commentMapper;

    @DubboReference(version = "0.0.1")
    private UserServiceInterface userService;

    @DubboReference(version = "0.0.1")
    private OrderServiceInterface orderService;

    public ResponseEntity<StatusWrap> getSkuComments(Long skuId, Integer page, Integer pageSize){
        List<CommentPo> com = null;
        CommentPoExample example=new CommentPoExample();
        CommentPoExample.Criteria criteria=example.createCriteria();
        Comment.State state= Comment.State.SUCCESS;
        PageHelper.startPage(page, pageSize);
        criteria.andGoodsSkuIdEqualTo(skuId);
        criteria.andStateEqualTo(state.getCode().byteValue());
        com=commentMapper.selectByExample(example);
        if(com.size()==0){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<CommentRetVo> commentRet=new ArrayList<>(com.size());
        for(CommentPo po:com){
            CustomerDTO customer=userService.getCustomerInfoById(po.getCustomerId());
            CommentRetVo comment=new CommentRetVo(po,customer);
            commentRet.add(comment);
        }
        PageInfo<CommentPo> raw = PageInfo.of(com);
        return StatusWrap.of(PageWrap.of(raw,commentRet));
    }

    public ResponseEntity<StatusWrap> getSelfComments(Long customerId,Integer page,Integer pageSize){
        List<CommentPo> com = null;
        CommentPoExample example=new CommentPoExample();
        CommentPoExample.Criteria criteria=example.createCriteria();
        PageHelper.startPage(page, pageSize);
        criteria.andCustomerIdEqualTo(customerId);
        com=commentMapper.selectByExample(example);
        if(com==null||com.isEmpty()){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<CommentRetVo> commentRet=new ArrayList<>(com.size());
        for(CommentPo po:com){
            CustomerDTO customer=userService.getCustomerInfoById(po.getCustomerId());
            CommentRetVo comment=new CommentRetVo(po,customer);
            commentRet.add(comment);
        }
        PageInfo<CommentPo> raw = PageInfo.of(com);
        return StatusWrap.of(PageWrap.of(raw,commentRet));
    }

    public ResponseEntity<StatusWrap> getShopComments(Integer state,Integer page,Integer pageSize){
        List<CommentPo> com = null;
        CommentPoExample example=new CommentPoExample();
        CommentPoExample.Criteria criteria=example.createCriteria();
        PageHelper.startPage(page, pageSize);
        if(state==null)
            criteria.andIdIsNotNull();
        else{
            Byte s;
            switch(state){
                case 0:s=0;break;
                case 1:s=1;break;
                case 2:s=2;break;
                default:return StatusWrap.just(Status.FIELD_NOTVALID);
            }
            criteria.andStateEqualTo(s);
        }
        com=commentMapper.selectByExample(example);
        if(com==null||com.isEmpty()){
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        List<CommentRetVo> commentRet=new ArrayList<>(com.size());
        for(CommentPo po:com){
            CustomerDTO customer=userService.getCustomerInfoById(po.getCustomerId());
            CommentRetVo comment=new CommentRetVo(po,customer);
            commentRet.add(comment);
        }
        PageInfo<CommentPo> raw = PageInfo.of(com);
        return StatusWrap.of(PageWrap.of(raw,commentRet));
    }

    public ResponseEntity<StatusWrap> createComment(Long customerId,Long orderitemId,CommentVo vo){
        Long skuId = orderService.getSkuIdByOrderItemId(orderitemId);
        CommentPoExample example=new CommentPoExample();
        CommentPoExample.Criteria criteria=example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        criteria.andOrderitemIdEqualTo(orderitemId);
        criteria.andGoodsSkuIdEqualTo(skuId);
        List<CommentPo> com=commentMapper.selectByExample(example);
        if(com.size()!=0){
            return StatusWrap.just(Status.COMMENT_CREATED);
        }
        CommentPo po=vo.createComment().getCommentPo();
        po.setOrderitemId(orderitemId);
        po.setCustomerId(customerId);
        po.setState(Comment.State.UNCHECK.getCode().byteValue());
        po.setGmtCreate(LocalDateTime.now());
        int ret=commentMapper.insert(po);

        CustomerDTO customer=userService.getCustomerInfoById(po.getCustomerId());
        CommentRetVo retVo=new CommentRetVo(po,customer);
        if (ret != 0) {
            return StatusWrap.of(retVo, HttpStatus.CREATED);
        } else {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public ResponseEntity<StatusWrap> confirmComment(Long commentId, CommentConfirmVo vo){
        CommentPo po=commentMapper.selectByPrimaryKey(commentId);
        if(po==null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if(po.getState()==0){
            if(vo.getConclusion()){
                po.setState(Comment.State.SUCCESS.getCode().byteValue());
            }else{
                po.setState(Comment.State.FAIL.getCode().byteValue());
            }
            po.setGmtModified(LocalDateTime.now());
            int ret=commentMapper.updateByPrimaryKeySelective(po);
            if (ret != 0) {
                return StatusWrap.just(Status.OK);
            } else {
                return StatusWrap.just(Status.FIELD_NOTVALID);
            }
        }
        return StatusWrap.just(Status.COMMENT_CONFIRMED);
    }

}

