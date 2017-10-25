package dao.impl;

import dao.AbstractDAOImpl;
import dao.IEmpDAO;
import vo.Emp;

public class EmpDAOImpl extends AbstractDAOImpl<Integer, Emp> implements IEmpDAO {
    public EmpDAOImpl() throws Exception {
        super(Emp.class);
    }
}
