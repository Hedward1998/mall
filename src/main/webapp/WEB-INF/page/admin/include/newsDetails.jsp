<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/plugins/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/plugins/ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/plugins/ueditor/lang/zh-cn/zh-cn.js"></script>
    <%--<script type="text/javascript">var ue = UE.getEditor('editor', {zIndex: 0});</script>--%>
    <script>
        $(function () {
            var ue = UE.getEditor('editor', {zIndex: 0});
            if ($("#details_news_id").val() === "" || $("#details_news_id").val() === undefined) {
                //news_id为空，表示为新增公告页

                //单击取消按钮时
                $("#btn_news_cancel").click(function () {
                    $(".menu_li[data-toggle=news]").click();
                });
                
                //当发布公告时
                $("#btn_news_save").click(function () {
                    var news_title = $("#input_news_title").val();
                    var news_status = $("input[name='radio_news_isEnabled']:checked").val();
                    var news_content = UE.getEditor('editor').getContent();   //getPlainTxt();
                    var yn = true;
                    if (news_status === undefined){
                        styleUtil.errorShow($("#text_newsStatus_details_msg"), "请选择新闻公告的发布状态！")
                        yn = false;
                    } 
                    if (news_title === ""){
                        styleUtil.basicErrorShow($("#lbl_news_title"));
                        yn = false;
                    } 
                    if (news_content === ""){
                        styleUtil.errorShow($("#text_ueditor_details_msg"), "请输入新闻公告的内容！");
                        yn = false;
                    } 
                    if (!yn){
                        return;
                    } 
                    
                    //数据集
                    var dataList = {
                        "news_title": news_title,
                        "news_status": news_status, 
                        "news_content": news_content
                    };
                    doAction(dataList, "admin/news", "POST");
                });
            } else {
                //显示新闻公告信息
                $("#btn_news_save").val("保存");

                //设置新闻公告状态
                var news_status = '${requestScope.news.news_status}';
                $("input[name='radio_news_isEnabled']").each(function () {
                    if ($(this).val() === news_status){
                        $(this).prop("checked",true);
                        if ($(this).val() === "0") {
                            $("#text_newsStatus_details_msg").text("提示：新闻公告未发布将不能在前台展示！").attr("title","提示：新闻公告未发布将不能在前台展示！").css("left",0).css("opacity","1");
                        } 
                        return false;
                    } 
                });
                
                //新闻公告内容回显
                var news_content = '${requestScope.news.news_content}';
                //判断ueditor 编辑器是否创建成功
                ue.addListener('ready', function () { 
                    //editor准备好之后才可以使用
                    ue.execCommand('insertHtml', news_content);
                });
                
                //当点击保存
                $("#btn_news_save").click(function () {
                    var news_id = $("#details_news_id").val();
                    var news_title = $("#input_news_title").val();
                    var news_status = $("input[name='radio_news_isEnabled']:checked").val();
                    var news_content = UE.getEditor('editor').getContent();

                    //检验数据
                    var yn = true;
                    if (news_status === undefined){
                        styleUtil.errorShow($("#text_newsStatus_details_msg"), "请选择新闻公告的发布状态！");
                        yn = false;
                    }
                    if (news_title === ""){
                        styleUtil.basicErrorShow($("#lbl_news_title"));
                        yn = false;
                    }
                    if (news_content === ""){
                        styleUtil.errorShow($("#text_ueditor_details_msg"), "请输入新闻公告的内容！");
                        yn = false;
                    }
                    if (!yn){
                        return;
                    }
                    //数据集
                    var dataList = {
                        "news_title": news_title,
                        "news_status": news_status,
                        "news_content": news_content
                    };
                    doAction(dataList, "admin/news/" + news_id, "PUT");
                });
            }
            //改变新闻公告发布状态时
            $('input:radio').click(function () {
                if ($(this).val() === "0") {
                    styleUtil.errorShow($("#text_newsStatus_details_msg"), "提示：新闻公告未发布将不能在前台展示！");
                } else {
                    styleUtil.errorHide($("#text_newsStatus_details_msg"));
                }
            });

            //单击取消按钮时
            $("#btn_news_cancel").click(function () {
                $(".menu_li[data-toggle=news]").click();
            });
            
            //获取到输入框焦点
            $("input:text").focus(function () {
                styleUtil.basicErrorHide($(this).prev("label"))
            });
        });
        
        //操作
        function doAction(dataList, url, type) {
            $.ajax({
                url: url,
                type: type,
                data: dataList,
                traditional: true,
                success: function (data) {
                    $("#btn_news_save").attr("disabled", false).val("保存");
                    if (data.success) {
                        $("#btn-ok,#btn-close").unbind("click").click(function () {
                            $('#modalDiv').modal("hide");
                            setTimeout(function () {
                                //ajax请求页面
                                ajaxUtil.getPage("news/" + data.news_id, null, true);
                            }, 170);
                        });
                        $(".modal-body").text("保存成功！");
                        $('#modalDiv').modal();
                    }
                },
                beforeSend: function () {
                    $("#btn_news_save").attr("disabled", true).val("保存中...");
                },
                error: function () {

                 }
            });
        }
    </script>
    <style rel="stylesheet">
        
        .bootstrap-select:not([class*=col-]):not([class*=form-control]):not(.input-group-btn){
            margin: 0 130px 0 0;
        }
        .frm_input{
            margin-right: 130px;
        }
        .frm_error_msg{
            white-space:nowrap;
        }
        .warn_height{
            max-height: 25px;
        }

        div.br {
            height: 20px;
        }

        .details_property_list label {
            margin-left: 10px;
        }
        .details_div_first{
            padding-bottom: 20px;
            border-bottom: solid 1px #e9ebef;
        }
        .content_div{
            width: 100%;
            height: 400px;
            padding-top: 20px;
        }
        .editor{
            width: 100%;
            height: 400px;
        }
        .content_div>.details_title{
            display: block;
            color: #333;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="details_div_first">
    <input type="hidden" value="${requestScope.news.news_id}" id="details_news_id"/>
    <div class="frm_div_last warn_height">
        <label class="frm_label text_info" id="lbl_news_title" for="input_news_title">新闻公告标题</label>
        <input class="frm_input" id="input_news_title" type="text" maxlength="50" value="${requestScope.news.news_title}"/>
        <label class="frm_label text_info" id="lbl_news_isEnabled" for="radio_news_isEnabled_true">发布状态</label>
        <input id="radio_news_isEnabled_true" name="radio_news_isEnabled" type="radio" value="1" checked="checked">
        <label class="frm_label text_info" id="lbl_news_isEnabled_true" for="radio_news_isEnabled_true">发布</label>
        <input id="radio_news_isEnabled_false" name="radio_news_isEnabled" type="radio" value="0">
        <label class="frm_label text_info" id="lbl_news_isEnabled_false" for="radio_news_isEnabled_false">暂不发布</label>
        <span class="frm_error_msg" id="text_newsStatus_details_msg"></span>
    </div>
</div>
<div class="content_div">
    <span class="details_title text_info">新闻公告内容 </span>
    <script id="editor" class="editor" type="text/plain"></script>
    <span class="frm_error_msg" id="text_editor_details_msg"></span>
</div>

<div class="details_tools_div">
    <input class="frm_btn" id="btn_news_save" type="button" value="发布"/>
    <input class="frm_btn frm_clear" id="btn_news_cancel" type="button" value="取消"/>
</div>

<%-- 模态框 --%>
<div class="modal fade" id="modalDiv" tabindex="-1" role="dialog" aria-labelledby="modalDiv" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body"></div>
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