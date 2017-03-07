$("#send_confirm").on("click",function () {
    var oldpassword=$("#old_password").val();
    var newpassword=$("#new_password").val();
    var datas={
       "oldpassword":oldpassword,
       "newpassword":newpassword,
    }
    console.log(datas);
    var newpasswords = $("#new_passwords").val();
    if (newpassword != newpasswords) {
        $("#change_pass").modal("show");
        $(".sa-icon").css("display", "block");
        $("#change_pass img").css("display", "none");
        $("#change_pass h2").html("两次密码输入不一样");
    } else {

        $.ajax({
            type: 'POST',
            url: '/operator/innerUpdatePassword',
            data: JSON.stringify(datas),
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                console.log(data);
                if (data.status == "1") {
                    $("#change_pass").modal("show");
                } else {
                    $("#change_pass").modal("show");
                    $(".sa-icon").css("display", "block");
                    $("#change_pass img").css("display", "none");
                    $("#change_pass h2").html("修改密码有误");
                }

            },
            error: function (jqXHR) {
                console.log(jqXHR.status);
                if (jqXHR.status == 406) {
                }
            }


        })
    }

})
