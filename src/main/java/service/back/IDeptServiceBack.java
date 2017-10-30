package service.back;

import vo.Dept;
import vo.Emp;

import java.util.List;
import java.util.Map;

public interface IDeptServiceBack {
    List<Dept> list() throws Exception;

    boolean updateMax(int deptno, int max) throws Exception;

    List<Emp> findEmpsByDept(int deptno) throws Exception;

    Map<String,Object> findEmpByDeptAndSplit(int deptno, int curentPage, int lineSize, String column, String keyWord) throws Exception;
}
