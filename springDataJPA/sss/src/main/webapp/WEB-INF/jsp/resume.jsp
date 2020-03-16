<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>简历</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
</head>
<body>
<form id="form" action="/resume/findAll">
    <a href="/resume/add">添加</a>
    <table>
        <thead>
            <th>序号</th>
            <th>姓名</th>
            <th>地址</th>
            <th>电话</th>
            <th>操作</th>
        </thead>
        <tbody>
        <c:if test="${!empty resumes}">
            <c:forEach items="${resumes}" var="resume" varStatus="vs">
            <tr>
                <td>${vs.index+1}</td>

                <td>${resume.name}</td>
                <td>${resume.address}</td>
                <td>${resume.phone}</td>
                <td>
                    <a href="/resume/edit?id=${resume.id}">编辑</a>&nbsp;&nbsp;
                    <a href="/resume/delete?id=${resume.id}">删除</a>
                </td>
            </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</form>
</body>
</html>
