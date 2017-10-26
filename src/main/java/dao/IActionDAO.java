package dao;

import vo.Action;

import java.util.Set;

public interface IActionDAO extends IDAO<Integer, Action> {
    Set<Action> findByMid(String mid)throws Exception;
    boolean verifyPermission(String mid,String permission)throws Exception;
}
