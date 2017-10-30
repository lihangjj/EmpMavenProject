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

import java.util.*;

public class MemberServiceBackImpl extends AbstractService implements IMemberServiceBack {
    @Override
    public Map<String, Object> login(Member vo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Member ckVo = DAOFactory.getInstance(MemberDAOImpl.class).findById(vo.getMid());
        if (ckVo != null) {

            if (vo.getPassword().equals(ckVo.getPassword())) {
                Set<Role> allRoles = DAOFactory.getInstance(RoleDAOImpl.class).findByMid(ckVo.getMid());
                Set<Action> allActions = DAOFactory.getInstance(ActionDAOImpl.class).findByMid(ckVo.getMid());
                Iterator<Action> iterator = allActions.iterator();
                Set<String> actionFlagSet = new HashSet<>();
                while (iterator.hasNext()) {
                    actionFlagSet.add(iterator.next().getFlag());
                }
                map.put("flag", true);
                map.put("mid", ckVo.getMid());
                map.put("name", ckVo.getName());
                map.put("sflag", ckVo.getSflag());
                map.put("allRoles", allRoles);
                map.put("allActions", allActions);
                map.put("actionFlagSet", actionFlagSet);//保存权限标记为set集合
            } else {
                map.put("flag", false);
            }
        } else {
            map.put("flag", false);
        }
        return map;
    }

    @Override
    public boolean add(Member vo,String[] rid) throws Exception {

        return DAOFactory.getInstance(MemberDAOImpl.class).add(vo,rid);
    }

    @Override
    public List<Member> list() throws Exception {
        return DAOFactory.getInstance(MemberDAOImpl.class).findAll();
    }

    @Override
    public boolean findById(String mid) throws Exception {
        return DAOFactory.getInstance(MemberDAOImpl.class).findById(mid) != null;
    }

}
