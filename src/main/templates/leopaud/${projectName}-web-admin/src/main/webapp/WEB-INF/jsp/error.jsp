<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%
response.setStatus(200);

String json = request.getParameter("json");
String ajax = request.getParameter("ajax");
if ("true".equals(json) || "true".equals(ajax)) {
	%>
	<!-- c:out value="${message }" escapeXml="true" /-->
	 <div class="pt-cuo cs-clear" style="text-align:center">
        <img src="/img/error.gif" style="margin:10px auto;display:block" /> 
        <a href="#" class="btn3 cs-clear" style="display:inline-block">
        	<span class="btn3_l"></span>
        	<span class="btn3_r" onclick="window.history.back(-2)">重新加载</span>
        </a>              
    </div>
	<%
	return;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content="zh-CN" http-equiv="Content-Language" />
<meta content="IE=EmulateIE7" http-equiv="X-UA-Compatible" />
<title>大厅</title>
<meta content="" name="Description" />
<meta content="" name="Keywords" />
<meta content="none" name="Robots" />
<link href="/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="home">
    
    <div class="pbd">
    
            <!--  -->
            <div class="pt-cuo cs-clear" style="text-align:center">
              <!-- 
              <div class="hd">错误提示！</div>
              <p><c:out value="${message }" escapeXml="true" /></p>
              <button onclick="window.history.back(-2)">返回</button>
               -->
               <img src="/img/error-2.gif" style="margin:10px auto;display:block" /> 
               <a href="#" class="btn3 cs-clear" style="display:inline-block">
               	<span class="btn3_l"></span>
               	<span class="btn3_r" onclick="window.history.back(-2)">重新加载</span>
               </a>
              
            </div>
            <!--  -->
    </div>
</div>
</body>
</html>

