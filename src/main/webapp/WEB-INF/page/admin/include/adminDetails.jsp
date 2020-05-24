<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script src="${pageContext.request.contextPath}/res/js/sweet-alert-dev.js"></script>
    <link href="${pageContext.request.contextPath}/res/css/sweet-alert.css" rel="stylesheet">
    <script>
        $(function () {

            /******
             * event
             ******/
            //单击取消按钮时
            $("#btn_admin_cancel").click(function () {
                $(".menu_li[data-toggle=admin]").click();
            });
            
            //单击删除按钮时
            $("#btn_admin_delete").click(function () {
                var admin_id = parseInt($("#details_admin_id").val());
                $.ajax({
                    url: "admin/admin/del?admin_id=" + admin_id,
                    type: "DELETE",
                    data: null,
                    traditional: true,
                    success: function (data) {
                        if (data.success) {
                            swal("删除成功！");
                            location.href = "/mall/admin";
                        } else {
                            swal("删除失败！");
                        }
                    },
                    beforeSend: function () {
                        $("#btn_admin_save").attr("disabled", true).val("删除中...");
                    },
                    error: function () {
                    }
                });
            });
        });
    </script>
    <style rel="stylesheet">
        #admin_profile_picture {
            border-radius: 5px;
        }

        #table_orderItem_list th:first-child {
            width: auto;
        }
    </style>
</head>
<body>
<div class="details_div_first">
    <input type="hidden" value="${requestScope.admin.admin_id}" id="details_admin_id"/>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_id">管理员编号</label>
        <span class="details_value" id="span_admin_id">${requestScope.admin.admin_id}</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_name">管理员姓名</label>
        <span class="details_value" id="span_admin_name">${requestScope.admin.admin_name}</span>
    </div>
</div>
<div class="details_div">
    <span class="details_title text_info">基本信息</span>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_profile_picture">管理员头像</label>
        <img
                src="${pageContext.request.contextPath}/res/images/item/adminProfilePicture/${requestScope.admin.admin_profile_picture_src}"
                id="admin_profile_picture" width="84px" height="84px"
                onerror="this.src='${pageContext.request.contextPath}/res/images/admin/loginPage/default_profile_picture-128x128.png'"/>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_admin_nickname">管理员昵称</label>
        <span class="details_value" id="span_admin_nickname">${requestScope.admin.admin_nickname}</span>
    </div>
</div>
<div class="details_tools_div">
    <input class="frm_btn" id="btn_admin_delete" type="button" value="删除"/>
    <input class="frm_btn frm_clear" id="btn_admin_cancel" type="button" value="取消"/>
</div>
<div class="loader"></div>
</body>
</html>
