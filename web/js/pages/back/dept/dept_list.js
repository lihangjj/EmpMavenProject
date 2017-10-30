var currentPage = 1;
var lineSize = 6;
var pageSize = 1;
var keyWord = "";
var dno;
var column = "empno";
var url = "/pages/back/dept/DeptServletBack/listEmpsByDeptno";
$(function () {
    $("[id*=editBtn-]").each(function () {
        var deptno = this.id.split("-")[1];
        $(this).on("click", function () {
            var maxnum = $("#maxnum-" + deptno).val();
            console.log("***** deptno = " + deptno + "，人数上限：" + maxnum);
            var max = $(this).prev().val();
            var deptno = $(this).prev().attr("id");
            $.post("/pages/back/dept/DeptServletBack/update", {
                "max": max,
                "deptno": deptno
            }, function (data) {
                if (data == "true") {
                    operateAlert(true, "部门人数上限修改成功！", "部门人数上限修改失败！");
                } else {
                    operateAlert(false, "部门人数上限修改成功！", "部门人数上限修改失败！");
                }

            }, "text");

        });
    });

    $("[id*='showBtn-']").each(function () {	// 取得显示按钮
        var deptno = this.id.split("-")[1];	// 别忘了dom本身的一些方法,如this.id，还有什么this.value之类的;分离出id信息
        $(this).on("click", function () {
            dno = deptno;
            console.log("deptno = " + deptno);
            $("#deptTitleSpan").text($("#dname-" + deptno).text());
            // 编写Ajax异步更新操作，读取所有的权限信息
            currentPage=1;
            postDeptServletBack(deptno);
            $("#empInfo").modal("toggle");
        });
    });



    $("#pre").on("click", function () {
        if (currentPage>1){
            currentPage--;
        }
        alert(currentPage+"当前页");
        alert(pageSize+"总页数");
        if (currentPage == pageSize) {
            $("#next").attr("disabled", "true");
        } else {
            $("#next").attr("disabled",false);
        }
        if (currentPage == 1) {
            $("#pre").attr("disabled", "true");
        } else {
            $("#pre").attr("disabled",false);
        }
        postDeptServletBack(dno);

    });
    $("#next").on("click", function () {

        if (currentPage<pageSize){
            currentPage++;
        }
        alert(currentPage+"当前页");
        alert(pageSize+"总页数");
        if (currentPage == pageSize) {
            $("#next").attr("disabled", "true");
        } else {
            $("#next").attr("disabled", false);
        }
        if (currentPage == 1) {
            $("#pre").attr("disabled", "true");
        } else {
            $("#pre").attr("disabled", false);
        }
        postDeptServletBack(dno);

    })
});

function postDeptServletBack(deptno) {
    $.post(url, {
        "deptno": deptno,
        "currentPage": currentPage,
        "lineSize": lineSize,
        "keyWord": keyWord,
        "column": column
    }, function (data) {
        if (data.result == true) {//这里要注意了，你存 的字符串就是字符串，如果是Boolean就是boolean类型
            $("#actionsTab tr:gt(0)").each(function () {
                $(this).remove()
            });
            pageSize=data.pageSize;
            for (var x = 0; x < data.allEmps.length; x++) {
                var emp = data.allEmps[x];
                var date = emp.hiredate.year + 1900 + "-" + emp.hiredate.month + "-" + emp.hiredate.date;
                var tr = $("<tr>\n" +
                    "\t\t\t\t\t\t\t\t\t\t<td class=\"text-center\"><img src=\"upload/emp/" + emp.photo + "\" style=\"width:30px;\"></td> \n" +
                    "\t\t\t\t\t\t\t\t\t\t<td class=\"text-center\">" + emp.ename + "</td>\n" +
                    "\t\t\t\t\t\t\t\t\t\t<td class=\"text-center\">" + emp.job + "</td>\n" +
                    "\t\t\t\t\t\t\t\t\t\t<td class=\"text-center\">" + emp.lid + "</td>\n" +
                    "\t\t\t\t\t\t\t\t\t\t<td class=\"text-center\">" + emp.sal + "</td>\n" +
                    "\t\t\t\t\t\t\t\t\t\t<td class=\"text-center\">" + emp.comm + "</td>\n" +
                    "\t\t\t\t\t\t\t\t\t\t<td class=\"text-center\">" + date + "</td>\n" +
                    "\t\t\t\t\t\t\t\t\t</tr>");
                $("#actionsTab").append(tr);
            }
        }
    }, "json");
}