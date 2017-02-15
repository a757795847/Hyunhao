var config = $("#config-json").data("json");
console.log(config);
// console.info("config: " + config);
wx.config({
    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: config.appid, // 必填，公众号的唯一标识
    timestamp: config.timestamp, // 必填，生成签名的时间戳
    nonceStr: config.nonceStr, // 必填，生成签名的随机串
    signature: config.signature,// 必填，签名，见附录1
    jsApiList: ['chooseImage', 'previewImage', 'uploadImage', 'downloadImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2

});
var ids = [];
//选择图片
$("#odd").on("click",function(){

    
    /*wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
            console.log(res);
            var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            console.log(localIds);

           

            localIds.map(function (id){
                console.log(id);
                wx.uploadImage({
                    localId: id, // 需要上传的图片的本地ID，由chooseImage接口获得
                    isShowProgressTips: 1, // 默认为1，显示进度提示
                    success: function (res) {
                        var serverId = res.serverId; // 返回图片的服务器端ID
                        console.log(serverId);

                            ids.push(serverId);
                        var image='<li class="lict"><img src="'+id+'" alt=""></li>';

                        if(ids.length == 3){
                            $("#odd").hide();
                        }

                        $("#odd").before(image);
                   
                    }
                });
            });
        }
    });*/
});


console.log(ids);

/*
$("#addID").on("click",function(){

    var orders=$("#Orders").val();
    var datas={
        "images":ids,
        "billno":orders
    }
    console.log(datas);
     $.ajax({
         type:'POST',
         url:"view/wechat/submit",
         data: datas,
         dataType: 'json',
         success:function(data){
            if(data.submit=="success"){
                $("#hide").hide();
                $("#Orders").hide();
                $("#addID").css("margin-bottom","96px");
                $("#addID").html("提交成功");

            }else{
                console.log("提交失败");

            }

         },
         error:function(jqXHR){
             if(jqXHR.status == 406){

             }
         }

     })


});*/


