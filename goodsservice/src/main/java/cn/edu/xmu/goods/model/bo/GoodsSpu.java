package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.GoodsSpuVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GoodsSpu
{
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State implements Serializable {
        NOT_SHELF(0, "未上架"),
        SHELF(4, "上架"),
        DELETED(6, "已删除");
        private final Integer code;
        private final String name;

        State(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
    private Long id;
    private String name;
    private Long brandId;
    private Long categoryId;
    private Long freightId;
    private Long shopId;
    private String goodsSn;
    private String detail;
    private String imageUrl;
    private State state;
    private String spec;
    private Byte disabled;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public GoodsSpu(GoodsSpuVo vo) {
        this.name = vo.getName();
        this.state = GoodsSpu.State.NOT_SHELF;
        this.gmtCreate = LocalDateTime.now();
    }

    public GoodsSpuPo getGoodsSpuPo() {
        GoodsSpuPo po = new GoodsSpuPo();
        po.setName(this.name);
        // TODO po.setState(this.state.getCode().byteValue());
        po.setGmtCreate(this.gmtCreate);
        return po;
    }
    public GoodsSpu(Long shopId) {
        this.shopId = shopId;
    }
    public GoodsSpu(String goodsSn) {
        this.goodsSn = goodsSn;
    }
}
