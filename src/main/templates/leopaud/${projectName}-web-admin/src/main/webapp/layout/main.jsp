<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>系统名称</title>
    <%--<!-- BEGIN GLOBAL MANDATORY STYLES -->--%>
    <link rel="stylesheet" href="/assets/global/plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="/assets/global/plugins/simple-line-icons/simple-line-icons.min.css">
    <link rel="stylesheet" href="/assets/global/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/assets/global/plugins/uniform/css/uniform.default.css">
    <link rel="stylesheet" href="/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css">
    <%--<!-- BEGIN PAGE LEVEL STYLES -->--%>
    <sitemesh:write property="page.pagerRefCss"/>
    <%--<!-- BEGIN THEME STYLES -->--%>
    <link rel="stylesheet" href="/assets/global/css/components-md.css">
    <link rel="stylesheet" href="/assets/global/css/plugins-md.css">
    <link rel="stylesheet" href="/assets/admin/layout/css/layout.css">
    <link rel="stylesheet" href="/assets/admin/layout/css/themes/darkblue.css">
    <link rel="stylesheet" href="/assets/admin/layout/css/custom.css">
    <sitemesh:write property='head'/>
    <link rel="shortcut icon" href="/assets/favicon.ico"/>
</head>
<body class="page-md page-header-fixed page-quick-sidebar-over-content">
<!-- BEGIN HEADER -->
<%@include file="/layout/header.jsp" %>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN SIDEBAR -->
    <%@include file="/layout/sidebar.jsp" %>
    <!-- END SIDEBAR -->
    <!-- BEGIN CONTENT -->
    <sitemesh:write property='body'/>
    <!-- END CONTENT -->
    <!-- BEGIN QUICK SIDEBAR -->
    <!-- END QUICK SIDEBAR -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<div class="page-footer">
    <div class="page-footer-inner">

    </div>
    <div class="scroll-to-top">
        <i class="icon-arrow-up"></i>
    </div>
</div>
<!-- END FOOTER -->
<%--<!-- BEGIN CORE PLUGINS -->--%>
<!--[if lt IE 9]>
<script src="/assets/global/plugins/respond.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/excanvas.min.js" type="text/javascript" ></script>
<![endif]-->
<script src="/assets/global/plugins/jquery.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/jquery-migrate.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/jquery.blockui.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/jquery.cokie.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript" ></script>
<script src="/assets/global/plugins/bootstrap-growl/jquery.bootstrap-growl.min.js"></script>
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="/assets/global/scripts/metronic.js" type="text/javascript" ></script>
<script src="/assets/admin/layout/scripts/layout.js" type="text/javascript" ></script>
<script src="/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript" ></script>
<script src="/assets/admin/layout/scripts/demo.js" type="text/javascript" ></script>
<script src="/assets/js/utils.js" type="text/javascript" ></script>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<%--引用页面js引用内容，使期添加在模板js之后--%>
<sitemesh:write property="page.pagerRefJs"/>

<script>
    jQuery(document).ready(function() {
        Metronic.init(); // init metronic core components
        Layout.init(); // init current layout
        QuickSidebar.init(); // init quick sidebar
        Demo.init(); // init demo features
    });
</script>
<%--%{--引用页面自定义js内容--}%--%>
<sitemesh:write property="page.pagerCustomJs"/>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>