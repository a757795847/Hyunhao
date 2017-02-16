var config = $("#config-json").data("json");
console.log(config);
// console.info("config: " + config);
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: config.appid, // 必填，公众号的唯一标识
    timestamp: config.timestamp, // 必填，生成签名的时间戳
    nonceStr: config.nonceStr, // 必填，生成签名的随机串
    signature: config.signature,// 必填，签名，见附录1
    jsApiList: ['chooseImage', 'previewImage', 'uploadImage', 'downloadImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2

});
var ids = [];
//选择图片
$("#odd").on("click",function(){

    
    wx.chooseImage({
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
    });
});
/*$("#form img").on("click",function(){
    var src=this.src;
    wx.previewImage({
        current: '', // 当前显示图片的http链接
        urls: [] // 需要预览的图片http链接列表
    });

});*/



console.log(ids);

$("#addID").on("click",function(){
    var HTML=$("#addID").html();
    var orders=$("#Orders").val();
    var datas={
        "image1":ids[0],
        "image2":ids[1],
        "image3":ids[2],
        "billno":orders
    }
    if(HTML=="马上提交") {
        if (orders == '' || ids[0] == '') {
            $.alert('请上传图片和订单号');
        } else {
            $.ajax({
                type: 'POST',
                url: "http://open.izhuiyou.com/view/wechat/submit",
                data: datas,
                dataType: 'json',
                success: function (data) {
                    if (data.status == "1") {
                        $("#text").css("display", "block");
                        $("#hide").hide();
                        $("#Orders").hide();
                        $("#addID").html("点击关闭");
                        $("#addID").css({
                            "backgroundColor": "#a20510",
                            "border": '1px solid #ffe9b8',
                            "color": "#ffe9b8",
                            "margin-top": "3%"
                        });
                        $("#addID").html("点击关闭");
                        $(".father").css("margin-top", "11%");

                    } else {
                        $.alert(data.message);
                        console.log(data.message);


                    }

                },
                error: function (jqXHR) {
                    if (jqXHR.status == 406) {

                    }
                }

            })
        }
    }else{
        WeixinJSBridge.call('closeWindow');

    }

});


