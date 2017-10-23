package dao.impl;

import dao.AbstractDAOImpl;
import dao.IRoleDAO;
import vo.Role;

import java.util.HashSet;
import java.util.Set;

public class RoleDAOImpl extends AbstractDAOImpl<Integer, Role> implements IRoleDAO {
    public RoleDAOImpl() throws Exception {
        super(Role.class);
    }

    @Override
    public Set<Role> findByMid(String mid) throws Exception {
        Set<Role> list = new HashSet<>();
        sql = "select rid,title,flag from role where rid in(select rid from member_role where mid=?)";
        pre = conn.prepareStatement(sql);
        pre.setString(1, mid);
        res = pre.executeQuery();
        while (res.next()) {
            Role role = new Role();
            role.setRid(res.getInt(1));
            role.setTitle(res.getString(2));
            role.setFlag(res.getString(3));
            list.add(role);
        }
        return list;
    }
}
