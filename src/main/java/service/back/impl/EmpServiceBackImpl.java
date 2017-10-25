package service.back.impl;

import dao.impl.DeptDAOImpl;
import dao.impl.EmpDAOImpl;
import dao.impl.LevelDAOImpl;
import factory.DAOFactory;
import service.AbstractService;
import service.back.IEmpServiceBack;
import vo.Dept;
import vo.Emp;
import vo.Level;

import java.util.*;

public class EmpServiceBackImpl extends AbstractService implements IEmpServiceBack {
    @Override
    public boolean insert(Emp vo) throws Exception {
        Dept dept = DAOFactory.getInstance(DeptDAOImpl.class).findById(vo.getDeptno());
        Level level = DAOFactory.getInstance(LevelDAOImpl.class).findById(vo.getLid());
        if (dept.getMaxnum() > dept.getCurrnum()) {
            if (level.getLosal() < vo.getSal() && vo.getSal() <= level.getHisal()) {
                if (DAOFactory.getInstance(EmpDAOImpl.class).doCreate(vo)) {
                    int currnum = dept.getCurrnum();
                    dept.setCurrnum(++currnum);
                    return DAOFactory.getInstance(DeptDAOImpl.class).doUpdate(dept);
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> addPre() throws Exception {
        Map<String, Object> map = new HashMap<>();
        //1.得到有剩余名额的部门
        List<Dept> allDept = DAOFactory.getInstance(DeptDAOImpl.class).findAll();
        Iterator<Dept> iterator = allDept.iterator();
        while (iterator.hasNext()) {
            Dept dept = iterator.next();
            if (dept.getCurrnum() >= dept.getMaxnum()) {
                iterator.remove();
            }
        }
        map.put("allDept", allDept);
        //2.查询出全部工资等级

        List<Level> allLevel = DAOFactory.getInstance(LevelDAOImpl.class).findAll();
        map.put("allLevel", allLevel);
        return map;
    }

    @Override
    public boolean checkSal(int lid, double sal) throws Exception {
        Level level = DAOFactory.getInstance(LevelDAOImpl.class).findById(lid);
        return level.getLosal() < sal && sal <= level.getHisal();
    }

    @Override
    public Map<String, Object> listSplitByFlag(int currentPage, int lineSize, String column, String keyWord, int flag) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Emp> allEmps = DAOFactory.getInstance(EmpDAOImpl.class).findAllSplitByFlag("flag", flag, currentPage, lineSize, column, keyWord);
        map.put("allEmps", allEmps);
        map.put("allRecorders", DAOFactory.getInstance(EmpDAOImpl.class).getAllCountByFlag("flag", flag, column, keyWord));
        Map<Integer,Level> allLevels=new HashMap<>();
        for (Emp e : allEmps){
            allLevels.put(e.getEmpno(),DAOFactory.getInstance(LevelDAOImpl.class).findById(e.getLid()));
        }
        map.put("allLevels",allLevels);
        return map;
    }


}
