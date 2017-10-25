package service.back;

import vo.Emp;

import java.util.Map;

public interface IEmpServiceBack {
    boolean insert(Emp vo) throws Exception;
    Map<String,Object> addPre()throws Exception;
    boolean checkSal(int lid,double sal)throws Exception;
    Map<String,Object> listSplitByFlag(int currentPage,int lineSize,String column,String keyWord,int flag)throws Exception;
}
