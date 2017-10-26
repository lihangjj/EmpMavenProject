package dao.impl;

import dao.AbstractDAOImpl;
import dao.IEmpDAO;
import vo.Emp;

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
}
