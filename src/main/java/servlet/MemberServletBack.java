package servlet;

import util.validate.EncryptUtil;
import vo.Member;

import javax.servlet.annotation.WebServlet;
import java.util.List;


@WebServlet(name = "MemberServletBack", urlPatterns = "/pages/back/member/MemberServletBack/*")
public class MemberServletBack extends EmpServlet {
    public Member member = new Member();

    public Member getMember() {
        return member;
    }

    String list() {
        if (verifyPermission("member:list")) {
            try {
                List<Member> allMembers = memberServiceBack.list();
                request.setAttribute("allMembers", allMembers);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "member.list.page";
        } else {
            return "errors.page";
        }

    }

    String add() {
        member.setPassword(EncryptUtil.getPwd(member.getPassword()));
        String[] rid=request.getParameterValues("rid");
        title="用户";
        try {
            if (memberServiceBack.add(member,rid)){
                setMsgAndUrl("vo.add.success.msg","MemberServletBack.list.page");
            }else {
                setMsgAndUrl("vo.add.failure.msg","MemberServletBack.list.page");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forwardPath;
    }

    void checkMid() {
        String mid = request.getParameter("mid");
        try {
            if (memberServiceBack.findById(mid)) {//找到了，就表示是true
                response.getWriter().print(false);
            } else {
                response.getWriter().print(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
