package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.CommentPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-07 21:05
 */
@Data
public class CommentRetVo {
    private Long id;
    private Long customerId;
    private Long goodsSkuId;
    private Integer type;
    private String content;
    private Integer state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public CommentRetVo(CommentPo obj){
        this.id=obj.getId();
        this.customerId=obj.getCustomerId();
        this.goodsSkuId=obj.getGoodsSkuId();
        switch(obj.getType()){
            case 0:this.type= Comment.Type.GOOD.getCode();break;
            case 1:this.type=Comment.Type.MEDIUM.getCode();break;
            case 2:this.type= Comment.Type.BAD.getCode();
        }
        this.content=obj.getContent();
        switch(obj.getState()){
            case 0:this.state= Comment.State.UNCHECK.getCode();break;
            case 1:this.state= Comment.State.SUCCESS.getCode();break;
            case 2:this.state= Comment.State.FAIL.getCode();
        }
        this.gmtCreate=obj.getGmtCreate();
        this.gmtModified=obj.getGmtModified();
    }
}
