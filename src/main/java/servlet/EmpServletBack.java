package servlet;

import vo.Dept;
import vo.Emp;
import vo.Level;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "EmpServletBack", urlPatterns = "/pages/back/emp/EmpServletBack/*")
public class EmpServletBack extends EmpServlet {
    Emp emp = new Emp();

    public Emp getEmp() {
        return emp;
    }

    String add() {
        System.out.println(emp);
        emp.setMid(getMid());
        emp.setHiredate(new Date());
        emp.setFlag(1);//刚入职,是在职
        String photoName = createSingleFileName();
        emp.setPhoto(photoName);
        if (verifyPermission("emp:add")) {
            title = "员工";
            try {
                if (empServiceBack.insert(emp,smart.getRequest().getParameter("note"))) {
                    save(photoName);
                    setMsgAndUrl("vo.add.success.msg", "EmpServletBack.list.onduty");
                } else {
                    setMsgAndUrl("vo.add.failure.msg", "EmpServletBack.add");

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return forwardPath;
        } else {
            return "errors.page";

        }
    }

    void checkSal() {
        double sal = Double.parseDouble(request.getParameter("sal"));
        String lidd = request.getParameter("lid");
        try {
            if (lidd == null || "".equals(lidd)) {
                response.getWriter().print(false);
            } else {
                int lid = Integer.parseInt(lidd);
                response.getWriter().print(empServiceBack.checkSal(lid, sal));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void leave() {
        if (verifyPermission("emp:remove")) {
            String ids = request.getParameter("ids");
            try {
                response.getWriter().print(empServiceBack.leave(ids));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().print(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    String list() {
        if (verifyPermission("emp:list")) {
            column = "empno";
            int flag = Integer.parseInt(request.getParameter("flag"));
            switch (flag) {
                case 1:
                    handleSplit("EmpServletBack.list.onduty");
                    break;
                case 0:
                    handleSplit("EmpServletBack.list.leave");
                    break;

            }
            try {
                Map<String, Object> map = empServiceBack.listSplitByFlag(currentPage, lineSize, column, keyWord, flag);
                List<Emp> allEmps = (List<Emp>) map.get("allEmps");
                Map<Integer, Level> allLevels = (Map<Integer, Level>) map.get("allLevels");
                Map<Integer, Dept> allDepts = (Map<Integer, Dept>) map.get("allDepts");
                request.setAttribute("allLevels", allLevels);
                request.setAttribute("allDepts", allDepts);
                int allRecorders = (int) map.get("allRecorders");
                request.setAttribute("allEmps", allEmps);
                request.setAttribute("allRecorders", allRecorders);
                request.setAttribute("flag", flag);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "emp.list.page";
        } else {

            return "errors.page";
        }

    }

    String editPre() {
        if (verifyPermission("emp:edit")) {
            int empno = Integer.parseInt(request.getParameter("empno"));

            try {
                Map<String, Object> map = empServiceBack.editPre(empno);
                Emp emp = (Emp) map.get("emp");
                Level level = (Level) map.get("level");
                Dept dept = (Dept) map.get("dept");
                List<Dept> allDept = (List<Dept>) map.get("allDept");
                List<Level> allLevel = (List<Level>) map.get("allLevel");
                request.setAttribute("allDept", allDept);
                request.setAttribute("allLevel", allLevel);
                request.setAttribute("emp", emp);
                request.setAttribute("dept", dept);
                request.setAttribute("level", level);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "emp.edit.page";
        } else {

            return "errors.page";
        }
    }

    String edit() {

        if (verifyPermission("emp:edit")) {
            if (isUpload()) {
                emp.setPhoto(createSingleFileName());
            }
            System.out.println(emp);
            String note=smart.getRequest().getParameter("note");
            try {
                if (empServiceBack.edit(emp,note)) {
                    setMsgAndUrl("vo.edit.success.msg", "EmpServletBack.list.onduty");
                    emp=null;
                } else {
                    setMsgAndUrl("vo.edit.failure.msg", "EmpServletBack.list.onduty");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return forwardPath;
        } else {
            return "errors.page";
        }
    }

    String addPre() {

        if (verifyPermission("emp:add")) {
            try {
                List<Dept> allDept = (List<Dept>) empServiceBack.addPre().get("allDept");
                List<Level> allLevel = (List<Level>) empServiceBack.addPre().get("allLevel");
                request.setAttribute("allDept", allDept);
                request.setAttribute("allLevel", allLevel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "emp.add.page";
        } else {

            return "errors.page";
        }


    }

    @Override
    public String getUploadDirectory() {
        return "/upload/emp/";
    }
}
