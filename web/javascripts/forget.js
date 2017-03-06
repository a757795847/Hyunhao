var phone='';
var Verification='';
$("#next_step").on("click",function(){
    if(phone==''){phone = $("#phone_pre").val();}
    console.log(phone);
    if($("#next_step").html()=="下一步") {
        $.ajax({
            type: 'post',
            url: ' /operator/checkUsername/' + phone,
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                console.log(data);
                if (data.status == "0") {
                    $("#next_step").html("重置密码");
                    $("#phone_pre").hide();
                    var tbody = '';
                    tbody += '<div class="input-group" style="margin-bottom:10px;">';
                    tbody += '<input type="text" class="form-control" placeholder="输入短信验证码" id="Verification">';
                    tbody += ' <span class="input-group-btn">';
                    tbody += ' <button class="btn btn-primary" id="getcode">获取验证码</button></span></div>';

                    $(".mb-housite").html(tbody);


                }

            },
            error: function (jqXHR) {
                if (jqXHR.status == 406) {

                }
            }

        })

    }else if($("#next_step").html()=="重置密码"){
       Verification=$("#Verification").val();
        $.ajax({
            type: 'post',
            url: ' /operator/checkVerificationCode/'+Verification,
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                console.log(data);
                if(data.status=="1") {
                    $("#next_step").html("确认");
                    $(".input-group").hide();
                    var body = '';
                    body = '<input type="password" class="form-control" placeholder="请输入新密码" id="password">';
                    $(".mb-housite").html(body);
                }
            },
            error: function (jqXHR) {
                if (jqXHR.status == 406) {

                }
            }

        })

    }else{


        var password=$("#password").val();
        var datas= {
            "phone":phone,
            "verificationCode":Verification,
            "password":password
        }
        console.log(datas);
        $.ajax({
            type: 'post',
            url: ' /operator/updatePassword',
            data: JSON.stringify(datas),
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                if(data.status=="1"){
                    $("#change_ok").modal("show");
                    $(".confirms").on("click",function () {
                        console.log("1");
                        window.location.href="/login";

                    })
                }else{
                    $("#change_ok").modal("show");
                    $(".sa-icon").css("display","block");
                    $("#change_ok img").css("display","none");
                    $("#change_ok h2").html("修改发生错误");

                }

               
            },
            error: function (jqXHR) {
                if (jqXHR.status == 406) {

                }
            }

        })

    }





});
$(".mb-housite").on("click",'#getcode',function () {
    console.log(1);
    $("#myModal").modal("show");
    settime(this);

});


var countdown=60;

function settime(obj) {
    if (countdown == 0) {
        obj.removeAttribute("disabled");
        $(obj).html("获取验证码");
        countdown = 60;
        return;
    } else {
        obj.setAttribute("disabled", true);
        $(obj).html('重新发送(' + countdown + ')');
        countdown --;
    }
    setTimeout(function() {
            settime(obj) }
        ,1000)
}

$("#verification_code").on('click','.Image',function(){
    $(".Image").remove();
    var date = Math.floor(Math.random()*20);
    $("#verification_code").prepend('<img src="/operator/getcodeImage?'+date+'" style="width:130px;height:53px;" class="Image">');
});

$(".confirm").on("click",function(){
    var phones=$("#phone_pre").val();
    var Input=$("#input_box").val();
    $.ajax({
        type:'POST',
        url:'/operator/verificationCode?phone='+phones+'&code='+Input,
        success:function(data){
            console.log(data);
        },
        error:function(jqXHR){
            console.log(jqXHR.status);
            if(jqXHR.status == 406){
            }
        }


    })

});

