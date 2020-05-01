<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="include/header.jsp" %>
<head>
    <script src="${pageContext.request.contextPath}/res/js/fore/fore_forgetPwd.js"></script>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_userDetailes.css" rel="stylesheet">
    <title>Mall.com - 忘记密码</title>
    <style rel="stylesheet">
        #baseNavigator {
            padding: 22px 0;
            width: 1190px;
            height: 44px;
            margin: auto;
        }

        #baseNavigator img {
            width: 190px;
            margin-top: 8px;
        }

        #nav {
            width: auto;
            height: 32px;
            font-family: "Microsoft YaHei UI", Tahoma, serif;
            font-size: 12px;
            position: relative !important;
            background: #f2f2f2;
            z-index: 999;
            border-bottom: 1px solid #e5e5e5;
        }
    </style>
</head>
<body>
<nav>
    <%@ include file="include/navigator.jsp" %>
    <div class="header">
        <div id="mallLogo">
            <a href="${pageContext.request.contextPath}">
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/mallLogoA.png">
            </a>
        </div>
    </div>
</nav>
<div class="content">
    <div class="sns-config" id="profile">
        <div class="sns-tab tab-app">
            <span>重置密码</span>
        </div>
        <div class="sns-main">
            <div id="tips-box">
                <label class="font_we">请输入正确的信息将有助于密码重置哦！</label>
            </div>
            <form action="${pageContext.request.contextPath}/reset_password" method="post" id="reset_password_form">
                <div class="form-item">
                    <label class="form-label tsl">用户名称：</label>
                    <input name="user_name" id="user_name"
                           class="form-text err-input"  placeholder="请输入你的用户名称"  maxlength="20">
                    <span class="form_span"></span>
                </div>
                <div class="form-item">
                    <label class="form-label tsl">电话号码：</label>
                    <input name="user_phone" id="user_phone"
                           class="form-text err-input"  placeholder="请输入你的电话号码"  maxlength="11">
                    <span class="form_span"></span>
                </div>
                <div class="form-item">
                    <label class="form-label tsl">登录密码：</label>
                    <input name="user_password" type="password" id="user_password" 
                           class="form-text err-input" placeholder="请设置登录密码" maxlength="20">
                    <span class="form_span"></span>
                </div>
                <div class="form-item">
                    <label class="form-label tsl">确认密码：</label>
                    <input name="user_password_one" type="password" id="user_password_one" 
                           class="form-text err-input" placeholder="请再次输入你的密码" maxlength="20">
                    <span class="form_span"></span>
                </div>
                <div class="form-item">
                    <input type="button" id="forgetPwd" class="btns btn-large tsl" value="重置密码"/>
                </div>
            </form>
        </div>
    </div>
</div>
<%@include file="include/footer.jsp" %>
<link href="${pageContext.request.contextPath}/res/css/fore/fore_foot_special.css" rel="stylesheet"/>
<div class="reset_password_msg">
    <span>密码重置成功，跳转到登录页面</span>
</div>
</body>

