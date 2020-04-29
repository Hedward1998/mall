<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/fore/fore_nav.css"/>
<script>
    $(function () {
        $(".quick_li").find("li").hover(
            function () {
                $(this).find(".sn_menu").addClass("sn_menu_hover");
                $(this).find(".quick_menu,.quick_qrcode,.quick_DirectPromoDiv,.quick_sitmap_div").css("display", "block");
            }, function () {
                $(this).find(".sn_menu").removeClass("sn_menu_hover");
                $(this).find(".quick_menu,.quick_qrcode,.quick_DirectPromoDiv,.quick_sitmap_div").css("display", "none");
            }
        );
    });
</script>
<div id="nav">
    <div class="nav_main">
        <p id="container_login">
            <c:choose>
                <c:when test="${requestScope.user.user_name==null}">
                    <em>欢迎来到Mall商城</em>
                    <a href="${pageContext.request.contextPath}/login">请登录</a>
                    <a href="${pageContext.request.contextPath}/register">免费注册</a>
                    <a href="${pageContext.request.contextPath}/login/logout">新闻公告</a>
                </c:when>
                <c:otherwise>
                    <em>Hi，</em>
                    <a href="${pageContext.request.contextPath}/userDetails" class="userName"
                       target="_blank">${requestScope.user.user_name}</a>
                    <a href="${pageContext.request.contextPath}/login/logout">退出</a>
                    <a href="${pageContext.request.contextPath}/login/logout">新闻公告</a>
                </c:otherwise>
            </c:choose>
        </p>
        <ul class="quick_li">
            <li class="quick_li_MyTaobao">
                <div class="sn_menu">
                    <a href="${pageContext.request.contextPath}/userDetails">个人中心<b></b></a>
                    <div class="quick_menu">
                        <a href="${pageContext.request.contextPath}/order/0/10">已买到的商品</a>
                    </div>
                </div>
            </li>
            <li class="quick_li_cart">
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/buyCar.png">
                <a href="${pageContext.request.contextPath}/cart">购物车</a>
            </li>
            <li class="quick_li_separator"></li>
            <li class="quick_DirectPromo">
                <div class="sn_menu">
                    <a href="#">关于本站<b></b></a>
                    <div class="quick_DirectPromoDiv">
                        <ul>
                            <p>
                                    【Mall商城】是致力于帮助广大消费者方便快捷、诚信满意的购物平台，
                                精挑细选提供最优质的商品，让您用更低廉的价格购买更精致的商品，是本站的宗旨与奋斗目标！
                            </p>
                        </ul>
                    </div>
                </div>
            </li>
            <li class="quick_sitemap">
                <div class="sn_menu">
                    <a>网站导航<b></b></a>
                    <div class="quick_sitmap_div">
                        <div class="site-hot">
                            <h2>网站推荐</h2>
                            <ul>
                                <li><a href="https://www.tmall.com">天猫超市</a></li>
                                <li><a href="https://www.tmall.hk/">天猫国际</a></li>
                                <li><a href="https://www.jd.com/">京东</a></li>
                                <li><a href="https://www.taobao.com">淘宝</a></li>
                                <li><a href="https://ju.taobao.com/">聚划算</a></li>
                                <li><a href="https://www.mogu.com/">蘑菇街</a></li>
                                <li><a href="https://you.163.com/">网易严选</a></li>
                                <li><a href="https://www.pinduoduo.com/">拼多多</a></li>
                                <li><a href="https://www.fliggy.com/">飞猪</a></li>
                                <li><a href="https://www.alipay.com/">支付宝</a></li>
                                <li><a href="https://www.dingtalk.com/">钉钉</a></li>
                                <li><a href="https://www.91mbp.com/">实淘惠</a></li>
                                <li><a href="https://www.disneystore.com/">迪士尼</a></li>
                                <li><a href="https://www.amazon.com/">亚马逊</a></li>
                                <li><a href="https://www.ebay.com/">易贝</a></li>
                            </ul>
                        </div>
                        <div class="site-help">
                            <h2>帮助指南</h2>
                            <ul>
                                <li><a href="https://consumerservice.tmall.com/online-help?spm=875.7931836/B.a2228l4.31.17d34265UJwThy">帮助中心</a></li>
                                <li><a href="https://www.tmall.com/wow/seller/act/pinkong?spm=875.7931836/B.a2228l4.32.17d34265UJwThy">品质保障</a></li>
                                <li><a href="https://www.tmall.com/wow/seller/act/special-service?spm=875.7931836/B.a2228l4.33.17d34265UJwThy">特色服务</a></li>
                                <li><a href="https://www.tmall.com/wow/seller/act/seven-day?spm=875.7931836/B.a2228l4.34.17d34265UJwThy">7天退换货</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>