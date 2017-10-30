package dao;

import vo.Emp;

import java.util.List;

public interface IEmpDAO extends IDAO<Integer, Emp> {
    boolean leave(String ids)throws Exception;
    List<Emp> findEmpsByDept(int deptno) throws Exception;
}
