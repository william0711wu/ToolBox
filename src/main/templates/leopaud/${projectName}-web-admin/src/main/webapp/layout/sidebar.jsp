<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="page-sidebar-wrapper">
  <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
  <!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
  <div class="page-sidebar navbar-collapse collapse">
    <!-- BEGIN SIDEBAR MENU -->
    <!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
    <!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
    <!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
    <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
    <!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
    <!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
    <ul class="page-sidebar-menu page-sidebar-menu-light " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
      <!-- DOC: To remove the sidebar toggler from the sidebar you just need to completely remove the below "sidebar-toggler-wrapper" LI element -->
      <li class="sidebar-toggler-wrapper">
        <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
        <div class="sidebar-toggler">
        </div>
        <!-- END SIDEBAR TOGGLER BUTTON -->
      </li>
      <%--<!-- DOC: To remove the search box from the sidebar you just need to completely remove the below "sidebar-search-wrapper" LI element -->--%>
      <%--<li class="sidebar-search-wrapper">--%>
      <%--<!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->--%>
      <%--<!-- DOC: Apply "sidebar-search-bordered" class the below search form to have bordered search box -->--%>
      <%--<!-- DOC: Apply "sidebar-search-bordered sidebar-search-solid" class the below search form to have bordered & solid search box -->--%>
      <%--<form class="sidebar-search " action="extra_search.html" method="POST">--%>
      <%--<a href="javascript:;" class="remove">--%>
      <%--<i class="icon-close"></i>--%>
      <%--</a>--%>
      <%--<div class="input-group">--%>
      <%--<input type="text" class="form-control" placeholder="Search...">--%>
      <%--<span class="input-group-btn">--%>
      <%--<a href="javascript:;" class="btn submit"><i class="icon-magnifier"></i></a>--%>
      <%--</span>--%>
      <%--</div>--%>
      <%--</form>--%>
      <%--<!-- END RESPONSIVE QUICK SEARCH FORM -->--%>
      <%--</li>--%>
      <li class="start" class="active open">
        <a href="/index">
          <i class="icon-home"></i>
          <span class="title">首页</span>
        </a>
      </li>
      <c:forEach items="${_userFuncList}" var="func" >
        <li>
          <a href="javascript:;">
            <i class="icon-folder-alt"></i>
            <span class="title">${func.name}</span>
            <span class="arrow "></span>
          </a>
          <ul class="sub-menu">
            <c:forEach items="${func.childFuncs}" var="childFunc" >
              <li>
                <a href="${childFunc.desc}">
                  <i class="icon-docs"></i>
                    ${childFunc.name}</a>
              </li>
            </c:forEach>
          </ul>
        </li>
      </c:forEach>


    </ul>
    <!-- END SIDEBAR MENU -->
  </div>
</div>