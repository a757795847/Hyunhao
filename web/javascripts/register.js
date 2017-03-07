




 var countdown=60;
 $("#getcode").on("click",function(){
     var mobile=$("#mobile").val();
     var reg = /^1[0-9]{10}$/;
     var flag = reg.test(mobile);
     console.log("2");
     if(flag){
         console.log("1");
         $('#myModal').modal('show');
         settime(this);
     }else{
        $("#lookModal").modal('show');
     }
 });


 function settime(obj) {
     if (countdown == 0) {
         obj.removeAttribute("disabled");
         $(obj).html("获取验证码");
         countdown = 60;
         return;
     } else {
         obj.setAttribute("disabled", true);
         $(obj).html('重新发送(' + countdown + ')');
         countdown--;
     }
     setTimeout(function () {
             settime(obj)
         }
         , 1000)
 }

 $("#verification_code").on('click', '.Image', function () {
     $(".Image").remove();
     var date = Math.floor(Math.random() * 20);
     $("#verification_code").prepend('<img src="/operator/getcodeImage?' + date + '" style="width:130px;height:53px;" class="Image">');
 });
 var Verification = '';

 $(".confirm").on("click", function () {
     var phone = $("#mobile").val();
     var Input = $("#input_box").val();
     $.ajax({
         type: 'POST',
         url: '/operator/verificationCode?phone=' + phone + '&code=' + Input,
         success: function (data) {
             console.log(data);
         },
         error: function (jqXHR) {
             console.log(jqXHR.status);
             if (jqXHR.status == 406) {
             }
         }


     })

 });


 $("#addID").on("click", function () {
     var phone_number = $("#mobile").val();
     var password = $("#password").val();
     var passwords = $("#passwords").val();
     var Verification = $("#Verification").val();
     console.log(Verification);


     var datas = {
         "phone": phone_number,
         "verificationCode": Verification,
         "password": password
     }
     console.log(datas);
     var mobiles = $("#mobile").val();
     var regs = /^1[0-9]{10}$/;
     var flags = regs.test(mobiles);
     var leg = /^[0-9a-zA-Z]+$/;
     if (flags == false) {
         $("#Prompt").html("手机号有误!")
         $("#lookModal").modal('show');
     } else if (Verification == '') {
         $("#Prompt").html("验证码不能为空!")
         $("#lookModal").modal('show');

     } else if (password.length < 6) {
         $("#Prompt").html("密码不能少于6位!");
         $("#lookModal").modal('show');
     } else if (!leg.test(password)) {
         $("#Prompt").html("密码只能由数字和字母组成!");
         $("#lookModal").modal('show');

     } else if (password !== passwords) {
         $("#Prompt").html("请确认密码输入相同!");
         $("#lookModal").modal('show');
     } else {
         $.ajax({
             type: 'POST',
             url: '/operator/register',
             data: JSON.stringify(datas),
             dataType: 'json',
             contentType: 'application/json;charset=UTF-8',
             success: function (data) {
                 console.log(data);
             },
             error: function (jqXHR) {
                 console.log(jqXHR.status);
                 if (jqXHR.status == 406) {
                 }
             }


         })
     }
 });