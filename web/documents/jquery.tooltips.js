/*******************************************/
/**      http://www.thinkcart.net        **/
/******************************************/

this.tooltipsPreview = function(){

    var xOffset = 20;
    var yOffset = 20;
    var winHeight = $(window).height();
    var winWidth = $(window).width();
    var obj;
    var objHeight = 0;
    var objWidth = 0;

    $(window).resize(function(){
        winHeight = $(window).height();
        winWidth =  $(window).width();
    });

    var setHover = function(e){
        if(objHeight == 0) objHeight = $(obj).find('iframe').height();
        var top = (e.pageY + yOffset);
        var left = (e.pageX + xOffset);
        if(e.pageY > winHeight/2) top = (e.pageY - objHeight - yOffset);
        if(e.pageX > winWidth/2) left = (e.pageX - objWidth - xOffset);
        $(obj).css({'top':top,'left':left});
        if( $(obj).css('visibility') == 'hidden' ){
            $(obj).css({'visibility':'visible'}).fadeIn('fast');
        }
    };

    $('.tooltips').hover(function(e){
        var pic = $(this).attr('src') || $(this).attr('value') || $(this).attr('href');
        if(!pic) return;
        var width = parseInt($(this).attr('_width'));
        if(!width) width = 200;
        objWidth = width + 2;
        $('body').append('<div id="tooltips" style="position:absolute;top:0;left:0;visibility:hidden;color:#fff;z-index:10001;">' +
            '<img style="position:absolute;left:0;top:0;z-index:10003;border:1px solid #333;" width="'+ width +'" src="'+ pic +'" />' +
            '<iframe width="'+ objWidth +'" style="position:absolute;border:none;filter:alpha(opacity=0);-moz-opacity:0;-khtml-opacity: 0;opacity:0;z-index:10002;"></iframe></div>');
        var top = (e.pageY + yOffset);
        var left = (e.pageX + xOffset);
        obj = $('#tooltips');
        $(obj).find('img').load(function(){ //如果图片从缓存读取，有的浏览器（如chrome）这里不会执行，所以后面再加一个setHover
            objHeight = $(this).height()+2;
            $(obj).find('iframe').height( objHeight );
            setHover(e);
        });
        setHover(e);
    },
    function(){
        $(obj).remove();
    });
    
    $('.tooltips').mousemove(function(e){
        setHover(e);
    });
};

$(window).load(function(){
    tooltipsPreview();
});