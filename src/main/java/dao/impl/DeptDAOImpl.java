package dao.impl;

import dao.AbstractDAOImpl;
import dao.IDeptDAO;
import vo.Dept;

public class DeptDAOImpl extends AbstractDAOImpl<Integer, Dept> implements IDeptDAO {
    public DeptDAOImpl() throws Exception {
        super(Dept.class);
    }
}
