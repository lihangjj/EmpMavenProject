package dao.impl;

import dao.AbstractDAOImpl;
import dao.IActionDAO;
import vo.Action;

import java.util.HashSet;
import java.util.Set;

public class ActionDAOImpl extends AbstractDAOImpl<Integer, Action> implements IActionDAO {
    public ActionDAOImpl() throws Exception {
        super(Action.class);
    }

    @Override
    public Set<Action> findByMid(String mid) throws Exception {
        Set<Action> list = new HashSet<>();
        sql = "select actid,title,flag from action where actid in(select actid from role_action where rid in(select rid " +
                "from member_role where mid=?))";
        pre = conn.prepareStatement(sql);
        pre.setString(1, mid);
        res = pre.executeQuery();
        while (res.next()) {
            Action action = new Action();
            action.setActid(res.getInt(1));
            action.setTitle(res.getString(2));
            action.setFlag(res.getString(3));
            list.add(action);
        }
        return list;
    }

    @Override
    public boolean verifyPermission(String mid, String permission) throws Exception {
        sql = "select count(*) from action where flag=" + "\'" + permission + "\' AND actid in(SELECT actid from role_action " +
                "where rid in(SELECT rid from member where mid=\'" + mid + "\'))";
        pre = conn.prepareStatement(sql);
        res = pre.executeQuery();
        while (res.next()) {
            return res.getInt(1) > 0;
        }
        return false;
    }
}
