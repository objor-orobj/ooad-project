package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.FloatPricePo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FloatPricesReturnVo
{
    @Data
    public static class CreatedBy
    {
        private Long id=null;
        private String userName=null;

    }
    @Data
    public static class ModifiedBy
    {
        private Long id=null;
        private String userName=null;

    }

    private Long id;

    private Long activityPrice;

    private Integer quantity;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private CreatedBy createdBy=new CreatedBy();

    private ModifiedBy invalidBy=new ModifiedBy();

    private boolean valid;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public void setCreatedBy(Long userId,String userName)
    {
        createdBy.id=userId;
        createdBy.userName=userName;
    }

    public void setModifiedBy(Long userId,String userName)
    {
        invalidBy.id=userId;
        invalidBy.userName=userName;
    }

    public FloatPricesReturnVo(FloatPricePo po)
    {
        this.id=po.getId();
        this.activityPrice=po.getActivityPrice();
        this.quantity=po.getQuantity();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.valid=po.getValid()==(byte)1;
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }
}
