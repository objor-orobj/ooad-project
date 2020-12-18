package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.dto.ShopDTO;
import cn.edu.xmu.goods.model.ro.ShopView;
import cn.edu.xmu.goods.model.vo.ShopCreatorValidation;
import cn.edu.xmu.privilegeservice.client.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@DubboService(version = "0.0.1")
@Service
public class ShopService implements ShopServiceInterface {
    @Autowired
    private ShopDao shopDao;
    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IUserService userService;

    @Override
    public ShopDTO getShopInfoById(Long id) {
        Shop bo = shopDao.select(id);
        if (bo == null) return null;
        ShopDTO dto = new ShopDTO();
        dto.setId(bo.getId());
        dto.setName(bo.getName());
        dto.setState(bo.getState().getCode().byteValue());
        dto.setGmtCreate(bo.getGmtCreate());
        dto.setGmtModified(bo.getGmtModified());
        return dto;
    }

    public ResponseEntity<StatusWrap> createByUser(ShopCreatorValidation vo, Long userId) {
        Shop create = new Shop(vo);
        create.setState(Shop.State.PENDING_APPROVAL);
        create.setGmtCreate(LocalDateTime.now());
        Shop saved = shopDao.create(create);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        boolean ok = userService.changeUserDepart(userId, saved.getId());
        if (!ok) {
            shopDao.prune(saved.getId());
            return StatusWrap.just(Status.SHOP_CREATE_GATEWAY_DENIED);
        }
        return StatusWrap.of(new ShopView(saved));
    }

    public ResponseEntity<StatusWrap> modifyInfo(Long shopId, ShopCreatorValidation vo) {
        Shop origin = shopDao.select(shopId);
        if (origin == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (origin.getState() != Shop.State.OFFLINE)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        Shop modified = new Shop(vo);
        modified.setGmtModified(LocalDateTime.now());
        Shop saved = shopDao.update(modified);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringOnline(Long shopId) {
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (shop.getState() == Shop.State.ONLINE)
            return StatusWrap.ok();
        if (shop.getState() != Shop.State.OFFLINE)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        shop.setState(Shop.State.ONLINE);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringOffline(Long shopId) {
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (shop.getState() == Shop.State.OFFLINE)
            return StatusWrap.ok();
        if (shop.getState() != Shop.State.ONLINE)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        shop.setState(Shop.State.OFFLINE);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> forceClose(Long shopId) {
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (shop.getState() == Shop.State.CLOSED)
            return StatusWrap.ok();
        if (shop.getState() != Shop.State.ONLINE && shop.getState() != Shop.State.OFFLINE)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        shop.setState(Shop.State.CLOSED);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> audit(Long shopId, Boolean approve) {
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        if (shop.getState() != Shop.State.PENDING_APPROVAL)
            return StatusWrap.of(Status.SHOP_ALREADY_AUDITED);
        shop.setState(approve ? Shop.State.OFFLINE : Shop.State.REJECTED);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        return StatusWrap.ok();
    }
}
