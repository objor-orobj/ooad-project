package cn.edu.xmu.goods.model.ro;

import cn.edu.xmu.goods.model.bo.FlashSale;
import cn.edu.xmu.other.model.dto.FlashSaleTimeSegmentDTO;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class FlashSaleWithTimeSegmentView {
    private final Long id;
    private final LocalDateTime flashDate;
    private final TimeSegmentView timeSeg;
    private final LocalDateTime gmtCreate;
    private final LocalDateTime gmtModified;

    @Data
    public static class TimeSegmentView {
        private final Long id;
        private final LocalDateTime beginTime;
        private final LocalDateTime endTime;
        private final LocalDateTime gmtCreate;
        private final LocalDateTime gmtModified;

        private TimeSegmentView(FlashSaleTimeSegmentDTO dto) {
            this.id = dto.getId();
            this.beginTime = dto.getBeginTime();
            this.endTime = dto.getEndTime();
            this.gmtCreate = dto.getGmtCreate();
            this.gmtModified = dto.getGmtModified();
        }
    }

    public FlashSaleWithTimeSegmentView(FlashSale bo, FlashSaleTimeSegmentDTO timeSegment) {
        this.id = bo.getId();
        this.flashDate = bo.getFlashDate();
        this.timeSeg = new TimeSegmentView(timeSegment);
        this.gmtCreate = bo.getGmtCreate();
        this.gmtModified = bo.getGmtModified();
    }
}
