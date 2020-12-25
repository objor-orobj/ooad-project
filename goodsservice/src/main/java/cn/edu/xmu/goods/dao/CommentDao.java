package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CommentPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.model.PageWrap;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.CommentPo;
import cn.edu.xmu.goods.model.po.CommentPoExample;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.vo.CommentConfirmVo;
import cn.edu.xmu.goods.model.vo.CommentRetVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.order.service.OrderServiceInterface;
import cn.edu.xmu.other.model.dto.CustomerDTO;
import cn.edu.xmu.other.service.CustomerServiceInterface;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    @Autowired
    private CommentPoMapper commentMapper;

    @Autowired
    private GoodsSkuPoMapper goodsSkuPoMapper;

    @DubboReference(version = "0.0.1")
    private CustomerServiceInterface userService;

    @DubboReference(version = "0.0.1")
    private OrderServiceInterface orderService;

    public ResponseEntity<StatusWrap> getSkuComments(Long skuId, Integer page, Integer pageSize) {
        logger.debug("skuId: " + skuId);
        logger.debug("page: " + page);
        PageHelper.startPage(page, pageSize);
        GoodsSkuPo goodsSkuPo = null;
        try {
//            GoodsSkuPoExample example = new GoodsSkuPoExample();
//            GoodsSkuPoExample.Criteria criteria = example.createCriteria();
//            criteria.andIdEqualTo(skuId);
//            List<GoodsSkuPo> all = goodsSkuPoMapper.selectByExample(example);
//            if (all == null || all.size() == 0)
//                goodsSkuPo = null;
//            else
//                goodsSkuPo = all.get(0);
            goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        } catch (DataAccessException exception) {
            exception.printStackTrace();
        }
        if (goodsSkuPo == null) {
            logger.debug("sku not found");
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        criteria.andStateEqualTo(Comment.State.SUCCESS.getCode().byteValue());
        List<CommentPo> com = commentMapper.selectByExample(example);
        if (com.size() == 0) {
            return StatusWrap.of(new ArrayList<>());
        }
        List<CommentRetVo> commentRet = new ArrayList<>(com.size());
        for (CommentPo po : com) {
            logger.debug("customerId: " + po.getCustomerId());
            CustomerDTO customer = null;
            try {
                customer = userService.getCustomerInfoById(po.getCustomerId());
            } catch (Exception exception) {
                logger.error("failed to fetch customer info");
                exception.printStackTrace();
            }
            if (customer == null) {
                logger.error("customer null");
                continue;
            }
            logger.debug("dto id: " + customer.getId());
            logger.debug("dto name: " + customer.getName());
            logger.debug("dto userName: " + customer.getUserName());
            CommentRetVo comment = new CommentRetVo(po, customer);
            commentRet.add(comment);
        }
        PageInfo<CommentPo> raw = PageInfo.of(com);
        return StatusWrap.of(PageWrap.of(raw, commentRet));
    }

    public ResponseEntity<StatusWrap> getSelfComments(Long customerId, Integer page, Integer pageSize) {
        List<CommentPo> com = null;
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, pageSize);
        criteria.andCustomerIdEqualTo(customerId);
        com = commentMapper.selectByExample(example);
        if (com == null || com.isEmpty()) {
            return StatusWrap.of(new ArrayList<>());
        }
        List<CommentRetVo> commentRet = new ArrayList<>(com.size());
        for (CommentPo po : com) {
            CustomerDTO customer = userService.getCustomerInfoById(po.getCustomerId());
            CommentRetVo comment = new CommentRetVo(po, customer);
            commentRet.add(comment);
        }
        PageInfo<CommentPo> raw = PageInfo.of(com);
        return StatusWrap.of(PageWrap.of(raw, commentRet));
    }

    public ResponseEntity<StatusWrap> getShopComments(Integer state, Integer page, Integer pageSize) {
        List<CommentPo> com = null;
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, pageSize);
        if (state == null)
            criteria.andIdIsNotNull();
        else {
            Byte s;
            switch (state) {
                case 0:
                    s = 0;
                    break;
                case 1:
                    s = 1;
                    break;
                case 2:
                    s = 2;
                    break;
//            GoodsSkuPoExample snDupExample = new GoodsSkuPoExample();
//            GoodsSkuPoExample.Criteria snDupCriteria = snDupExample.createCriteria();
//            snDupCriteria.andSkuSnEqualTo(vo.getSn());
//            List<GoodsSkuPo> dup = goodsSkuPoMapper.selectByExample(snDupExample);
//            logger.debug("dup: " + dup);
//            if (dup != null && dup.size() > 0) {
//                return StatusWrap.just(Status.SKUSN_SAME);
//            }
//        }
                default:
                    return StatusWrap.just(Status.FIELD_NOTVALID);
            }
            criteria.andStateEqualTo(s);
        }
        com = commentMapper.selectByExample(example);
        if (com == null || com.isEmpty()) {
            return StatusWrap.of(new ArrayList<>());
        }
        List<CommentRetVo> commentRet = new ArrayList<>(com.size());
        for (CommentPo po : com) {
            CustomerDTO customer = userService.getCustomerInfoById(po.getCustomerId());
            CommentRetVo comment = new CommentRetVo(po, customer);
            commentRet.add(comment);
        }
        PageInfo<CommentPo> raw = PageInfo.of(com);
        return StatusWrap.of(PageWrap.of(raw, commentRet));
    }

    public ResponseEntity<StatusWrap> createComment(Long customerId, Long orderitemId, CommentVo vo) {
        logger.debug("ordertimeId: " + orderitemId);
        logger.debug("customerId: " + customerId);
        Boolean belongs = false;
        try {
            belongs = orderService.isOrderItemBelongToUser(orderitemId, customerId);
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("error confirming order item");
        }
        if (belongs == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        if (!belongs)
            return StatusWrap.just(Status.USER_NOTBUY);
        Long skuId = null;
        logger.debug("orderItemId: " + orderitemId);
        logger.debug("comment.content: " + vo.getContent());
        logger.debug("comment.type: " + vo.getType());
        try {
            skuId = orderService.getSkuIdByOrderItemId(orderitemId);
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("error fetching skuId");
        }
        if (skuId == null) {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
        if (vo.getContent() == null || vo.getType() == null) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        logger.debug("query customerId: " + customerId);
        logger.debug("query orderitemId: " + orderitemId);
        logger.debug("query skuId: " + skuId);
        criteria.andCustomerIdEqualTo(customerId)
                .andOrderitemIdEqualTo(orderitemId)
                .andGoodsSkuIdEqualTo(skuId);
        List<CommentPo> com = null;
        try {
            com = commentMapper.selectByExample(example);
        } catch (DataAccessException exception) {
            exception.printStackTrace();
            logger.error("error query comments");
        }
        logger.debug("existing comment: " + com);
        if (com != null && com.size() != 0) {
            return StatusWrap.just(Status.COMMENT_CREATED);
        }
        CommentPo po = vo.createComment().getCommentPo();
        po.setOrderitemId(orderitemId);
        po.setCustomerId(customerId);
        po.setGoodsSkuId(skuId);
        po.setState(Comment.State.UNCHECK.getCode().byteValue());
        po.setGmtCreate(LocalDateTime.now());
        int ret;
        try {
            ret = commentMapper.insert(po);
        } catch (DataAccessException exception) {
            logger.debug("error saving to database");
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
        CustomerDTO customer = null;
        try {
            customer = userService.getCustomerInfoById(po.getCustomerId());
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("error fetching customerInfo");
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
        CommentRetVo retVo = new CommentRetVo(po, customer);
        if (ret != 0) {
            return StatusWrap.of(retVo, HttpStatus.CREATED);
        } else {
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        }
    }

    public ResponseEntity<StatusWrap> confirmComment(Long commentId, CommentConfirmVo vo) {
        if (vo.getConclusion() == null) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        CommentPo po = commentMapper.selectByPrimaryKey(commentId);
        if (po == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (po.getState() == 0) {
            if (vo.getConclusion()) {
                po.setState(Comment.State.SUCCESS.getCode().byteValue());
            } else {
                po.setState(Comment.State.FAIL.getCode().byteValue());
            }
            po.setGmtModified(LocalDateTime.now());
            int ret = commentMapper.updateByPrimaryKeySelective(po);
            if (ret != 0) {
                return StatusWrap.just(Status.OK);
            } else {
                return StatusWrap.just(Status.FIELD_NOTVALID);
            }
        }
        return StatusWrap.just(Status.COMMENT_CONFIRMED);
    }

}

