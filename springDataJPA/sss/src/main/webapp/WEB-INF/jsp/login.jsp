<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" >
        $(document).ready(function () {
            $("#btn").bind("click", function () {
                $("#errMsg").html("");
                var username = $("#username").val();
                var password = $("#password").val();
                $.ajax({
                    type: "post",
                    url: "/loginCheck",
                    data: {
                        username: username,
                        password: password
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            $("#errMsg").html(data.message);
                        } else {
                            $("#form").submit();
                        }
                    }
                });
            });
        });
    </script>
</head>
<body>
    <form id="form" action="/doLogin">
        <div>用户名：<input type="text" name="username" id="username"/></div>
        <div>密码：<input type="password" name="password" id="password"/></div>
        <div><span id="errMsg" style="color:red;"></span></div>
        <div><input type="button" id="btn" value="提交"/></div>
    </form>
</body>
</html>
