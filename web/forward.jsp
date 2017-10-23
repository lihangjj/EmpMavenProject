<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <jsp:include page="/pages/plugins/include_javascript.jsp"/>
    <link rel="stylesheet" type="text/css" href="css/login.css"/>
    <script type="text/javascript" src="jquery/jquery.backstretch.min.js"></script>
</head>
<body>
<div class="top-content">
    <div class="inner-bgx">
        <div class="container">
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">
                    <div class="form-top">
                        <div class="form-top-left">
                            <h3>企业人事管理系统</h3>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-lock"><img src="images/login-lock.png"></i>
                        </div>
                    </div>
                    <div class="form-bottom" style="background: white;">
                        <div>
                            <div>${msg}</div>
                            <div><span id="mytime">3</span>秒后跳转到<a href="<%=basePath%>${url}">首页</a>！</div>
                        </div>
                        <script type="text/javascript">
                            $(function () {
                                var time = 2;
                                var intervalFn = setInterval(function () {
                                    if (time< 1) {
                                        clearInterval(intervalFn);
                                        window.location=$("#mytime+a").attr("href");
                                    }
                                    $("#mytime").text(time);
                                    time--;
                                }, 1000)
                            });
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
