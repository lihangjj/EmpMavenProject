<%
    request.setCharacterEncoding("utf-8");
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";

%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="<%=basePath%>">
    <title>雇员信息系统</title>
    <jsp:include page="include_javascript.jsp"></jsp:include>
</head>
<body>


<%
    int pageSize;
    int allRecorders;
    int currentPage;
    int lineSize;
    String url;
    String keyWord = "";
    String column = "empon";
    String parameterName = null;
    String parameterValue = null;
    keyWord = request.getAttribute("keyWord") == null ? "" : (String) request.getAttribute("keyWord");
    column = request.getAttribute("column") == null ? null : (String) request.getAttribute("column");
        url = request.getAttribute("url") == null ? null : (String) request.getAttribute("url");
    if (url != null && url.contains("?")) {
        String parameterNameAndValue = url.substring(url.lastIndexOf("?") + 1);
        String nameValue[] = parameterNameAndValue.split("=");
        parameterName = nameValue[0];
        parameterValue = nameValue[1];
    } else {
        url = url + "?";
    }
    currentPage = request.getAttribute("currentPage") == null ? 1 : (int) request.getAttribute("currentPage");
    lineSize = request.getAttribute("lineSize") == null ? 10 : (int) request.getAttribute("lineSize");
    allRecorders = request.getAttribute("allRecorders") == null ? 0 : (int) request.getAttribute("allRecorders");
    pageSize = (allRecorders + lineSize - 1) / lineSize;
    if (pageSize < currentPage) {
        currentPage = 1;
    }

%>
<div class="row">
    <div class="col-md-2 form-inline pull-right">
        <span class="splitFont">每页显示<select id="showLine" class="form-control" style="margin: 0px"
                                            onchange="goSplit('<%=url%>',1,this.value,'<%=column%>','<%=keyWord%>','<%=parameterName%>',<%=parameterValue%>)">

                <%
                    for (int x = 1; x < 21; x++) {
                %>
                <option <%=lineSize == x ? "selected" : ""%>><%=x%></option>
                <%
                    }
                %>
            </select>行</span></div>
    <div class="col-md-12 pull-right" style="width: auto">
        <ul class="pagination" style="margin: 0px;">
            <li class=<%=currentPage == 1 ? "active" : ""%>><a style="cursor:pointer"
                                                               onclick="goSplit('<%=url%>',1,<%=lineSize%>,'<%=column%>','<%=keyWord%>','<%=parameterName%>',<%=parameterValue%>)">1</a>
            </li>

            <%
                if (pageSize < 10) {
                    for (int x = 2; x < pageSize; x++) {
            %>
            <li class=<%=currentPage == x ? "active" : ""%>><a style="cursor:pointer"
                                                               onclick="goSplit('<%=url%>',<%=x%>,<%=lineSize%>,'<%=column%>','<%=keyWord%>','<%=parameterName%>',<%=parameterValue%>)"><%=x%>
            </a></li>
            <%
                }
            } else {
                if (currentPage < 6) {
                    for (int x = 2; x < currentPage + 4; x++) {
            %>
            <li class=<%=currentPage == x ? "active" : ""%>><a style="cursor:pointer"
                                                               onclick="goSplit('<%=url%>',<%=x%>,<%=lineSize%>,'<%=column%>','<%=keyWord%>','<%=parameterName%>',<%=parameterValue%>)"><%=x%>
            </a></li>

            <%
                }
            %>
            <li><a style="cursor:pointer">...</a></li>
            <%

            } else {
            %>
            <li><a style="cursor:pointer">...</a></li>
            <%
                if (currentPage + 4 < pageSize) {
                    for (int x = currentPage - 3; x < currentPage + 4; x++) {
            %>
            <li class=<%=currentPage == x ? "active" : ""%>><a style="cursor:pointer"
                                                               onclick="goSplit('<%=url%>',<%=x%>,<%=lineSize%>,'<%=column%>','<%=keyWord%>','<%=parameterName%>',<%=parameterValue%>)"><%=x%>
            </a></li>
            <%

                }
            %>
            <li><a style="cursor:pointer">...</a></li>
            <%
            } else {
                for (int x = currentPage - 3; x < pageSize; x++) {
            %>
            <li class=<%=currentPage == x ? "active" : ""%>><a style="cursor:pointer"
                                                               onclick="goSplit('<%=url%>',<%=x%>,<%=lineSize%>,'<%=column%>','<%=keyWord%>','<%=parameterName%>',<%=parameterValue%>)"><%=x%>
            </a></li>
            <%
                        }
                    }
                }
            %>
            <%
                }
            %>
            <%
                //如果页面总数小于2页，就只显示第一页
                if (pageSize < 2) {
                } else {
            %>
            <li class=<%=currentPage == pageSize ? "active" : ""%>>
                <a style="cursor:pointer"
                   onclick="goSplit('<%=url%>',<%=pageSize%>,<%=lineSize%>,'<%=column%>','<%=keyWord%>','<%=parameterName%>',<%=parameterValue%>)"><%=pageSize%>
                </a></li>
            <%

                }
            %>

        </ul>
    </div>
</div>
<script type="text/javascript">
    function goSplit(url, currentPage, lineSize, column, keyWord, parameterName, parameterValue) {
        window.location = url + "&currentPage=" + currentPage + "&lineSize=" + lineSize + "&column=" + column + "&keyWord=" + keyWord + "&" + parameterName + "=" + parameterValue;
    }
</script>

<style type="text/css">
    .splitFont {
        font-size: 18px;
        font-weight: bold;
        margin: 0px;
    }
</style>
</body>
</html>
