<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <script src="${pageContext.request.contextPath}/res/js/sweet-alert-dev.js"></script>
    <link href="${pageContext.request.contextPath}/res/css/sweet-alert.css" rel="stylesheet">
    <script>
        $(function () {
            //单击保存按钮时
            $("#btn_admin_save").click(function () {
                var admin_name = $.trim($("#input_admin_name").val());
                var admin_nickname = $.trim($("#input_admin_nickname").val());
                var admin_password = $.trim($("#input_admin_password").val());
                var admin_image_src = null;
                //校验数据合法性
                if ($("#admin_profile_picture").hasClass("new")) {
                    admin_image_src = $.trim($("#admin_profile_picture").attr("src"));
                }
                var yn = true;
                if(admin_name === ""){
                    styleUtil.basicErrorShow($("#lbl_admin_name"));
                    yn = false;
                }
                if(admin_nickname === ""){
                    styleUtil.basicErrorShow($("#lbl_admin_nickname"));
                    yn = false;
                }
                if(admin_password === ""){
                    styleUtil.errorShow($("#text_adminState_details_msg"), "密码未设置，使用默认密码1234！");
                }else{
                    styleUtil.errorHide($("#text_adminState_details_msg"));
                }
                if(!yn){
                    return;
                }

                //数据集
                var dataList = {
                    "admin_name": admin_name,
                    "admin_nickname": admin_nickname,
                    "admin_password": admin_password,
                    "admin_profile_picture_src": admin_image_src
                };
                doAction(dataList, "admin/admin/add", "POST");
            });
            //单击取消按钮时
            $("#btn_admin_cancel").click(function () {
                $(".menu_li[data-toggle=admin]").click();
            });
            //获取到输入框焦点时
            $("input:text").focus(function () {
                styleUtil.basicErrorHide($(this).prev("label"));
            });
        });

        //管理员信息添加
        function doAction(dataList, url, type) {
            $.ajax({
                url: url,
                type: type,
                data: dataList,
                traditional: true,
                success: function (data) {
                    $("#btn_admin_save").attr("disabled", false).val("保存");
                    if (data.success) {
                        $("#btn-ok,#btn-close").unbind("click").click(function () {
                            $('#modalDiv').modal("hide");
                            setTimeout(function () {
                                //ajax请求页面
                                ajaxUtil.getPage("admin/" + data.admin_id, null, true);
                            }, 170);
                        });
                        $(".modal-body").text("保存成功！");
                        $('#modalDiv').modal();
                    } else {
                        swal(data.message);
                    }
                },
                beforeSend: function () {
                    $("#btn_admin_save").attr("disabled", true).val("保存中...");
                },
                error: function () {

                }
            });
        }

        //图片上传
        function uploadImage(fileDom) {
            //获取文件
            var file = fileDom.files[0];
            //判断类型
            var imageType = /^image\//;
            if (file === undefined || !imageType.test(file.type)) {
                $("#btn-ok").unbind("click").click(function () {
                    $("#modalDiv").modal("hide");
                });
                $(".modal-body").text("请选择图片！");
                $('#modalDiv').modal();
                return;
            }
            //判断大小
            if (file.size > 512000) {
                $("#btn-ok").unbind("click").click(function () {
                    $("#modalDiv").modal("hide");
                });
                $(".modal-body").text("图片大小不能超过500K！");
                $('#modalDiv').modal();
                return;
            }
            //清空值
            $(fileDom).val('');
            var formData = new FormData();
            formData.append("file", file);
            //上传图片
            $.ajax({
                url: "/mall/admin/uploadAdminHeadImage",
                type: "post",
                data: formData,
                contentType: false,
                processData: false,
                dataType: "json",
                mimeType: "multipart/form-data",
                success: function (data) {
                    $(".loader").css("display", "none");
                    if (data.success) {
                        $("#admin_profile_picture").addClass("new").attr("src", "${pageContext.request.contextPath}/res/images/item/adminProfilePicture/" + data.fileName);
                    } else {
                        alert("图片上传异常！");
                    }
                },
                beforeSend: function () {
                    $(".loader").css("display", "block");
                },
                error: function () {

                }
            });
        }
        function showTip() {
            var admin_password = $.trim($("#input_admin_password").val());
            if(admin_password === ""){
                styleUtil.errorShow($("#text_adminState_details_msg"), "密码未设置，使用默认密码1234！");
            }else{
                styleUtil.errorHide($("#text_adminState_details_msg"));
            }
        }
    </script>
    <style rel="stylesheet">
        #admin_profile_picture {
            border-radius: 5px;
        }
        .frm_input{
            margin-right: 130px;
        }
        .frm_error_msg{
            white-space:nowrap;
        }

        .details_property_list label {
            margin-left: 10px;
        }
        .details_div_first{
            padding-bottom: 20px;
            border-bottom: solid 1px #e9ebef;
        }
        #uploadImage {
            vertical-align: middle;
            display: inline-block;
            position: relative;
            right: 88px;
            opacity: 0;
            width: 84px;
            height: 84px;
            border-radius: 5px;
            cursor: pointer;
            z-index: 999;
        }
        .frm_label{
            margin-top: 20px;
        }
        .details_div{
            margin-top: 0;
            border-top: none;
        }
    </style>
</head>
<body>
<div class="details_div_first">
    <input type="hidden" id="details_admin_id"/>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_name" for="input_admin_name">管理员账户</label>
        <input class="frm_input" id="input_admin_name" type="text" maxlength="50" />
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_password" for="input_admin_password">管理员密码</label>
        <input class="frm_input" id="input_admin_password" type="password" maxlength="50" onchange="showTip()"/><br/>
        <span class="frm_error_msg" id="text_adminState_details_msg"></span>
    </div>
</div>
<div class="details_div">
    <span class="details_title text_info">基本信息</span>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_profile_picture">管理员头像</label>
        <img
                src=""
                id="admin_profile_picture" width="84px" height="84px"
                onerror="this.src='${pageContext.request.contextPath}/res/images/admin/loginPage/default_profile_picture-128x128.png'"/>
        <input type="file" onchange="uploadImage(this)" accept="image/*" id="uploadImage">
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_nickname" for="input_admin_nickname">管理员昵称</label>
        <input class="frm_input" id="input_admin_nickname" type="text" maxlength="50" 
               value="${requestScope.admin.admin_nickname}"/>
    </div>
</div>
<div class="details_tools_div">
    <input class="frm_btn" id="btn_admin_save" type="button" value="保存"/>
    <input class="frm_btn frm_clear" id="btn_admin_cancel" type="button" value="取消"/>
</div>
<%-- 模态框 --%>
<div class="modal fade" id="modalDiv" tabindex="-1" role="dialog" aria-labelledby="modalDiv" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">您确定要删除该图片吗？</div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary" id="btn-ok">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal" id="btn-close">关闭</button>
            </div>
        </div>
        <%-- /.modal-content --%>
    </div>
    <%-- /.modal --%>
</div>
<div class="loader"></div>
</body>
</html>