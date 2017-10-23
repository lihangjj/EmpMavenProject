$(function() {
	$.backstretch("images/login_back.jpg");
	$("#imageCode").on("click",function(){
		$("#imageCode").attr("src","captcha.jpg?p=" + Math.random()) ;
		$("#code").val("");
	}) ;
	$("#myform").validate({
		debug : true, // 取消表单的提交操作
		submitHandler : function(form) {
			form.submit(); // 提交表单
		},
		errorPlacement : function(error, element) {
			$("#" + $(element).attr("id").replace(".", "\\.") + "Msg").append(error);
		},
		highlight : function(element, errorClass) {
			$(element).fadeOut(1,function() {
				$(element).fadeIn(1, function() {
					$("#" + $(element).attr("id").replace(".","\\.") + "Div").attr("class","form-group has-error");
				});

			})
		},
		unhighlight : function(element, errorClass) {
			$(element).fadeOut(1,function() {
				$(element).fadeIn(1,function() {
						$("#" + $(element).attr("id").replace(".","\\.") + "Div").attr("class","form-group has-success");
				});
			})
		},
		errorClass : "text-danger",
		rules : {
			"member.mid" : {
				required : true 
			},
			"member.password" : { 
				required : true
			} ,
			"code" : {
				required : true ,
				remote : {
					url : "CodeCheck", // 后台处理程序
					type : "post", // 数据发送方式
					dataType : "html", // 接受数据格式
					data : { // 要传递的数据
						code : function() {
							return $("#code").val();
						}
					},
					dataFilter : function(data, type) {
						if (data.trim() == "true") {
							return true;
						} else {
							return false;
						}
					}
				}

			}
		}
	});
	$('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea')
			.on('focus', function() {
				$(this).removeClass('input-error');
			});

	$('.login-form').on(
			'submit',
			function(e) {
				$(this).find(
						'input[type="text"], input[type="password"], textarea')
						.each(function() {
							if ($(this).val() == "") {
								e.preventDefault();
								$(this).addClass('input-error');
							} else {
								$(this).removeClass('input-error');
							}
						});

				});
})
.validate({
        // debug: true,
        // submitHandler: function (form) {
        //     form.submit();
        // },
        errorPlacement: function (error, element) {
            $("#" + $(element).attr("id") + "Span").append(error);
        },
        highlight: function (element, errorClass) {
            $(element).fadeOut(1, function () {
                $(element).fadeIn(1);
                $(element).parent().attr("class", "form-group form-inline has-error")
            })
        },
        unhighlight: function (element, errorClass) {
            $(element).fadeOut(1, function () {
                $(element).fadeIn(1);
                //通过parent()选择父元素
                $(element).parent().attr("class", "form-group form-inline has-success");
            })
        },
        errorClass: "text-danger",
        rules: {
            "admin": {
                required: true
            },
            "email": {
                required: true,
                email: true
            },
            "password": {
                required: true
            },
            "age": {
                required: true,
                digits: true
            },
            "birthday": {
                dateISO: true
            },
            "code": {
                required: true,
                rangelength: [4, 4],
                remote: {
                    url: "haha.jsp",
                    type: "post",
                    dataType: "text",
                    data: {
                        "code": function () {
                            return $("#code").val();
                        }
                    },
                    dataFilter: function (data, type) {
                        alert(data)
                        if (data.trim() == "true") {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }


    })