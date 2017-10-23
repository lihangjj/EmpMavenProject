package dao;

import vo.Role;

import java.util.List;
import java.util.Set;

public interface IRoleDAO extends IDAO<Integer, Role> {
    Set<Role> findByMid(String mid) throws Exception;
}
