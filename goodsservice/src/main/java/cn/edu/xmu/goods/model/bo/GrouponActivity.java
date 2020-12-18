package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.vo.GrouponActivityVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GrouponActivity {


    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State  {
        OFFLINE(0, "下线"),
        ONLINE(1,"上线"),
        DELETE(2, "已删除");
        private final Integer code;
        private final String name;

        State(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static GrouponActivity.State fromCode(Integer code) {
            switch (code) {
                case 0:
                    return OFFLINE;
                case 1:
                    return ONLINE;
                case 2:
                    return DELETE;
                default:
                    return null;
            }
        }
    }


    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private State state;
    private Long shopId;
    private Long goodsSpuId;
    private String strategy;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public GrouponActivity() {
    }

    public GrouponActivity(GrouponActivityPo po) {
        this.id = po.getId();
        this.name = po.getName();
        this.beginTime = po.getBeginTime();
        this.endTime = po.getEndTime();
        this.shopId = po.getShopId();
        this.state = State.fromCode(po.getState().intValue());
        this.goodsSpuId = po.getGoodsSpuId();
        this.strategy = po.getStrategy();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }


    public GrouponActivity(GrouponActivityVo vo) {
        this.name = vo.getName();
        this.beginTime = vo.getBeginTime();
        this.endTime = vo.getEndTime();
        this.shopId = vo.getShopId();
        this.state = State.fromCode(vo.getState().intValue());
        this.goodsSpuId = vo.getGoodsSpuId();
        this.strategy = vo.getStrategy();
        this.gmtCreate = LocalDateTime.now();
    }

    public GrouponActivityPo getGrouponActivityPo() {
        GrouponActivityPo po = new GrouponActivityPo();
        po.setId(this.id);
        po.setName(this.name);
        po.setShopId(this.shopId);
        po.setState(this.state.getCode().byteValue());
        po.setGoodsSpuId(this.goodsSpuId);
        po.setBeginTime(this.beginTime);
        po.setEndTime(this.endTime);
        po.setStrategy(this.strategy);
        po.setGmtCreate(LocalDateTime.now());
        return po;
    }

    public State getState() {
        return state;
    }
}
