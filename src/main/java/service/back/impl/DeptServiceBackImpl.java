package service.back.impl;

import dao.impl.DeptDAOImpl;
import dao.impl.EmpDAOImpl;
import factory.DAOFactory;
import service.AbstractService;
import service.back.IDeptServiceBack;
import vo.Dept;
import vo.Emp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeptServiceBackImpl extends AbstractService implements IDeptServiceBack {
    @Override
    public List<Dept> list() throws Exception {
        return DAOFactory.getInstance(DeptDAOImpl.class).findAll();
    }

    @Override
    public boolean updateMax(int deptno, int max) throws Exception {
        Dept dept = DAOFactory.getInstance(DeptDAOImpl.class).findById(deptno);
        dept.setMaxnum(max);
        return DAOFactory.getInstance(DeptDAOImpl.class).doUpdate(dept);
    }

    @Override
    public List<Emp> findEmpsByDept(int deptno) throws Exception {
        return DAOFactory.getInstance(EmpDAOImpl.class).findEmpsByDept(deptno);
    }

    @Override
    public Map<String,Object> findEmpByDeptAndSplit(int deptno, int curentPage, int lineSize, String column, String keyWord) throws Exception {
        Map<String,Object> map=new HashMap<>();
        List<Emp> emps= DAOFactory.getInstance(EmpDAOImpl.class).findAllSplitByFlag("deptno", deptno, curentPage, lineSize, column, keyWord);
        int x=DAOFactory.getInstance(EmpDAOImpl.class).getAllCountByFlag("deptno",deptno,column,keyWord);
        map.put("allEmps",emps);
        map.put("allRecorders",x);
        return map;
    }
}
