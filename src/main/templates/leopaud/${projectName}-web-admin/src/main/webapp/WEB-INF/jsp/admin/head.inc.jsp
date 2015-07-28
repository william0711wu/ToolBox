<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="taglib.inc.jsp" %>
<h1 id="site-logo">
    <a href="/admin/index.do"><img src="/img/logo.png" alt="Site Logo"></a>
</h1>
<header id="header">
    <a href="javascript:;" data-toggle="collapse" data-target=".top-bar-collapse" id="top-bar-toggle" class="navbar-toggle collapsed">
        <i class="fa fa-cog"></i>
    </a>

    <a href="javascript:;" data-toggle="collapse" data-target=".sidebar-collapse" id="sidebar-toggle" class="navbar-toggle collapsed">
        <i class="fa fa-reorder"></i>
    </a>
</header>
<nav id="top-bar" class="collapse top-bar-collapse">
    <ul class="nav navbar-nav pull-left" style="display:none">
        <li class="">
            <a href="./index.html">
                <i class="fa fa-home"></i>
                		首页
            </a>
        </li>
        <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:;">
                下拉菜单 <span class="caret"></span>
            </a>
            <ul class="dropdown-menu" role="menu">
                <li><a href="javascript:;"><i class="fa fa-user"></i>&nbsp;&nbsp;某某字段</a></li>
                <li><a href="javascript:;"><i class="fa fa-calendar"></i>&nbsp;&nbsp;某某字段</a></li>
                <li class="divider"></li>
                <li><a href="javascript:;"><i class="fa fa-tasks"></i>&nbsp;&nbsp;某某字段</a></li>
            </ul>
        </li>
    </ul>
    <ul class="nav navbar-nav pull-right">
        <li class="">
            <a href="#">
           		     环境：<font color="red"><%=com.duowan.leopard.core.env.EnvUtil.getDuowanEnv()%></font>
            </a>
        </li>
        <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:;">
                <i class="fa fa-user"></i>
                <%=com.duowan.leopard.web.userinfo.util.RequestUtil.getSessionUsername(request) %>
                <span class="caret"></span>
            </a>

            <ul class="dropdown-menu" role="menu">
                <li style="display:none">
                    <a href="./page-settings.html">
                        <i class="fa fa-cogs"></i>
                        &nbsp;&nbsp;个人设置
                    </a>
                </li>
                <li class="divider"></li>
                <li>
                    <a href="/udb/logout.do">
                        <i class="fa fa-sign-out"></i>
                        &nbsp;&nbsp;退出
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</nav>