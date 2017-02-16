<%--
  Created by IntelliJ IDEA.
  User: admin5
  Date: 17/1/16
  Time: 上午10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script src="/javascripts/jquery.min.js"/>
    <script>
        $.ajax({
            type: 'POST',
            url: "http://open.izhuiyou.com/view/wechat/submit",
            data: datas,
            dataType: 'json',
            async:true,
            success: function (data) {



            },
            error: function () {

            }

        })

    </script>
  </head>
  <body>

  </body>
</html>
