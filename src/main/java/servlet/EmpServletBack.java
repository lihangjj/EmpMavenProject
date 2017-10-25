package servlet;

import vo.Dept;
import vo.Emp;
import vo.Level;

import javax.servlet.annotation.WebServlet;
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
        emp.setMid(getMid());
        emp.setHiredate(new Date());
        emp.setFlag(1);//刚入职
        String photoName = createSingleFileName();
        emp.setPhoto(photoName);
        System.out.println(emp);
        System.out.println(photoName);
        if (verifyPermission("emp:add")) {
            title = "员工";
            try {
                if (empServiceBack.insert(emp)) {
                    save(photoName);
                    setMsgAndUrl("vo.add.success.msg", "EmpServletBack.list");
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

    String list() {
        if (verifyPermission("emp:list")) {
            column="empno";
            handleSplit("EmpServletBack.list");
            try {
                Map<String, Object> map = empServiceBack.listSplitByFlag(currentPage, lineSize, column, keyWord, 1);
                List<Emp> allEmps = (List<Emp>) map.get("allEmps");
                Map<Integer,Level> allLevels= (Map<Integer, Level>) map.get("allLevels");
                request.setAttribute("allLevels",allLevels);
                int allRecorders = (int) map.get("allRecorders");
                request.setAttribute("allEmps", allEmps);
                request.setAttribute("allRecorders", allRecorders);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "emp.list.page";
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
        return "/upload/emp";
    }
}
