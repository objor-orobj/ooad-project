package cn.edu.xmu.other.service;

import cn.edu.xmu.other.model.dto.CustomerDTO;

public interface CustomerServiceInterface {
    CustomerDTO getCustomerInfoById(Long userId);
}
