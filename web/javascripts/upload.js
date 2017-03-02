$(function(){

    function showimg(url){
        var img='<img src="'+url+'" style="width:100%;height:140px;"/>';
        $('#upfile_list').append(img);
    }
    function addfile(){
        showimg(window.URL.createObjectURL(this.files[0]));
  /*      $(this).hide();*/
        $('#tops').bind('change',addfile);
    }
    $('#tops').bind('change',addfile);



    function showimgs(url){
        var img='<img src="'+url+'" style="width:100%;height:140px;"/>';
        $('#upfile_kich').append(img);
    }
    function addfiles(){
        showimgs(window.URL.createObjectURL(this.files[0]));
        /*      $(this).hide();*/
        $('#bottoms').bind('change',addfiles);
    }
    $('#bottoms').bind('change',addfiles);

    
    
    
    $("#click").on("click",function(e){
        e.preventDefault();

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
            console.log(res);



        }).fail(function(res) {});





    });

})