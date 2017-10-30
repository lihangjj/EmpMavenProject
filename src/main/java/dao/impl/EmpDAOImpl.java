package dao.impl;

import dao.AbstractDAOImpl;
import dao.IEmpDAO;
import vo.Emp;

import java.util.ArrayList;
import java.util.List;

public class EmpDAOImpl extends AbstractDAOImpl<Integer, Emp> implements IEmpDAO {
    public EmpDAOImpl() throws Exception {
        super(Emp.class);
    }


    @Override
    public boolean leave(String ids) throws Exception {
        String id[] = ids.split("\\|");
        sql = "update " + tableName + " set flag=0 where empno =?";
        pre = conn.prepareStatement(sql);
        for (String x : id) {
            pre.setInt(1, Integer.valueOf(x));
            pre.addBatch();
        }
        int[] count = pre.executeBatch();
        return count.length == id.length;
    }

    @Override
    public List<Emp> findEmpsByDept(int deptno) throws Exception {
        bufSelectAll();
        List<Emp> list = new ArrayList<>();
        buf.append(" where deptno=" + deptno);
        System.out.println(buf.toString());
        pre = conn.prepareStatement(buf.toString());
        res = pre.executeQuery();
        while (res.next()) {
            Emp emp = new Emp();
            voSet(emp);
            list.add(emp);

        }
        return list;
    }

}
