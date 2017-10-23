package service.back.impl;

import dao.impl.ActionDAOImpl;
import dao.impl.MemberDAOImpl;
import dao.impl.RoleDAOImpl;
import factory.DAOFactory;
import service.AbstractService;
import service.back.IMemberServiceBack;
import vo.Action;
import vo.Member;
import vo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemberServiceBackImpl extends AbstractService implements IMemberServiceBack {
    @Override
    public Map<String, Object> login(Member vo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Member ckVo = DAOFactory.getInstance(MemberDAOImpl.class).findById(vo.getMid());
        if (ckVo != null) {

            if (vo.getPassword().equals(ckVo.getPassword())) {
                Set<Role> allRoles = DAOFactory.getInstance(RoleDAOImpl.class).findByMid(ckVo.getMid());
                Set<Action> allActions = DAOFactory.getInstance(ActionDAOImpl.class).findByMid(ckVo.getMid());
                map.put("flag", true);
                map.put("mid", ckVo.getMid());
                map.put("name", ckVo.getName());
                map.put("sflag", ckVo.getSflag());
                map.put("allRoles",allRoles);
                map.put("allActions",allActions);
            } else {
                map.put("flag", false);
            }
        } else {
            map.put("flag", false);
        }
        return map;
    }
}
