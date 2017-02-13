  console.log(basePath)



  $("#btn").on("click",function(){

    var username = $("#username").val();
    var password = $("#password").val();

    var datas= {
      "username":username,
      "password":password
    }

    $.ajax({
      type:'POST',
      url:basePath+'/user/login',
      data: datas,
      dataType: 'json',
      success:function(data){

        console.log(data);
        console.log(data.url);
       location.href=basePath+data.url;
      },
      error:function(jqXHR){
        if(jqXHR.status == 406){

        }
      }

    })


  });


