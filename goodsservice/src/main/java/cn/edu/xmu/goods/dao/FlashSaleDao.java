package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.FlashSaleItemPoMapper;
import cn.edu.xmu.goods.mapper.FlashSalePoMapper;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.FlashSale;
import cn.edu.xmu.goods.model.po.FlashSaleItemPo;
import cn.edu.xmu.goods.model.po.FlashSaleItemPoExample;
import cn.edu.xmu.goods.model.po.FlashSalePo;
import cn.edu.xmu.goods.model.po.FlashSalePoExample;
import cn.edu.xmu.goods.model.ro.FlashSaleItemExtendedView;
import cn.edu.xmu.goods.model.vo.ReturnGoodsSkuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FlashSaleDao {
    @Autowired
    private FlashSalePoMapper flashSalePoMapper;
    @Autowired
    private FlashSaleItemPoMapper flashSaleItemPoMapper;
    @Autowired
    private GoodsSkuDao goodsSkuDao;

    public FlashSale selectActivity(@NotNull Long id) {
        FlashSalePo po;
        try {
            po = flashSalePoMapper.selectByPrimaryKey(id);
        } catch (DataAccessException exception) {
            return null;
        }
        if (po == null) return null;
        return new FlashSale(po);
    }

    public FlashSale createActivity(@NotNull FlashSale flashSale) {
        FlashSalePo po = flashSale.toFlashSalePo();
        try {
            flashSalePoMapper.insert(po);
        } catch (DataAccessException exception) {
            return null;
        }
        return new FlashSale(po);
    }

    public FlashSale updateActivity(@NotNull FlashSale flashSale) {
        try {
            flashSalePoMapper.updateByPrimaryKeySelective(flashSale.toFlashSalePo());
        } catch (DataAccessException exception) {
            return null;
        }
        return flashSale;
    }

    public FlashSale.Item selectItem(@NotNull Long id) {
        FlashSaleItemPo po;
        try {
            po = flashSaleItemPoMapper.selectByPrimaryKey(id);
        } catch (DataAccessException exception) {
            return null;
        }
        if (po == null) return null;
        return new FlashSale.Item(po);
    }

    public FlashSale.Item insertItem(@NotNull FlashSale.Item bo) {
        FlashSaleItemPo po = bo.toItemPo();
        try {
            flashSaleItemPoMapper.insert(po);
        } catch (DataAccessException exception) {
            return null;
        }
        return new FlashSale.Item(po);
    }

    /*
     * remove flash sale item by id, return null on exception
     */
    public Long deleteItem(Long id) {
        try {
            flashSaleItemPoMapper.deleteByPrimaryKey(id);
        } catch (DataAccessException exception) {
            return null;
        }
        return id;
    }

    public Flux<FlashSaleItemExtendedView> getAllFlashSaleItemsWithinTimeSegments(List<Long> timeSegIds) {
        return Mono.just(timeSegIds).map(
                timeIds -> {
                    FlashSalePoExample example = new FlashSalePoExample();
                    FlashSalePoExample.Criteria criteria = example.createCriteria();
                    criteria.andTimeSegIdIn(timeIds)
                            .andFlashDateBetween(LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX));
                    List<FlashSalePo> allSales = flashSalePoMapper.selectByExample(example);
                    List<Long> saleIds = allSales.stream().map(FlashSalePo::getId).collect(Collectors.toList());

                    FlashSaleItemPoExample example2 = new FlashSaleItemPoExample();
                    FlashSaleItemPoExample.Criteria criteria2 = example2.createCriteria();
                    criteria2.andIdIn(saleIds);
                    List<FlashSaleItemPo> items = flashSaleItemPoMapper.selectByExample(example2);
                    return items;
                }
        ).flatMapMany(Flux::fromIterable).map(po -> {
            Long skuId = po.getGoodsSkuId();
            ReturnGoodsSkuVo skuVo = goodsSkuDao.getSingleSimpleSku(skuId.intValue());
            return new FlashSaleItemExtendedView(new FlashSale.Item(po), skuVo);
        });

//        FlashSalePoExample example = new FlashSalePoExample();
//        FlashSalePoExample.Criteria criteria = example.createCriteria();
//        criteria.andTimeSegIdIn(timeSegIds)
//                .andFlashDateBetween(LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX));
//        List<FlashSalePo> allSales = flashSalePoMapper.selectByExample(example);
//        List<Long> saleIds = allSales.stream().map(FlashSalePo::getId).collect(Collectors.toList());
//
//        FlashSaleItemPoExample example2 = new FlashSaleItemPoExample();
//        FlashSaleItemPoExample.Criteria criteria2 = example2.createCriteria();
//        criteria2.andIdIn(saleIds);
//        List<FlashSaleItemPo> items = flashSaleItemPoMapper.selectByExample(example2);

//        List<FlashSaleItemExtendedView> views = items.stream().map(
//                po -> {
//                    Long skuId = po.getGoodsSkuId();
//                    ReturnGoodsSkuVo skuVo = goodsSkuDao.getSingleSimpleSku(skuId.intValue());
//                    return new FlashSaleItemExtendedView(new FlashSale.Item(po), skuVo);
//                }
//        ).collect(Collectors.toList());

//        return views;
    }
}
