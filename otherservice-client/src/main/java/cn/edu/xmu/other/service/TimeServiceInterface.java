package cn.edu.xmu.other.service;

import cn.edu.xmu.other.model.dto.FlashSaleTimeSegmentDTO;

import java.util.ArrayList;

public interface TimeServiceInterface {
    ArrayList<Long> getCurrentFlashSaleTimeSegs();

    Boolean timeSegIsFlashSale(Long id);

    FlashSaleTimeSegmentDTO getFlashSaleTimeSegmentById(Long id);
}
