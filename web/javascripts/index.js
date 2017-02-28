(function ($) {
    var  index = 1;
    var time = setInterval(function(){
        $('.bannerRound li').removeClass('active');
        $('.bannerRound li').eq(index).addClass('active');
        var left = -(index*1300);
        $('.banner').css('left',left)
        index ++;
        if(index == 3){
            index = 0;
        }
    },2000)

    $('.bannerRound li').on('click',function () {
        $('.bannerRound li').removeClass('active');
        $(this).addClass('active');
        index = $(this).attr('data-index');
    })

})(jQuery)
