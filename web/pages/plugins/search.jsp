<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/5/19 0019
  Time: 下午 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf-8");
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";

%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <script type="text/javascript" src="/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.min.css"/>
</head>
<body>
<%
    int allRecorders;
    int currentPage;
    int lineSize;
    String url;
    String keyWord = "";
    String column = "empon";
    String parameterName=null;
    String parameterValue=null;
    keyWord = request.getAttribute("keyWord") == null ? "" : (String) request.getAttribute("keyWord");
    column = request.getAttribute("column") == null ? "empon" : (String) request.getAttribute("column");
    url = request.getAttribute("url") == null ? null : (String) request.getAttribute("url");
    if (url!=null&&url.contains("?")){
        String parameterNameAndValue=url.substring(url.lastIndexOf("?")+1);
        String nameValue[]=parameterNameAndValue.split("=");
        parameterName=nameValue[0];
        parameterValue=nameValue[1];
    }
    currentPage = request.getAttribute("currentPage") == null ? 1 : (int) request.getAttribute("currentPage");
    lineSize = request.getAttribute("lineSize") == null ? 10 : (int) request.getAttribute("lineSize");
    allRecorders = request.getAttribute("allRecorders") == null ? 0 : (int) request.getAttribute("allRecorders");
    String columnData = request.getAttribute("columnData") == null ? "雇员编号:empno|雇员名字:ename|雇员职位:job|雇员工资:sal" : (String) request.getAttribute("columnData");
    String items1[] = columnData.split("\\|");
    String items2[];
%>
<form action="<%=url%>">

    <div style="padding: 10px" class="form-inline">
        <span class="searchFont">请选择查询方式：</span><select class="form-control" id="column" name="column">

        <%
            for (int x = 0; x < items1.length; x++) {
                items2 = items1[x].split(":");
        %>
        <option value="<%=items2[1]%>" <%=column.equals(items2[1]) ? "selected" : ""%>><%=items2[0]%>
        </option>
        <%
            }
        %>
    </select><span class="searchFont">&nbsp请输入查询关键字</span><input class="form-control" style="font-weight: bold" type="text" id="keyWord" name="keyWord" value="<%=keyWord%>">
        <input class="btn btn-primary" type="submit" value="检索"><span class="searchFont">&nbsp一共查询到<%=allRecorders%>
                条数据</span>
        <input type="hidden" value="<%=currentPage%>" name="currentPage">
        <input type="hidden" value="<%=lineSize%>" name="lineSize">
        <input type="hidden" value="<%=column%>" name="column">
        <input type="hidden" value="<%=parameterValue%>" name="<%=parameterName%>">
    </div>
</form>
<style type="text/css">
    .searchFont{
        font-size: 18px;
        font-weight: bold;
    }
</style>

</body>
</html>
