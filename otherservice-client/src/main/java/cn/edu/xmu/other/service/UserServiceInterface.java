package cn.edu.xmu.other.service;

import cn.edu.xmu.other.model.dto.CustomerDTO;

public interface UserServiceInterface {
    CustomerDTO getCustomerInfoById(Long userId);
}
