$('#basic-usage-demo').fancySelect();

$('#basic-time-demo').fancySelect();



$(".trigger").on('click',function(){
    var sub=$(this).html();
    if(sub=="授权状态"){
        $("div.fancy-select ul.options").css('left','180px');

    }else{
        $("div.fancy-select ul.options").css('left','0');

    }




});
