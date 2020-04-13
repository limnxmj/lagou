<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>简历</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#btn").click(function(){
                $("#form").submit();
            });
        });

    </script>
</head>
<body>
<form id="form" action="${pageContext.request.contextPath}/resume/save">
    <div>姓名：<input type="text" name="name" value="${resume.name}"/></div>
    <div>地址：<input type="text" name="address" value="${resume.address}"/></div>
    <div>电话：<input type="text" name="phone" value="${resume.phone}"/></div>
    <div>
        <input type="button" id="btn" value="提交"/>
        <input type="hidden" name="id" value="${resume.id}"/>
    </div>
</form>
</body>
</html>
