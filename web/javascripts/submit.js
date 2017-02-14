var config = $("#config-json").data("json");
console.log(config);
// console.info("config: " + config);
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: config.appId, // 必填，公众号的唯一标识
    timestamp: config.timestamp, // 必填，生成签名的时间戳
    nonceStr: config.nonceStr, // 必填，生成签名的随机串
    signature: config.signature,// 必填，签名，见附录1
    jsApiList: ['chooseImage', 'previewImage', 'uploadImage', 'downloadImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2

});

//选择图片
wx.chooseImage({
    count: 1, // 默认9
    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
    success: function (res) {
        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片

        // console.info("success");
        //upload image to wechat server

        localIds.map(function (id){
            wx.uploadImage(
                localId: id, // 需要上传的图片的本地ID，由chooseImage接口获得
                isShowProgressTips: 1, // 默认为1，显示进度提示
                success: function (res) {
                var serverId = res.serverId; // 返回图片的服务器端ID


                /*outer.state.images[position] = serverId;
                 outer.state.images_display[position] = outer.state.source_tempfile + serverId;
                 outer.setState({});*/
                // console.info("serverId: " + serverId);
                // window.location = "/" + serverId;

                // notify server to fetch image
                // $.post(outer.state.source + serverId, {}, function (res) {
                //     alert(res.result);
                //     alert(res.result.image);
                //     console.info(res);
                //     var newData = outer.state.data;
                //     newData.imagePath = res.result.image;
                //     outer.setState({data: newData})
                // });
            }
        });
    })
}
});



var sub=serverId;

$("addID").on("click",function(){
    var orders=$("$Orders").val();
    var datas={
        "images":sub,
        "billno":orders

    }
    $.ajax({
        type:'POST',
        url:"view/wechat/submit",
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


