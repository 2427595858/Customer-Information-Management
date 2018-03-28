<%--
  Created by IntelliJ IDEA.
  User: 光玉
  Date: 2018/3/25
  Time: 23:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h3 align="center">高级搜索</h3>
    <form action="<c:url value='/CustomerServlet' />" method="get">
        <input type="hidden" name="method" value="query">
        <table border="0" align="center" width="40%" style="margin-left: 100px">
            <tr>
                <td width="100px">客户姓名</td>
                <td width="40%">
                    <input type="text" name="name">
                </td>
            </tr>
            <tr>
                <td>客户性别</td>
                <td>
                    <select name="gender">
                        <option value="">==请选择性别==</option>
                        <option value="male">male</option>
                        <option value="female">female</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>电话</td>
                <td>
                    <input type="text" name="phone">
                </td>
            </tr>
            <tr>
                <td>邮箱</td>
                <td>
                    <input type="text" name="email">
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" value="搜索">
                    <input type="reset" value="重置">
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
