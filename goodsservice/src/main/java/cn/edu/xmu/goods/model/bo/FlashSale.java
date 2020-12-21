package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.FlashSaleItemPo;
import cn.edu.xmu.goods.model.po.FlashSalePo;
import cn.edu.xmu.goods.model.vo.FlashSaleCreatorValidation;
import cn.edu.xmu.goods.model.vo.FlashSaleItemCreatorValidation;
import cn.edu.xmu.goods.model.vo.FlashSaleModifierValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashSale {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum State {
        OFFLINE(0, "已下线"),
        ONLINE(1, "已上线"),
        DELETED(2, "已删除");
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

        public static State fromCode(Byte code) {
            if (code == null) return OFFLINE;
            else if (code.equals((byte) 0)) return OFFLINE;
            else if (code.equals((byte) 1)) return ONLINE;
            else if (code.equals((byte) 2)) return DELETED;
            return OFFLINE;
        }
    }

    private Long id;
    private LocalDateTime flashDate;
    private Long timeSegId;
    private State state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public FlashSalePo toFlashSalePo() {
        FlashSalePo po = new FlashSalePo();
        po.setId(id);
        po.setFlashDate(flashDate);
        po.setTimeSegId(timeSegId);
        po.setState(state == null ? null : state.getCode().byteValue());
        po.setGmtCreate(gmtCreate);
        po.setGmtModified(gmtModified);
        return po;
    }

    public FlashSale(FlashSalePo po) {
        this.id = po.getId();
        this.flashDate = po.getFlashDate();
        this.timeSegId = po.getTimeSegId();
        this.state = State.fromCode(po.getState());
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public FlashSale(FlashSaleCreatorValidation vo) {
        this.flashDate = vo.getFlashDate();
    }

    public FlashSale(FlashSaleModifierValidation vo) {
        this.flashDate = vo.getFlashDate();
        this.gmtModified = LocalDateTime.now();
    }

    @Data
    public static class Item {
        private Long id;
        private Long saleId;
        private Long goodsSkuId;
        private Long price;
        private Integer quantity;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtModified;

        public Item(FlashSaleItemCreatorValidation vo) {
            this.goodsSkuId = vo.getSkuId();
            this.price = vo.getPrice();
            this.quantity = vo.getQuantity();
        }

        public Item(FlashSaleItemPo po) {
            this.id = po.getId();
            this.saleId = po.getSaleId();
            this.goodsSkuId = po.getGoodsSkuId();
            this.price = po.getPrice();
            this.quantity = po.getQuantity();
            this.gmtCreate = po.getGmtCreate();
            this.gmtModified = po.getGmtModified();
        }

        public FlashSaleItemPo toItemPo() {
            FlashSaleItemPo po = new FlashSaleItemPo();
            po.setId(id);
            po.setSaleId(saleId);
            po.setGoodsSkuId(goodsSkuId);
            po.setPrice(price);
            po.setQuantity(quantity);
            po.setGmtCreate(gmtCreate);
            po.setGmtModified(gmtModified);
            return po;
        }
    }
}
