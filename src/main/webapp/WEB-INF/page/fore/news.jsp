<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="include/header.jsp" %>
<head>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_news.css" rel="stylesheet">
    <script type="text/javascript" src="${pageContext.request.contextPath}/res/js/fore/fore_news.js"></script>
    <title>Mall.com - 新闻公告</title>
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
    <script type="text/javascript" defer="true">
        var newsArry = null;
        var isFirstPage = true;
        //取得新闻列表数据
        function getNewsArry() {
            newsArry = new Array();
            <c:forEach items="${requestScope.newsList}" var="news">
                var arry = new Array();
                arry.push("${news.news_id}");
                arry.push("${news.news_title}");
                arry.push('${news.news_content}');
                arry.push("${news.news_publish_date}");
                arry.push("${news.news_publish_person_name}");
                newsArry.push(arry);
            </c:forEach>
        }
        //换页后加载数据
        function getNewsArryByData(data) {
            newsArry = Array();
            for (let i = 0;i < data.newsList.length; i++) {
                var arry = new Array();
                arry.push(data.newsList[i].news_id);
                arry.push(data.newsList[i].news_title);
                arry.push(data.newsList[i].news_content);
                arry.push(data.newsList[i].news_publish_date);
                arry.push(data.newsList[i].news_publish_person_name);
                newsArry.push(arry);
            }
        }
        //默认显示列表第一条新闻公告的内容
        function setDefaultNewsContent(data) {
            if (isFirstPage === true)  {
                getNewsArry();
            } else {
                getNewsArryByData(data);
            }
            //清除之前的数据
            $("#news_content").text("");
            $("#news_publish_date").text("发布时间：");
            $("#news_publish_person_name").text("发布人：");
            
            $("#news_title_name").text(newsArry[0][1]);
            $("#news_content").append(newsArry[0][2]);//append尾部内容追加
            $("#news_publish_date").append(newsArry[0][3]);
            $("#news_publish_person_name").append(newsArry[0][4]);
        }
        //页面加载完毕执行方法
        $(function () {
            isFirstPage = true;
            setDefaultNewsContent(null);
        });
        //获取点击行数据
        function getClickNews(news_id) {
            for (let i = 0;i < newsArry.length;i++) {
                if (news_id == newsArry[i][0]) {
                    //清除之前的数据
                    $("#news_content").text("");
                    $("#news_publish_date").text("发布时间：");
                    $("#news_publish_person_name").text("发布人：");
                    
                    $("#news_title_name").text(newsArry[i][1]);
                    $("#news_content").append(newsArry[i][2]);
                    $("#news_publish_date").append(newsArry[i][3]);
                    $("#news_publish_person_name").append(newsArry[i][4]);
                    break;
                }
            } 
        }

        //获取新闻公告数据
        function getData(object,url,dataObject) {
            var table = $("#table_news_list");
            var tbody = table.children("tbody").first();
            $.ajax({
                url: url,
                type: "get",
                data: dataObject,
                traditional: true,
                success: function (data) {
                    //清空原有数据
                    tbody.empty();
                    //设置样式
                    $(".loader").css("display","none");
                    object.attr("disabled",false);
                    //显示新闻公告统计数据
                    $("#news_count_data").text(data.newsCount);
                    if (data.newsList.length > 0) {
                        for (var i = 0; i < data.newsList.length; i++) {
                            var news_id = data.newsList[i].news_id;
                            var news_title = data.newsList[i].news_title;
                            //显示用户数据
                            // getClickNews('"+ news_id +"')
                            tbody.append("<tr onclick='getClickNews("+ news_id +")'><td title='" + news_title + "'>" + news_title + "</td><td hidden class='news_id'>" + news_id + "</td></tr>");
                        }
                        //绑定事件
                        tbody.children("tr").click(function () {
                            trDataStyle($(this));
                        });
                        //分页
                        var pageUtil = {
                            index: data.pageUtil.index,
                            count: data.pageUtil.count,
                            total: data.pageUtil.total,
                            totalPage: data.totalPage
                        };
                        createPageDiv($(".loader"), pageUtil);
                        isFirstPage = false;
                        setDefaultNewsContent(data);
                    }
                },
                beforeSend: function () {
                    $(".loader").css("display","block");
                    object.attr("disabled",true);
                },
                error: function () {

                }
            });
        }

        //获取页码数据
        function getPage(index) {
            getData($(this), "news/" + index + "/10", null);
        }
    </script>
</head>
<body>
<nav>
    <%@ include file="include/navigator.jsp" %>
    <div class="header">
        <div id="mallLogo">
            <a href="${pageContext.request.contextPath}"><img
                    src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/mallLogoA.png"><span
                    class="span_mallRegister">新闻公告</span></a>
        </div>
    </div>
</nav>
<div class="news_div">
    <div class="data_count_div text_info">
        <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2522" width="16"
             height="16">
            <path d="M401.976676 735.74897c-88.721671 0-172.124196-34.635845-234.843656-97.526197-62.724577-62.86784-97.271394-146.453537-97.271394-235.358379s34.546817-172.490539 97.276511-235.361449c62.715367-62.887282 146.117892-97.522104 234.838539-97.522104 88.719624 0 172.135452 34.633798 234.881518 97.522104 62.704111 62.875003 97.235578 146.4607 97.235578 235.361449 0 88.901773-34.530444 172.487469-97.231485 235.358379C574.112128 701.116195 490.6963 735.74897 401.976676 735.74897zM401.976676 121.204479c-75.012438 0-145.533584 29.290093-198.572568 82.474386-109.585861 109.834524-109.585861 288.539602-0.004093 398.36901 53.043077 53.188386 123.564223 82.47848 198.577684 82.47848 75.015507 0 145.553027-29.291117 198.620663-82.47848C710.126918 492.220514 710.126918 313.511343 600.593246 203.678866 547.530726 150.496619 476.992183 121.204479 401.976676 121.204479z"
                  p-id="2523" fill="#FF7874">
            </path>
            <path d="M932.538427 958.228017c-6.565533 0-13.129019-2.508123-18.132986-7.52437L606.670661 642.206504c-9.989515-10.014074-9.969049-26.231431 0.045025-36.220946s26.230408-9.969049 36.220946 0.045025l307.73478 308.497143c9.989515 10.014074 9.969049 26.231431-0.045025 36.220946C945.627537 955.735244 939.081447 958.228017 932.538427 958.228017z"
                  p-id="2524" fill="#FF7874">
            </path>
        </svg>
        <span class="data_count_title">查看合计</span>
        <span>新闻公告总数:</span>
        <span class="data_count_value" id="news_count_data">${requestScope.newsCount}</span>
        <span class="data_count_unit">条</span>
    </div>
    <div class="table_news_div">
        <table class="table_news" id="table_news_list">
            <thead class="text_info">
            <tr>
                <th class="data_info" data-name="news_title">
                    <span>新闻公告标题</span>
                </th>
                <th hidden>新闻公告ID</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${requestScope.newsList}" var="news">
                <tr onclick="getClickNews(${news.news_id})" >
                    <td id="news_title" title="${news.news_title}">${news.news_title}</td>
                    <td hidden class="news_id">${news.news_id}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <%@ include file="include/page.jsp" %>
        <div class="loader"></div>
    </div>
    <div class="news_details" id="news_details">
        <h3 class="news_title_name" id="news_title_name">测试</h3>
        <span class="news_publish_date" id="news_publish_date">发布时间:</span>&nbsp;&nbsp;
        <span class="news_publish_person_name" id="news_publish_person_name">发布人:</span>
        <div>
            <div class="news_content" id="news_content">
                <%--这是文章的测试文字！这是文章的测试文字！这是文章的测试文字！这是文章的测试文字！这是文章的测试文字！这是文章的测试文字！--%>
                <%--这是文章的测试文字！这是文章的测试文字！这是文章的测试文字！--%>
                <%--这是文章的测试文字！这是文章的测试文字！这是文章的测试文字！这是文章的测试文字！--%>
            </div>
        </div>
    </div>
</div>
<%@include file="include/footer.jsp" %>
<link href="${pageContext.request.contextPath}/res/css/fore/fore_foot_special.css" rel="stylesheet"/>
</body>

