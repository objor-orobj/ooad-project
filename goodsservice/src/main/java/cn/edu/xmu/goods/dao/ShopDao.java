package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.ShopPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public class ShopDao {
    @Autowired
    private ShopPoMapper mapper;

    /*
     * return Shop of given ID
     * null if not found
     */
    public Shop select(@NotNull Long id) {
        ShopPo po;
        try {
            po = mapper.selectByPrimaryKey(id);
        } catch (DataAccessException exception) {
            return null;
        }
        if (po == null) return null;
        return new Shop(po);
    }

    /*
     * update Shop of given ID
     * shopId not checked, return null on failure
     */
    public Shop update(@NotNull Shop shop) {
        try {
            mapper.updateByPrimaryKeySelective(shop.toShopPo());
        } catch (DataAccessException exception) {
            return null;
        }
        return shop;
    }

    /*
     * insert Shop as-is
     * shopId ignored, return null on failure
     */
    public Shop create(@NotNull Shop shop) {
        ShopPo po = shop.toShopPo();
        try {
            mapper.insert(po);
        } catch (DataAccessException exception) {
            return null;
        }
        return new Shop(po);
    }

    /*
     * DANGER ZONE !!!
     * remove from database, intended only for a failed changeUserDepart()
     */
    public Boolean prune(@NotNull Long shopId) {
        try {
            mapper.deleteByPrimaryKey(shopId);
        } catch (DataAccessException exception) {
            return false;
        }
        return true;
    }
}
