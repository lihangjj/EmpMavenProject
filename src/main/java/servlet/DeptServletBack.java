package servlet;

import net.sf.json.JSONObject;
import vo.Dept;
import vo.Emp;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DeptServletBack", urlPatterns = "/pages/back/dept/DeptServletBack/*")
public class DeptServletBack extends EmpServlet {
    public Dept dept = new Dept();

    public Dept getDept() {
        return dept;
    }

    String list() {
        if (verifyPermission("dept:list")) {
            try {
                List<Dept> allDepts = deptServiceBack.list();
                request.setAttribute("allDepts", allDepts);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "dept.list.page";
        } else {

            return "errors.page";
        }

    }

    void listEmpsByDeptno() {
        JSONObject jsonObject = new JSONObject();

        if (verifyPermission("dept:edit")) {
            String deptno = request.getParameter("deptno");
            try {
                column="empno";
                handleSplit("dept.list.spilt.page");
                Map<String,Object>map = deptServiceBack.findEmpByDeptAndSplit(Integer.valueOf(deptno), currentPage, lineSize, column, keyWord);
                List<Emp> allEmps= (List<Emp>) map.get("allEmps");
                int allRecorders= (int) map.get("allRecorders");

                jsonObject.put("allEmps", allEmps);
                jsonObject.put("result", true);
                jsonObject.put("url", getPath("dept.list.spilt.page"));
                jsonObject.put("keyWord", "");
                jsonObject.put("column", "empno");
                jsonObject.put("currentPage", currentPage);
                jsonObject.put("lineSize", lineSize);
                jsonObject.put("deptno", deptno);
                jsonObject.put("allRecorders", allRecorders);
                int pageSize= (int) Math.ceil(allRecorders/lineSize);
                jsonObject.put("pageSize", pageSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            jsonObject.put("result", false);
        }
        try {
            System.out.println(jsonObject);
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void update() throws Exception {
        if (verifyPermission("dept:edit")) {
            String max = request.getParameter("max");
            String deptno = request.getParameter("deptno");
            if (deptServiceBack.updateMax(Integer.valueOf(deptno), Integer.valueOf(max))) {
                response.getWriter().print(true);
            }
        } else {
            response.getWriter().print(false);
        }
    }

    @Override
    public String getUploadDirectory() {
        return null;
    }
}
