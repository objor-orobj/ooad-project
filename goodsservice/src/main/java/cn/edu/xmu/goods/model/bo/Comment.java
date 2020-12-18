package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.CommentPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-07 19:55
 */
@Data
public class Comment {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State implements Serializable {
        UNCHECK(0,"未审核"),
        SUCCESS(1,"评论成功"),
        FAIL(2,"未通过");
        private final Integer code;
        private final String name;

        State(Integer code, String name){
            this.code=code;
            this.name=name;
        }

        public Integer getCode() {return code;}

        public String getName() {return name;}

        public static Comment.State getStateByCode(Integer code) {
            switch (code) {
                case 0:
                    return UNCHECK;
                case 1:
                    return SUCCESS;
                case 2:
                    return FAIL;
            }
            return null;
        }
    }

    public enum Type implements  Serializable{
        GOOD(0,"好评"),
        MEDIUM(1,"中评"),
        BAD(2,"差评");
        private final int code;
        private final String name;

        Type(int code,String name){
            this.code=code;
            this.name=name;
        }


        public Integer getCode() {return code;}

        public String getName() {return name;}

        public static Comment.Type getTypeByCode(Integer code) {
            switch (code) {
                case 0:
                    return GOOD;
                case 1:
                    return MEDIUM;
                case 2:
                    return BAD;
            }
            return null;
        }
    }

    private Long id;

    private Long customerId;

    private Long goodsSkuId;

    private Long orderitemId;

    private Type type;

    private String content;

    private State state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public CommentPo getCommentPo(){
        CommentPo po=new CommentPo();
        po.setId(this.id);
        //po.setGoodsSkuId(this.getGoodsSkuId());
        //po.setOrderitemId(this.getOrderitemId());
        //po.setCustomerId(this.getCustomerId());
        po.setContent(this.getContent());
        switch(this.getType().code){
            case 0:po.setType(Type.GOOD.getCode().byteValue());break;
            case 1:po.setType(Type.MEDIUM.getCode().byteValue());break;
            case 2:po.setType(Type.BAD.getCode().byteValue());
        }
        //po.setGmtCreate(this.getGmtCreate());
        //po.setGmtModified(this.getGmtModified());
        //po.setState(this.getState().getCode().byteValue());
        //po.setType(this.getType().getCode().byteValue());
        return po;
    }



    public Comment(){
    }

    public Comment(CommentPo po){
        this.id=po.getId();
        this.customerId=po.getCustomerId();
        this.goodsSkuId=po.getGoodsSkuId();
        this.orderitemId=po.getOrderitemId();
        this.type=Type.getTypeByCode(po.getType().intValue());
        this.content=po.getContent();
        this.state=State.getStateByCode(po.getState().intValue());
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }


}
