import factory.ServiceFactory;
import net.sf.json.JSONObject;
import service.back.IDeptServiceBack;
import service.back.impl.DeptServiceBackImpl;
import vo.Emp;

import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        IDeptServiceBack ideptServiceBack= ServiceFactory.getInstance(DeptServiceBackImpl.class);
        List<Emp> emps= ideptServiceBack.findEmpsByDept(2);
        Emp emp2=emps.get(0);
        Emp emp1=new Emp();
        emp1.setFlag(1);
        emp1.setMid("jhah");
        System.out.println(emp1);
        System.out.println(emp2);
        JSONObject jsonObject=JSONObject.fromObject(emp1);
        JSONObject jsonObject2=JSONObject.fromObject(emp2);
        System.out.println(jsonObject);
        System.out.println(jsonObject2);

    }

}
