package dao;

import vo.Emp;

public interface IEmpDAO extends IDAO<Integer, Emp> {
    boolean leave(String ids)throws Exception;
}
