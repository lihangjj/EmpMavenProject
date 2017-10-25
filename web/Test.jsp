<%--
  Created by IntelliJ IDEA.
  User: lh
  Date: 2017/10/25
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
<head>
    <title>企业人事管理系统（HR）</title>
    <link rel="icon" href="images/mldn.ico" type="image/x-icon" />
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <script type="text/javascript" src="/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/js/mldn.js"></script>
    <script type="text/javascript" src="/jquery/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="/jquery/validate/additional-methods.min.js"></script>
    <script type="text/javascript" src="/jquery/validate/Message_zh_CN.js"></script>
    <script type="text/javascript" src="/bootstrap/js/bootstrap.min.js"></script>
    <link type="text/css" rel="stylesheet" href="/bootstrap/css/bootstrap.min.css">

</head>
<body>
        <div class="dropdown">
            <span class="glyphicon glyphicon-edit">测试</span>
            <a class="dropdown-toggle glyphicon glyphicon-edit" data-toggle="dropdown" style="cursor: pointer;text-decoration: none">雇员管理啊</a>
            <ul class=" dropdown-menu">
                <li class="active"><a href="pages/back/emp/EmpServletBack/addPre"><i
                        class="fa fa-circle-o"></i> 雇员入职</a></li>
                <li class="active"><a href="pages/back/emp/EmpServletBack/list?flag=1"><i
                        class="fa fa-circle-o"></i> 在职雇员列表</a></li>
                <li class="active"><a href="pages/back/emp/EmpServletBack/list?flag=0"><i
                        class="fa fa-circle-o"></i> 离职雇员列表</a></li>
            </ul>
        </div>
</body>
</html>
