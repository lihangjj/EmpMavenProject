package service.back;

import vo.Emp;

import java.util.Map;

public interface IEmpServiceBack {
    boolean insert(Emp vo,String note) throws Exception;
    Map<String,Object> addPre()throws Exception;
    boolean checkSal(int lid,double sal)throws Exception;
    Map<String,Object> listSplitByFlag(int currentPage,int lineSize,String column,String keyWord,int flag)throws Exception;
    Boolean leave(String ids)throws Exception;
    Map<String,Object>editPre(int empno)throws Exception;
    boolean edit(Emp emp,String note)throws Exception;

}
