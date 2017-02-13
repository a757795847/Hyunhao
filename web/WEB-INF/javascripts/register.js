 $("#addID").on("click",function(){

    var mobile = $("#mobile").val();
    var Verification = $("#Verification").val();
    var password = $("#password").val();



var datas= {
    "mobile":mobile,
    "smscode":Verification,
    "password":password
}

     console.log(datas);
$.ajax({
    type:'POST',
    url:basePath+'/user/register',
    data: datas,
    success:function(data){
        console.log(data);
    },
    error:function(jqXHR){
        console.log(jqXHR.status);
        if(jqXHR.status == 406){
            console.log("post");
        }
    }

})

 });
 var countdown=60;
 $("#getcode").on("click",function(){
     console.log(1)
     settime(this);







 });



 function settime(obj) {
     if (countdown == 0) {
         obj.removeAttribute("disabled");
         obj.value="获取验证码";
         countdown = 60;
         return;
     } else {
         obj.setAttribute("disabled", true);
         obj.value="重新发送(" + countdown + ")";
         countdown--;
     }
     setTimeout(function() {
             settime(obj) }
         ,1000)
 }
