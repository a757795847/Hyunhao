$("#btn").on("click", function () {

    var username = $("#username").val();
    var password = $("#password").val();

    var datas = {
        "username": username,
        "password": password
    }
    if(username==''){
        $("#Prompt").html("请输入用户名");
        $("#lookModal").modal('show');
    }else if(password==''){
        $("#Prompt").html("密码不能为空");
        $("#lookModal").modal('show');
    }else {
        $.ajax({
            type: 'POST',
            url: '/login',
            data: JSON.stringify(datas),
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                if (data.status == '0') {
                    $("#Prompt").html(data.message);
                    $("#lookModal").modal('show');

                } else {
                    location.href = data.url;
                }
                console.log(data);
                console.log(data.url);

            },
            error: function (jqXHR) {
                if (jqXHR.status == 406) {

                }
            }

        })
    }


});


