package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.dto.ShopDTO;

public interface ShopServiceInterface {
    ShopDTO getShopInfoById(Long id);
}
