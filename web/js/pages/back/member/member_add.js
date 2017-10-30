$(function () {
    $("#myform").validate({
        debug: true,	// 表单的提交按钮失效了
        submitHandler: function (form) {	// 对于表单提交按钮的控制处理
            form.submit();	// 提交表单
            // 如果你的提交需要使用ajax处理，那么就将“submit()”函数取消掉，而后进行ajax调用即可
        },
        highlight: function (element, errorClass) {
            var id = element.id;	// 取得组件ID
            if (element.id.indexOf(".")) {	// 表示当前的ID需要进行转义的处理
                id = element.id.replace(".", "\\.");	// 修改新的id
            }
            id = id + "Div";	// 现在的组件ID
            $("#" + id).attr("class", "form-group has-error");
        },
        unhighlight: function (element, errorClass) {
            var id = element.id;	// 取得组件ID
            if (element.id.indexOf(".") != -1) {	// 表示当前的ID需要进行转义的处理
                id = element.id.replace(".", "\\.");	// 修改新的id
            }
            id = id + "Div";	// 现在的组件ID
            $("#" + id).attr("class", "form-group has-success");
        },
        errorPlacement: function (error, element) {
            var id = $(element).attr("id");
            if (id.indexOf(".") != -1) {
                id = id.replace(".", "\\.");
            }
            id = id + "Msg";
            $("#" + id).empty();	// 先清除之前的所有内容
            $("#" + id).append("<span class='text-danger glyphicon glyphicon glyphicon-remove'></span>&nbsp;");
            $("#" + id).append(error);
        },
        errorClass: "text-danger",
        messages: {"member.mid": "此用户已经被注册"
        },

        success: function (error, element) {
            var id = $(element).attr("id");
            if (id.indexOf(".") != -1) {
                id = id.replace(".", "\\.");
            }
            id = id + "Msg";
            $("#" + id).empty();	// 先清除之前的所有内容
            $("#" + id).append("<span class='text-success glyphicon glyphicon-ok'></span>");
        },
        rules: {	// 针对于每一个表单实现的验证控制处理
            "member.mid": {
                required: true,
                remote: {	// 需要进行远程交互验证
                    url: "/pages/back/member/MemberServletBack/checkMid",
                    type: "post",
                    dataType: "text",
                    data: {
                        "mid": function () {
                            return $("#member\\.mid").val();
                        }
                    },
                    dataFilter: function (data, type) {
                        return data == "true";
                    }
                }
            },
            "member.name": {
                required: true
            },
            "member.password": {
                required: true
            },
            "passwordAgin": {
                required: true,
                equalTo: $("#member\\.password")//这里要是jquery对象，而不是id的字符串
            },
            "rid": {
                required: true
            }
        }

    });
});
