<div class="page-header md-shadow-z-1-i navbar navbar-fixed-top">
  <!-- BEGIN HEADER INNER -->
  <div class="page-header-inner">
    <!-- BEGIN LOGO -->
    <div class="page-logo">
        <a href="#">
        <img src="/assets/admin/layout/img/logo.png" alt="logo" class="logo-default"/>
        </a>
      <div class="menu-toggler sidebar-toggler hide">
        <!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
      </div>
    </div>
    <!-- END LOGO -->
    <!-- BEGIN RESPONSIVE MENU TOGGLER -->
    <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
    </a>
    <!-- END RESPONSIVE MENU TOGGLER -->
    <!-- BEGIN TOP NAVIGATION MENU -->
    <div class="top-menu">
      <ul class="nav navbar-nav pull-right">
        <li class="dropdown dropdown-user">
          <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
            <img alt="" class="img-circle" src="/assets/user.png">
					<span class="username username-hide-on-mobile">
					${requestScope._username}
                    </span>
            <i class="fa fa-angle-down"></i>
          </a>
          <ul class="dropdown-menu dropdown-menu-default">
            <li>
              <a href="#">
                <i class="icon-info"></i> 当前环境：<span class="badge badge-danger">
							<%=com.duowan.leopard.core.env.EnvUtil.getDuowanEnv()%> </span> </a>
            </li>
            <li>
              <a href="/udb/logout.do">
                <i class="icon-key"></i> 退出 </a>
            </li>
          </ul>
        </li>
        <li class="dropdown dropdown-quick-sidebar-toggler">
            <a href="/udb/logout.do" title="退出" class="dropdown-toggle">
            <i class="icon-logout"></i>
            </a>
        </li>
        <!-- END QUICK SIDEBAR TOGGLER -->
      </ul>
    </div>
    <!-- END TOP NAVIGATION MENU -->
  </div>
  <!-- END HEADER INNER -->
</div>