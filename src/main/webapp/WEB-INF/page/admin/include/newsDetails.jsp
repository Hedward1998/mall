<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <script>
        $(function () {
            if($("#details_product_id").val() === ""){
                //刷新下拉框
                $('#select_product_category').selectpicker('refresh');

                /******
                 * event
                 ******/
                //单击保存按钮时
                $("#btn_product_save").click(function () {
                    var product_category_id = $("#select_product_category").selectpicker("val");
                    var product_isEnabled = $("input[name='radio_product_isEnabled']:checked").val();
                    var product_name = $.trim($("#input_product_name").val());
                    var product_title = $.trim($("#input_product_title").val());
                    var product_price = $.trim($("#input_product_price").val());
                    var product_sale_price = $.trim($("#input_product_sale_price").val());

                    //校验数据合法性
                    var yn = true;
                    if(product_isEnabled === undefined){
                        styleUtil.errorShow($("#text_productState_details_msg"),"请选择产品状态！");
                        yn = false;
                    }
                    if(product_name === ""){
                        styleUtil.basicErrorShow($("#lbl_product_name"));
                        yn = false;
                    }
                    if(product_title === ""){
                        styleUtil.basicErrorShow($("#lbl_product_title"));
                        yn = false;
                    }
                    if(product_price === "" || isNaN(product_price)){
                        styleUtil.basicErrorShow($("#lbl_product_price"));
                        yn = false;
                    }
                    if(product_sale_price === "" || isNaN(product_sale_price)){
                        styleUtil.basicErrorShow($("#lbl_product_sale_price"));
                        yn = false;
                    }
                    if(!yn){
                        return;
                    }

                    //产品属性Map
                    var propertyMap = {};
                    $("input[id^='input_product_property']").each(function () {
                        var value = $.trim($(this).val());
                        if (value === "") {
                            return true;
                        }
                        var key = $(this).attr("id").substring($(this).attr("id").lastIndexOf('_') + 1);
                        propertyMap[key] = value;
                    });

                    //产品图片List
                    var productSingleImageList = [];
                    $("#product_single_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        if (img.attr("name") === "new") {
                            productSingleImageList.push(img.attr("src"));
                        }
                    });
                    var productDetailsImageList = [];
                    $("#product_details_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        if (img.attr("name") === "new") {
                            productDetailsImageList.push(img.attr("src"));
                        }
                    });

                    //数据集
                    var dataList = {
                        "product_category_id": product_category_id,
                        "product_isEnabled" : product_isEnabled,
                        "product_name": product_name,
                        "product_title": product_title,
                        "product_price": product_price,
                        "product_sale_price": product_sale_price,
                        "propertyJson": JSON.stringify(propertyMap),
                        "productSingleImageList": productSingleImageList,
                        "productDetailsImageList": productDetailsImageList
                    };
                    doAction(dataList, "admin/product", "POST");
                });
            } else {
                //设置产品种类值
                $('#select_product_category').selectpicker('val','${requestScope.product.product_category.category_id}');
                //设置产品状态
                var product_isEnabled = '${requestScope.product.product_isEnabled}';
                $("input[name='radio_product_isEnabled']").each(function () {
                    if($(this).val() === product_isEnabled){
                        $(this).prop("checked",true);
                        if($(this).val() === "1"){
                            $("#text_productState_details_msg").text("提示：产品停售时无法进行交易").attr("title","提示：产品停售时无法进行交易").css("left",0).css("opacity","1");
                        }
                        return false;
                    }
                });
                //设置产品编号
                $("#span_product_id").text('${requestScope.product.product_id}');
                //设置产品创建日期
                $("#span_product_create_date").text('${requestScope.product.product_create_date}');
                //判断文件是否允许上传
                checkFileUpload($("#product_single_list"),5);
                checkFileUpload($("#product_details_list"),8);
                //原属性值Map
                var propertyMap = {};
                $("input[id^='input_product_property'][data-pvid]").each(function () {
                    var value_id = $(this).attr("data-pvid");
                    propertyMap[value_id] = $(this).val();
                });

                /******
                 * event
                 ******/
                //单击保存按钮时
                $("#btn_product_save").click(function () {
                    var product_id = $("#details_product_id").val();
                    var product_category_id = $("#select_product_category").selectpicker("val");
                    var product_isEnabled = $("input[name='radio_product_isEnabled']:checked").val();
                    var product_name = $.trim($("#input_product_name").val());
                    var product_title = $.trim($("#input_product_title").val());
                    var product_price = $.trim($("#input_product_price").val());
                    var product_sale_price = $.trim($("#input_product_sale_price").val());

                    //校验数据合法性
                    var yn = true;
                    if (product_isEnabled === undefined) {
                        styleUtil.errorShow($("#text_productState_details_msg"), "请选择产品状态！");
                        yn = false;
                    }
                    if (product_name === "") {
                        styleUtil.basicErrorShow($("#lbl_product_name"));
                        yn = false;
                    }
                    if (product_title === "") {
                        styleUtil.basicErrorShow($("#lbl_product_title"));
                        yn = false;
                    }
                    if (product_price === "" || isNaN(product_price)) {
                        styleUtil.basicErrorShow($("#lbl_product_price"));
                        yn = false;
                    }
                    if (product_sale_price === "" || isNaN(product_sale_price)) {
                        styleUtil.basicErrorShow($("#lbl_product_sale_price"));
                        yn = false;
                    }
                    if (!yn) {
                        return;
                    }

                    //产品属性Map
                    var propertyAddMap = {};
                    var propertyUpdateMap = {};
                    var propertyDeleteList = [];
                    //获取需要更新或删除的产品属性
                    $("input[id^=input_product_property][data-pvid]").each(function () {
                        var value_id = $(this).attr("data-pvid");
                        var value = $.trim($(this).val());
                        if (value === "") {
                            propertyDeleteList.push(value_id);
                        } else if (propertyMap[value_id] !== value) {
                            propertyUpdateMap[value_id] = value;
                        }
                    });
                    //获取需要添加的产品属性
                    $("input[id^=input_product_property]:not([data-pvid])").each(function () {
                        var value = $.trim($(this).val());
                        if (value === "") {
                            return true;
                        } else {
                            var key = $(this).attr("id").substring($(this).attr("id").lastIndexOf('_') + 1);
                            propertyAddMap[key] = value;
                        }
                    });

                    //产品图片List
                    var productSingleImageList = [];
                    $("#product_single_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        if (img.attr("name") === "new") {
                            productSingleImageList.push(img.attr("src"));
                        }
                    });
                    var productDetailsImageList = [];
                    $("#product_details_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        if (img.attr("name") === "new") {
                            productDetailsImageList.push(img.attr("src"));
                        }
                    });

                    //数据集
                    var dataList = {
                        "product_category_id": product_category_id,
                        "product_isEnabled": product_isEnabled,
                        "product_name": product_name,
                        "product_title": product_title,
                        "product_price": product_price,
                        "product_sale_price": product_sale_price,
                        "propertyAddJson": JSON.stringify(propertyAddMap),
                        "propertyUpdateJson": JSON.stringify(propertyUpdateMap),
                        "propertyDeleteList": propertyDeleteList,
                        "productSingleImageList": productSingleImageList,
                        "productDetailsImageList": productDetailsImageList
                    };
                    doAction(dataList, "admin/product/" + product_id, "PUT");
                });
            }

            /******
             * event
             ******/
            //单击图片列表项时
            $(".details_picList").on("click","li:not(.details_picList_fileUpload)",function () {
                var img = $(this);
                var productImage_id = img.children("img").attr("name");
                var fileUploadInput = $(this).parents("ul").children(".details_picList_fileUpload");
                if (productImage_id === "new") {
                    $("#btn-ok").unbind("click").click(function () {
                        img.remove();
                        fileUploadInput.css("display", "inline-block");
                        $('#modalDiv').modal("hide");
                    });
                } else {
                    $("#btn-ok").unbind("click").click(function () {
                        $.ajax({
                            url: "/mall/admin/productImage/" + productImage_id,
                            type: "delete",
                            data: null,
                            success: function (data) {
                                $("#btn-ok").attr("disabled", false).text("确定");
                                if (data.success) {
                                    img.remove();
                                    fileUploadInput.css("display", "inline-block");
                                    $('#modalDiv').modal("hide");
                                } else {
                                    $('#modalDiv').modal("hide");
                                    alert("图片删除异常！");
                                }
                            },
                            beforeSend: function () {
                                $("#btn-ok").attr("disabled", true).text("操作中...");
                            },
                            error: function () {

                            }
                        });
                    });
                }
                $(".modal-body").text("您确定要删除该产品图片吗？");
                $('#modalDiv').modal();
            });
            //改变产品状态时
            $('input:radio').click(function () {
                if($(this).val() === "1"){
                    styleUtil.errorShow($("#text_productState_details_msg"),"提示：产品停售时无法进行交易");
                } else {
                    styleUtil.errorHide($("#text_productState_details_msg"));
                }
            });
            //单击取消按钮时
            $("#btn_product_cancel").click(function () {
                $(".menu_li[data-toggle=product]").click();
            });
            //更改产品类型列表时
            $("#select_product_category").change(function () {
                $.ajax({
                    url: "admin/property/type/"+$(this).val(),
                    type: "get",
                    data: null,
                    success: function (data) {
                        $(".loader").css("display", "none");
                        //清空原有数据
                        var listDiv = $(".details_property_list");
                        listDiv.empty().append("<span class='details_title text_info'>属性值信息</span>");
                        //显示产品属性数据
                        if(data.propertyList.length > 0){
                            for(var i = 0;i<data.propertyList.length;i++){
                                var propertyId = data.propertyList[i].property_id;
                                var propertyName = data.propertyList[i].property_name;
                                if(data.propertyList[i+1] !== undefined){
                                    var nextPropertyId = data.propertyList[i+1].property_id;
                                    var nextPropertyName = data.propertyList[i+1].property_name;
                                    i++;
                                    listDiv.append("<label class='frm_label lbl_property_name text_info' id='lbl_product_property_" + propertyId + "' for='input_product_property_" + propertyId + "'>" + propertyName + "</label><input class='frm_input' id='input_product_property_" + propertyId + "' type='text' maxlength='50'/><label class='frm_label lbl_property_name text_info' id='lbl_product_property_" + nextPropertyId + "' for='input_product_property_" + nextPropertyId + "'>" + nextPropertyName + "</label><input class='frm_input' id='input_product_property_" + nextPropertyId + "' type='text' maxlength='50'/><div class='br'></div>");
                                } else {
                                    listDiv.append("<label class='frm_label lbl_property_name text_info' id='lbl_product_property_" + propertyId + "' for='input_product_property_" + propertyId + "'>" + propertyName + "</label><input class='frm_input' id='input_product_property_" + propertyId + "' type='text' maxlength='50'/><div class='br'></div>");
                                }
                            }
                        }
                    },
                    beforeSend: function () {
                        $(".loader").css("display", "block");
                    },
                    error: function () {

                    }
                });
            });
            //获取到输入框焦点时
            $("input:text").focus(function () {
                styleUtil.basicErrorHide($(this).prev("label"));
            });
        });

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
            var ul = $(fileDom).parents(".details_picList");
            var type;
            if (ul.attr("id") === "product_single_list") {
                type = "single";
            } else {
                type = "details";
            }
            //清空值
            $(fileDom).val('');
            var formData = new FormData();
            formData.append("file", file);
            formData.append("imageType", type);
            //上传图片
            $.ajax({
                url: "/mall/admin/uploadProductImage",
                type: "post",
                data: formData,
                contentType: false,
                processData: false,
                dataType: "json",
                mimeType: "multipart/form-data",
                success: function (data) {
                    $(fileDom).attr("disabled", false).prev("span").text("上传图片");
                    if (data.success) {
                        if (type === "single") {
                            $(fileDom).parent('.details_picList_fileUpload').before("<li><img src='${pageContext.request.contextPath}/res/images/item/productSinglePicture/" + data.fileName + "' width='128px' height='128px' name='new'/></li>");
                            checkFileUpload(ul, 5);
                        } else {
                            $(fileDom).parent('.details_picList_fileUpload').before("<li><img src='${pageContext.request.contextPath}/res/images/item/productDetailsPicture/" + data.fileName + "' width='128px' height='128px' name='new'/></li>");
                            checkFileUpload(ul, 8);
                        }
                    } else {
                        alert("图片上传异常！");
                    }
                },
                beforeSend: function () {
                    $(fileDom).attr("disabled", true).prev("span").text("图片上传中...");
                },
                error: function () {

                }
            });
        }

        //判断是否允许上传文件
        function checkFileUpload(obj, size) {
            if(obj.children("li:not(.details_picList_fileUpload,:hidden)").length>=size){
                obj.children(".details_picList_fileUpload").css("display","none");
            } else {
                obj.children(".details_picList_fileUpload").css("display","inline-block");
            }
        }

        //产品操作
        function doAction(dataList, url, type) {
            $.ajax({
                url: url,
                type: type,
                data: dataList,
                traditional: true,
                success: function (data) {
                    $("#btn_product_save").attr("disabled", false).val("保存");
                    if (data.success) {
                        $("#btn-ok,#btn-close").unbind("click").click(function () {
                            $('#modalDiv').modal("hide");
                            setTimeout(function () {
                                //ajax请求页面
                                ajaxUtil.getPage("product/" + data.product_id, null, true);
                            }, 170);
                        });
                        $(".modal-body").text("保存成功！");
                        $('#modalDiv').modal();
                    }
                },
                beforeSend: function () {
                    $("#btn_product_save").attr("disabled", true).val("保存中...");
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
    </style>
</head>
<body>
<div class="details_div_first">
    <input type="hidden" value="${requestScope.news.news_id}" id="details_news_id"/>
    <div class="frm_div_last warn_height">
        <label class="frm_label text_info" id="lbl_news_title" for="input_news_title">新闻公告标题</label>
        <input class="frm_input" id="input_news_title" type="text" maxlength="50" value="${requestScope.news.news_title}"/>
        <label class="frm_label text_info" id="lbl_news_isEnabled" for="radio_news_isEnabled_true">发布状态</label>
        <input id="radio_news_isEnabled_true" name="radio_news_isEnabled" type="radio" value="1" checked>
        <label class="frm_label text_info" id="lbl_news_isEnabled_true" for="radio_news_isEnabled_true">发布中</label>
        <input id="radio_news_isEnabled_false" name="radio_news_isEnabled" type="radio" value="0">
        <label class="frm_label text_info" id="lbl_news_isEnabled_false" for="radio_news_isEnabled_false">未发布</label>
        <span class="frm_error_msg" id="text_newsState_details_msg"></span>
    </div>
</div>
<div class="content_div">
    
</div>

<div class="details_tools_div">
    <input class="frm_btn" id="btn_news_save" type="button" value="发布"/>
    <input class="frm_btn frm_clear" id="btn_news_cancel" type="button" value="取消"/>
</div>
<div class="loader"></div>
</body>
</html>