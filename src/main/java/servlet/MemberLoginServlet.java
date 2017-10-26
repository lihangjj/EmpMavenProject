package servlet;

import factory.ServiceFactory;
import service.back.IMemberServiceBack;
import service.back.impl.MemberServiceBackImpl;
import util.servlet.DispatcherServlet;
import util.validate.EncryptUtil;
import vo.Action;
import vo.Member;
import vo.Role;

import javax.servlet.annotation.WebServlet;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "MemberLoginServlet", urlPatterns = "/MemberLoginServlet/*")
public class MemberLoginServlet extends DispatcherServlet {
    Member member = new Member();

    public Member getMember() {
        return member;
    }

    String login() {
        try {

            IMemberServiceBack memberServiceBack = ServiceFactory.getInstance(MemberServiceBackImpl.class);
            member.setPassword(EncryptUtil.getPwd(member.getPassword()));
            Map<String, Object> map = memberServiceBack.login(member);
            Boolean flag = (Boolean) map.get("flag");
            String mid = (String) map.get("mid");

            if (flag == true) {
                String name = (String) map.get("name");
                int sflag = (int) map.get("sflag");
                Set<Role> allRoles = (Set<Role>) map.get("allRoles");
                Set<Action> allActions = (Set<Action>) map.get("allActions");
                Set<String> actionFlagSet= (Set<String>) map.get("actionFlagSet");
                request.getSession().setAttribute("name", name);
                request.getSession().setAttribute("mid", mid);
                request.getSession().setAttribute("sflag", sflag);
                request.getSession().setAttribute("allRoles", allRoles);
                request.getSession().setAttribute("allActions", allActions);
                request.getSession().setAttribute("actionFlagSet", actionFlagSet);
                setMsgAndUrl("login.success.msg", "index.page");
            } else {
                setMsgAndUrl("login.failure.msg", "login.page");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return forwardPath;

    }

    String logout() {
        getSession().invalidate();
        setMsgAndUrl("logout.success.msg", "login.page");
        return forwardPath;
    }

    @Override
    public String getUploadDirectory() {
        return null;
    }
}
