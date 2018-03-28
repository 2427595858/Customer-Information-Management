<%--
  Created by IntelliJ IDEA.
  User: 光玉
  Date: 2018/3/25
  Time: 11:22
  To change this template use File | Settings | File Templates.
--%>
<%-- 每个jsp文件中都加入下面两句 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    <%--注意：需要放一个jstl.jar包到Tomcat的WEB-INF/lib下--%>

<html>
<head>
    <title>Home</title>
</head>

<%-- 将页面布局为上下两部分，上面放置top.jsp，下面放置show.jsp --%>

<frameset rows="20%,*">
    <frame src="<c:url value='/top.jsp'/>" name="top">
    <frame src="<c:url value='/show.jsp'/>" name="main"/>
</frameset>

</html>
