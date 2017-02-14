$(function () {
    $('#enddate').datepicker({
        autoclose: true
    });
});

$("#modify").click(function(){
    $(".custom-panel").css("display","block");

});
$(".btn.btn-block.btn-default").click(function(){
    $(".custom-panel").css("display","none");

});

$(".btn-group.pull-right").on('click','button',function() {
    console.log($(this));
    $(this).addClass('active');
    $(this).siblings().removeClass('active');
    $(this).css("background-color","#fff");


});


        $("#addId").on("click", function () {
            var sub = $("#addId").html();
            if (sub == '+ 添加公众号') {                                                                                                                                            
                $("#addId").html("< 公众号列表");
                $("#lich").css("display", "block");
                $("#bottom").css("display", "none");
                $("#Right").css("display", "none");


            } else {
                $("#addId").html("+ 添加公众号");
                $("#lich").css("display", "none");
                $("#bottom").css("display", "block");
                $("#Right").css("display", "block");

            }
        });
 
//提示框

$("#searchfor").on("click",function(){
    var lich=1;
    if(lich=1){
        $(".sa-warning").css("border-color","#C9DAE1");
        $(".sa-body").css("background-color","#C9DAE1");
        $(".sa-dot").css("background-color","#C9DAE1");

    }else if(lich=2){
        $(".sa-warning").css("border-color","#F27474");

    }else{
        $(".sa-warning").css("border-color","#ccc");
    }



});


$(".duration").on("click","li",function(){
        $(".duration li").addClass("liclass");
       $(this).nextAll().removeClass("liclass");

});

