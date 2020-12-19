package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.*;
import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GrouponService {
    @Autowired
    private GrouponActivityDao grouponActivitydao;

    //GROUPON
    public ResponseEntity<StatusWrap> createGrouponActivity(GrouponActivity grouponActivity) {
        return grouponActivitydao.createGrouponActivity(grouponActivity);
    }

    public ResponseEntity<StatusWrap> getGrouponActivitybyspuid(Long id) {
        return grouponActivitydao.getGrouponActivityBySpuid(id);
    }

    public ResponseEntity<StatusWrap> getGrouponActivity(GrouponActivityInVo vo) {
        return grouponActivitydao.getGrouponActivity(vo);
    }

    public ResponseEntity<StatusWrap> getallGrouponActivity(GrouponActivityInVo vo) {
        return grouponActivitydao.getallGrouponActivity(vo);
    }

    public ResponseEntity<StatusWrap> modifyGrouponActivityById(Long Id, GrouponActivityVo vo) {
        return grouponActivitydao.modifyGrouponActivity(Id, vo);
    }

    public ResponseEntity<StatusWrap> GtoONLINE(Long shopId, Long Id) {
        return grouponActivitydao.GtoONLINE(shopId, Id);
    }

    public ResponseEntity<StatusWrap> GtoOFFLINE(Long shopId, Long Id) {
        return grouponActivitydao.GtoOFFLINE(shopId, Id);
    }

    public ResponseEntity<StatusWrap> deleteGrouponActivity(Long shopId, Long id) {
        GrouponActivity grouponActivity = new GrouponActivity(grouponActivitydao.getGrouponActivityById(id));
//        if (grouponActivity.getState() == GrouponActivity.State.DELETE) {
//            return StatusWrap.just(Status.OK);
//        }
        if (grouponActivity.getState() == GrouponActivity.State.ONLINE) {
            return StatusWrap.just(Status.GROUPON_STATENOTALLOW);
        }
        return grouponActivitydao.deleteGrouponActivityById(shopId, id);
    }
}
