$(function () {

    //用户名input获取光标
    $("#user_name").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("4-11位数字、中英文、下划线").css("display", "inline-block").css("color", "#00A0E9");
    });
    //密码input获取光标
    $("#user_password").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("4-11位数字、字母组合，必须包含数字和字母").css("display", "inline-block").css("color", "#00A0E9");
    });
    //再次输入密码input获取光标
    $("#user_password_one").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("和登录密码保持一致").css("display", "inline-block").css("color", "#00A0E9");
    });
    //电话input获取光标
    $("#user_phone").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("请输入中国大陆手机号").css("display", "inline-block").css("color", "#00A0E9");
    });
    //input离开光标
    $(".form-text").blur(function () {
        $(this).css("border-color", "#cccccc")
            .next().css("display", "none");
    });

    //非空验证
    $("#forgetPwd").click(function () {
        //用户名
        var user_name = $.trim($("input[name=user_name]").val());
        //密码
        var user_password = $.trim($("input[name=user_password]").val());
        //确认密码
        var user_password_one = $.trim($("input[name=user_password_one]").val());
        //电话号码
        var user_phone = $.trim($("input[name=user_phone]").val());
        //验证密码的格式 包含数字和英文字母。4~11位数字、字母、下划线或组合
        var reg_pwd = new RegExp(/^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{4,11}$/);
        if (user_name == null || user_name === "") {
            $("#user_name").css("border", "1px solid red")
                .next().text("请输入用户名！").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_phone == null || user_phone === "") {
            $("#user_phone").css("border", "1px solid red")
                .next().text("请输入电话号码！").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_password == null || user_password === "") {
            $("#user_password").css("border", "1px solid red")
                .next().text("请输入要修改的密码！").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_password_one == null || user_password_one === "") {
            $("#user_password_one").css("border", "1px solid red")
                .next().text("请确认密码！").css("display", "inline-block").css("color", "red");
            return false;
        } else if (!reg_pwd.test(user_password)) {
            $("#user_password").css("border", "1px solid red")
                .next().text("密码格式错误！").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_password !== user_password_one) {
            $("#user_password_one").css("border", "1px solid red")
                .next().text("两次输入密码不相同！").css("display", "inline-block").css("color", "red");
            return false;
        }
        $.ajax({
            type: "POST",
            url: "/mall/forgetPwd",
            data: {
                "user_name": user_name,
                "user_password": user_password,
                "user_phone": user_phone
            },
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    $(".reset_password_msg").stop(true, true).animate({
                        opacity: 1
                    }, 550, function () {
                        $(".reset_password_msg").animate({
                            opacity: 0
                        }, 1500, function () {
                            location.href = "/mall/login";
                        });
                    });
                } else if (data.user_name != null) {
                    $("#user_name").css("border", "1px solid red")
                        .next().text(data.user_name).css("display", "inline-block").css("color", "red");
                } else if (data.user_phone != null) {
                    $("#user_phone").css("border", "1px solid red")
                        .next().text(data.user_phone).css("display", "inline-block").css("color", "red");
                }
            },
            error: function (data) {
                location.reload(true);
            },
            beforeSend: function () {
            }
        });
    });
});