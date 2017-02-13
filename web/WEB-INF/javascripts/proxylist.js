$(".checkbox-toggle").click(function () {
    var clicks = $(this).data('clicks');
    if (clicks) {
        //Uncheck all checkboxes
        $(".mailbox-messages input[type='checkbox']").iCheck("uncheck");
        $(".fa", this).removeClass("fa-check-square-o").addClass('fa-square-o');
    } else {
        //Check all checkboxes
        $(".mailbox-messages input[type='checkbox']").iCheck("check");
        $(".fa", this).removeClass("fa-square-o").addClass('fa-check-square-o');
    }
    $(this).data("clicks", !clicks);
});

$("#jqueryPage").pagination({
    count: 100, //总数
    size:20, //每页数量
    index: 1,//当前页
    lrCount: 5,//当前页左右最多显示的数量
    lCount: 1,//最开始预留的数量
    rCount: 1,//最后预留的数量
    callback: function () {
        //options.count = 300;
        //return options;
    },
});


$('#basic-usage-demo').fancySelect();


$('#basic-time-demo').fancySelect();
$('#basic-status-demo').fancySelect();
$('#basic-details-demo').fancySelect();

$(".trigger").on('click',function(){
   var sub=$(this).html();
    if(sub=="导入时间"){
        $("div.fancy-select ul.options").css('left','180px');

    }else if(sub=="状态"){
        $("div.fancy-select ul.options").css('left','360px');

    }else if(sub=="红包详情"){
        $("div.fancy-select ul.options").css('left','540px');

    }else{
        $("div.fancy-select ul.options").css('left','0');

    }




});

$("#clear").click(function(){
    console.log("1");
    $("#Text").val("");
});


var reds=true;
$(".text-color.border-color").click(function(){
    if(reds) {
        $(this).addClass("redbag");
        reds=false;
    }else{
        $(this).removeClass("redbag");  

    }

});
    //$(this).css("background-color","#2ca2fb");



// Boilerplate
