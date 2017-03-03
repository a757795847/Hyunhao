$(function(){

    function showimg(url){
      /*  var img='<img src="'+url+'"/>';*/
        console.log(url);
        $('.images_go').attr('src',url);
        $('.images_go').css("zIndex","999");
    }
    function addfile(){
        showimg(window.URL.createObjectURL(this.files[0]));
  /*      $(this).hide();*/
        $('#tops').bind('change',addfile);
    }
    $('#tops').bind('change',addfile);



    function showimgs(url){
      /*  var imgs='<img src="'+url+'" style="width:100%;height:140px;"/>';*/

        $('.images_li').attr('src',url);
        $('.images_li').css("zIndex","999");
    }
    function addfiles(){
        showimgs(window.URL.createObjectURL(this.files[0]));
        /*      $(this).hide();*/
        $('#bottoms').bind('change',addfiles);
    }
    $('#bottoms').bind('change',addfiles);

    
    
    
    $("#click").on("click",function(e){
        console.log($('#upfile_list').html());
        e.preventDefault();
        console.log(new FormData($('#uploadForm')[0]));
        $.ajax({
            url: '/proxy/uploadPayQR',
            type: 'POST',
            cache: false,
            dataType: 'json',
            data: new FormData($('#uploadForm')[0]),
            processData: false,
            contentType: false
        }).done(function(res) {
            console.log(res);




        }).fail(function(res) {});





    });

})