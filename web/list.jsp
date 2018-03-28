<%--
  Created by IntelliJ IDEA.
  User: 光玉
  Date: 2018/3/25
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>客户列表</title>
</head>
<body>
    <!-- 建表 -->
    <h3 align="center">客户列表</h3>
    <table border="1" width="70%" align="center">
        <tr>
            <th>姓名</th>
            <th>性别</th>
            <th>电话</th>
            <th>邮箱</th>
            <th>个人描述</th>
            <th>操作</th>
        </tr>
        <!-- JSP中使用JSTL的c标签 -->
        <c:forEach items="${pb.beanList}" var="cstm">
            <tr>
                <td>${cstm.name}</td>
                <td>${cstm.gender}</td>
                <td>${cstm.phone}</td>
                <td>${cstm.email}</td>
                <td>${cstm.description}</td>
                <td>
                    <a href="<c:url value='/CustomerServlet?method=preEdit&id=${cstm.id}'/>">编辑</a>
                    <a href="<c:url value='/CustomerServlet?method=delete&id=${cstm.id}'/>">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <br/>

    <%-- 分页 --%>
    <div style="text-align: center">
        第${pb.pageNum}页/共${pb.totalPage}页
        <a href="${pb.url}&pageNum=1">首页</a>
        <c:if test="${pb.pageNum>1}">
            <a href="${pb.url}&pageNum=${pb.pageNum-1}">上一页</a>
        </c:if>

        <%-- 计算页数，赋给变量begin和end --%>
        <c:choose>
            <%-- 如果总页数不超过10页，则将所有页数都显示出来 --%>
            <c:when test="${pb.totalPage<=10}">
                <c:set var="begin" value="1"/>
                <c:set var="end" value="${pb.totalPage}"/>
            </c:when>

            <%-- 当总页数超过10时，通过公式计算begin和end的值 --%>
            <c:otherwise>
                <c:set var="begin" value="${pb.pageNum-5}"/>
                <c:set var="end" value="${pb.pageNum+4}"/>
                <%-- 头溢出 --%>
                <c:if test="${begin<1}">
                    <c:set var="begin" value="1"/>
                    <c:set var="end" value="10"/>
                </c:if>
                <%-- 尾溢出 --%>
                <c:if test="${end>pb.totalPage}">
                    <c:set var="begin" value="${pb.totalPage-9}"/>
                    <c:set var="end" value="${pb.totalPage}"/>
                </c:if>
            </c:otherwise>
        </c:choose>

        <%-- 循环遍历页码列表 --%>
        <c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i eq pb.pageNum}">  <%-- 在方框中显示当前页码 --%>
                    [${i}]
                </c:when>
                <c:otherwise>
                    <a href="${pb.url}&pageNum=${i}">[${i}]</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <%-- 给出下一页以及尾页链接 --%>
        <c:if test="${pb.pageNum<pb.totalPage}">
            <a href="${pb.url}&pageNum=${pb.pageNum+1}">下一页</a>
        </c:if>
        <a href="${pb.url}&pageNum=${pb.totalPage}">尾页</a>
    </div>
</body>
</html>
