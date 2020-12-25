package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.BrandDao;
import cn.edu.xmu.goods.model.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-09 11:05
 */
@Service
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    public Object createBrand(BrandVo vo) {
        return brandDao.createBrand(vo);
    }

    public Object uploadBrandImage(Long id,String imageUrl) {
        return brandDao.uploadBrandImage(id,imageUrl);
    }

    public Object getAllBrands(Integer page,Integer pageSize) {
        return brandDao.getAllBrands(page,pageSize);
    }

    public Object modifyBrand(Long id,BrandVo vo) {
        return brandDao.modifyBrand(id,vo);
    }

    public Object deleteBrand(Long id) {
        return brandDao.deleteBrand(id);
    }

    public Object addSpuToBrand(Long id,Long spuId){
        return brandDao.addSpuToBrand(id,spuId);
    }

    public Object removeSpuFromBrand(Long id,Long spuId){
        return brandDao.removeSpuFromBrand(id,spuId);
    }
}
