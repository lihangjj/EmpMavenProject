package servlet;

import factory.ServiceFactory;
import service.back.IDeptServiceBack;
import service.back.IEmpServiceBack;
import service.back.IMemberServiceBack;
import service.back.impl.DeptServiceBackImpl;
import service.back.impl.EmpServiceBackImpl;
import service.back.impl.MemberServiceBackImpl;
import util.servlet.DispatcherServlet;

import javax.servlet.annotation.WebServlet;
import java.util.Set;

@WebServlet(name = "EmpServlet")
public class EmpServlet extends DispatcherServlet {
    protected IEmpServiceBack empServiceBack = ServiceFactory.getInstance(EmpServiceBackImpl.class);
    protected IDeptServiceBack deptServiceBack = ServiceFactory.getInstance(DeptServiceBackImpl.class);
    protected IMemberServiceBack memberServiceBack = ServiceFactory.getInstance(MemberServiceBackImpl.class);

    boolean verifyPermission(String actionFlag) {//验证登陆用户是否有该权限
        return ((Set<String>) request.getSession().getAttribute("actionFlagSet")).contains(actionFlag);
    }

    protected String getMid() {
        return (String) request.getSession().getAttribute("mid");
    }

    @Override
    public String getUploadDirectory() {
        return null;
    }
}
