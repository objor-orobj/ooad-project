package cn.edu.xmu.order.service;

import cn.edu.xmu.order.model.dto.FreightModelDTO;

public interface FreightServiceInterface {
    FreightModelDTO getFreightModelById(Long freightId);
}
