<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/pages/plugins/include_static_head.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <%
        String addEmpUrl = basePath + "pages/back/emp/EmpServletBack/addPre";
        String editEmpUrl = basePath + "pages/back/emp/EmpServletBack/editPre";
    %>
    <jsp:include page="/pages/plugins/include_javascript_head.jsp"/>
    <script type="text/javascript" src="js/pages/back/emp/emp_list.js"></script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- 导入头部标题栏内容 -->
    <jsp:include page="/pages/plugins/include_title_head.jsp"/>
    <!-- 导入左边菜单项 -->
    <jsp:include page="/pages/plugins/include_menu_item.jsp">
        <jsp:param name="role" value="emp"/>
        <jsp:param name="action" value="emp:list"/>
    </jsp:include>
    <div class="content-wrapper">
        <!-- 此处编写需要显示的页面 -->
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <!-- /.box-header -->
                    <div class="box-body table-responsive no-padding">
                        <div class="panel panel-info">
                            <div class="panel-heading">
                                <strong><span class="glyphicon glyphicon-user"></span>&nbsp;雇员信息列表</strong>
                            </div>
                            <div class="panel-body" style="height : 95%;">
                                <jsp:include page="/pages/plugins/search.jsp"/>
                                <table class="table table-hover">
                                    <tr>
                                        <th  class="text-center"><input type="checkbox" id="selall"></th>
                                        <th  class="text-center">状态</th>
                                        <th  class="text-center">编号</th>
                                        <th class="text-center">姓名</th>
                                        <th class="text-center">照片</th>
                                        <th  class="text-center">级别</th>
                                        <th class="text-center">职位</th>
                                        <th  class="text-center">部门</th>
                                        <th  class="text-center">基本工资</th>
                                        <th  class="text-center">佣金</th>
                                        <th class="text-center">雇佣日期</th>
                                        <th  class="text-center">操作</th>
                                    </tr>
                                    <c:forEach items="${allEmps}" var="emp">
                                        <tr id="${emp.empno}" >

                                            <td class="text-center"><input type="checkbox" id="empno" name="empno"
                                                                           value="${emp.empno}"></td>
                                            <td class="text-center"><span class="${emp.flag==1?"text-success":"text-danger"}">${emp.flag==1?"在职":"离职"}</span></td>
                                            <td class="text-center">${emp.empno}</td>
                                            <td class="text-center">${emp.ename}</td>
                                            <td class="text-center"><img src="/upload/emp/${emp.photo}" width="50px"></td>
                                            <td class="text-center">${allLevels[emp.empno].title}（${allLevels[emp.empno].flag}）</td>
                                            <td class="text-center">${emp.job}</td>
                                            <td class="text-center">${allDepts[emp.empno].dname}</td>
                                            <td class="text-center">￥${emp.sal}/月</td>
                                            <td class="text-center">￥${emp.comm}/月</td>
                                            <td class="text-center">${emp.hiredate}</td>
                                            <td class="text-center"><a href="<%=editEmpUrl%>?empno=${emp.empno}"
                                                                       class="btn btn-xs btn-primary"><span
                                                    class="glyphicon glyphicon-edit"></span>&nbsp;编辑</a></td>
                                        </tr>

                                    </c:forEach>

                                </table>
                                <a href="<%=addEmpUrl%>" id="inBtn" class="btn btn-lg btn-primary"><span
                                        class="glyphicon glyphicon-plus-sign"></span>&nbsp;雇员入职</a>
                                <button id="outBtn" class="btn btn-lg btn-danger" ${flag==1?"":"disabled"}><span
                                        class="glyphicon glyphicon-remove"></span>&nbsp;雇员离职
                                </button>
                                <jsp:include page="/pages/plugins/split_bar.jsp"/>
                                <jsp:include page="/pages/plugins/include_alert.jsp"/>
                            </div>
                        </div>
                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- /.box -->
            </div>
        </div>
    </div>
    <!-- 导入公司尾部认证信息 -->
    <jsp:include page="/pages/plugins/include_title_foot.jsp"/>
    <!-- 导入右边工具设置栏 -->
    <jsp:include page="/pages/plugins/include_menu_sidebar.jsp"/>
    <div class="control-sidebar-bg"></div>
</div>
<jsp:include page="/pages/plugins/include_javascript_foot.jsp"/>
</body>
</html>
