package service.back.impl;

import dao.IDeptDAO;
import dao.impl.DeptDAOImpl;
import dao.impl.ElogDAOImpl;
import dao.impl.EmpDAOImpl;
import dao.impl.LevelDAOImpl;
import factory.DAOFactory;
import service.AbstractService;
import service.back.IEmpServiceBack;
import vo.Dept;
import vo.Elog;
import vo.Emp;
import vo.Level;

import java.text.SimpleDateFormat;
import java.util.*;

public class EmpServiceBackImpl extends AbstractService implements IEmpServiceBack {
    @Override
    public boolean insert(Emp emp, String note) throws Exception {
        Dept dept = DAOFactory.getInstance(DeptDAOImpl.class).findById(emp.getDeptno());
        Level level = DAOFactory.getInstance(LevelDAOImpl.class).findById(emp.getLid());
        if (dept.getMaxnum() > dept.getCurrnum()) {
            if (level.getLosal() < emp.getSal() && emp.getSal() <= level.getHisal()) {
                if (DAOFactory.getInstance(EmpDAOImpl.class).doCreate(emp)) {
                    int currnum = dept.getCurrnum();
                    dept.setCurrnum(++currnum);
                    if (DAOFactory.getInstance(DeptDAOImpl.class).doUpdate(dept)) {
                        Elog elog = new Elog();
                        elog.setEmpno(DAOFactory.getInstance(EmpDAOImpl.class).getLastInsertId());//设置emp编号
                        elog.setDeptno(emp.getDeptno());
                        elog.setMid(emp.getMid());
                        elog.setLid(emp.getLid());
                        elog.setJob(emp.getJob());
                        elog.setSal(emp.getSal());
                        elog.setComm(emp.getComm());
                        elog.setFlag(emp.getFlag());
                        elog.setSflag(0);
                        elog.setNote(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + ":刚入职" + note);
                        return DAOFactory.getInstance(ElogDAOImpl.class).doCreate(elog);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> editPre(int empno) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Emp emp = DAOFactory.getInstance(EmpDAOImpl.class).findById(empno);
        Level level = DAOFactory.getInstance(LevelDAOImpl.class).findById(emp.getLid());
        Dept dept = DAOFactory.getInstance(DeptDAOImpl.class).findById(emp.getDeptno());
        map.put("emp", emp);
        map.put("dept", dept);
        map.put("level", level);
        List<Dept> allDept = DAOFactory.getInstance(DeptDAOImpl.class).findAll();
        Iterator<Dept> iterator = allDept.iterator();
        while (iterator.hasNext()) {
            Dept d = iterator.next();
            if (!d.getDeptno().equals(dept.getDeptno())) {
                if (d.getCurrnum() >= d.getMaxnum()) {
                    iterator.remove();
                }
            }
        }
        map.put("allDept", allDept);
        List<Level> allLevel = DAOFactory.getInstance(LevelDAOImpl.class).findAll();
        map.put("allLevel", allLevel);

        return map;
    }

    @Override
    public boolean edit(Emp emp, String note) throws Exception {
        Elog elog = new Elog();
        elog.setEmpno(emp.getEmpno());
        elog.setDeptno(emp.getDeptno());
        elog.setMid(emp.getMid());
        elog.setLid(emp.getLid());
        elog.setJob(emp.getJob());
        elog.setSal(emp.getSal());
        elog.setComm(emp.getComm());
        Emp oldEmp = DAOFactory.getInstance(EmpDAOImpl.class).findById(emp.getEmpno());
        int sflag = oldEmp.getSal() < emp.getSal() ? 1 : oldEmp.getSal().equals(emp.getSal()) ? 3 : 2;
        elog.setFlag(emp.getFlag());
        elog.setSflag(sflag);
        String status = sflag == 1 ? "工资增加操作" : sflag == 2 ? "工资减少操作" : "工资未改动";
        elog.setNote(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + ":" + status + ":" + note);
        if (DAOFactory.getInstance(EmpDAOImpl.class).doUpdate(emp)) {
            return DAOFactory.getInstance(ElogDAOImpl.class).doCreate(elog);
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
        Map<Integer, Level> allLevels = new HashMap<>();
        Map<Integer, Dept> allDepts = new HashMap<>();
        for (Emp e : allEmps) {
            allLevels.put(e.getEmpno(), DAOFactory.getInstance(LevelDAOImpl.class).findById(e.getLid()));
            allDepts.put(e.getEmpno(), DAOFactory.getInstance(DeptDAOImpl.class).findById(e.getDeptno()));
        }
        map.put("allLevels", allLevels);
        map.put("allDepts", allDepts);
        return map;
    }

    @Override
    public Boolean leave(String ids) throws Exception {
        if (DAOFactory.getInstance(EmpDAOImpl.class).leave(ids)) {//如果批量离职成功
            String[] id = ids.split("\\|");
            int count = 0;
            IDeptDAO iDeptDAO = DAOFactory.getInstance(DeptDAOImpl.class);
            for (String x : id) {
                Emp emp = DAOFactory.getInstance(EmpDAOImpl.class).findById(Integer.valueOf(x));
                Dept d = iDeptDAO.findById(emp.getDeptno());
                d.setCurrnum(d.getCurrnum() - 1);
                if (iDeptDAO.doUpdate(d)) {
                    emp.setFlag(0);
                    Elog elog = new Elog();
                    elog.setEmpno(emp.getEmpno());
                    elog.setDeptno(emp.getDeptno());
                    elog.setMid(emp.getMid());
                    elog.setLid(emp.getLid());
                    elog.setJob(emp.getJob());
                    elog.setSal(emp.getSal());
                    elog.setComm(emp.getComm());
                    elog.setFlag(emp.getFlag());
                    elog.setSflag(3);
                    String status = "雇员离职操作";
                    elog.setNote(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + ":" + status + ":");
                    if (DAOFactory.getInstance(ElogDAOImpl.class).doCreate(elog)) {
                        count++;
                    }
                }
            }
            return count == id.length;

        }
        return false;
    }


}
