package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.controller.ShopController;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

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
        if (create.getName() == null || create.getName().isBlank() || create.getName().isEmpty())
            return StatusWrap.just(Status.FIELD_NOTVALID);
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
        return StatusWrap.of(new ShopView(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<StatusWrap> modifyInfo(Long shopId, ShopCreatorValidation vo) {
        logger.debug("shop[" + shopId + "] try modify");
        logger.debug("modify to name: str[" + vo.getName() + "]");
        if (vo.getName() == null || vo.getName().isEmpty() || vo.getName().isBlank())
            return StatusWrap.just(Status.FIELD_NOTVALID);
        Shop origin = shopDao.select(shopId);
        if (origin == null) {
            logger.debug("no such shop");
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        }
        logger.debug("shop[" + origin.getId() + "] state: " + origin.getState().getName());
//        if (origin.getState() != Shop.State.OFFLINE)
//            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        Shop modified = new Shop();
        modified.setGmtModified(LocalDateTime.now());
        Shop saved = shopDao.update(modified);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        logger.debug("modify ok");
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringOnline(Long shopId) {
        logger.debug("shop[" + shopId + "] try online");
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        logger.debug("shop[" + shop.getId() + "] state: " + shop.getState().getName());
        if (shop.getState() == Shop.State.ONLINE)
            return StatusWrap.ok();
        if (shop.getState() != Shop.State.OFFLINE)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        shop.setState(Shop.State.ONLINE);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        logger.debug("online ok");
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> bringOffline(Long shopId) {
        logger.debug("shop[" + shopId + "] try offline");
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        logger.debug("shop[" + shop.getId() + "] state: " + shop.getState().getName());
        if (shop.getState() == Shop.State.OFFLINE)
            return StatusWrap.ok();
        if (shop.getState() != Shop.State.ONLINE)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        shop.setState(Shop.State.OFFLINE);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        logger.debug("offline ok");
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> forceClose(Long shopId) {
        logger.debug("shop[" + shopId + "] try close");
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        logger.debug("shop[" + shop.getId() + "] state: " + shop.getState().getName());
        if (shop.getState() == Shop.State.CLOSED)
            return StatusWrap.ok();
        if (shop.getState() != Shop.State.ONLINE && shop.getState() != Shop.State.OFFLINE)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        shop.setState(Shop.State.CLOSED);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        logger.debug("close ok");
        return StatusWrap.ok();
    }

    public ResponseEntity<StatusWrap> audit(Long shopId, Boolean approve) {
        logger.debug("shop[" + shopId + "] try audit");
        Shop shop = shopDao.select(shopId);
        if (shop == null)
            return StatusWrap.just(Status.RESOURCE_ID_NOTEXIST);
        logger.debug("shop[" + shop.getId() + "] state: " + shop.getState().getName());
        if (shop.getState() == Shop.State.REJECTED)
            return StatusWrap.just(Status.SHOP_STATE_DENIED);
        shop.setState(approve ? Shop.State.OFFLINE : Shop.State.REJECTED);
        Shop saved = shopDao.update(shop);
        if (saved == null)
            return StatusWrap.just(Status.INTERNAL_SERVER_ERR);
        logger.debug("audit ok");
        return StatusWrap.ok();
    }
}
