package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.dto.FlashSaleTimeSegmentDTO;

import java.util.ArrayList;

public interface TimeSegmentServiceInterface {
    ArrayList<Long> getCurrentFlashSaleTimeSegs();

    Boolean timeSegIsFlashSale(Long id);

    FlashSaleTimeSegmentDTO getFlashSaleTimeSegmentById(Long id);
}
