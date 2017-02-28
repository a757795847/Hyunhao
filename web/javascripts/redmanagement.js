
//列表显示
function indexAjax(){
    $.ajax({
        type:'get',
        url:'/redstrategy/list',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success:function (data) {
            console.log(data);

            var tbody='';
            $.each(data.list,function(i,order){
                if(order.id < 10){
                    order.id="0"+order.id;
                }
               tbody +='<tr><td>'+order.id+'</td><td>'+order.name+'</td><td>'+order.money/100+'</td>';
               tbody +='<td><span class="label label-success edit" data-index="' + order.id + '">编辑</span>' +
                   '<span class="label label-success detele">删除</span></td></tr>';



            });
            $("#Table").find("tbody").html(tbody);

        }




    })
    
    
    
    
    
}

    indexAjax();
//编辑
$("#Table").on("click",'.edit',function () {
    var orderIndex =$(this).data("index");
    console.log(orderIndex);
   var edit_nub = $(this).parent().prev().text();
   var edit_name = $(this).parent().prev().prev().text();
    $("#name_update").val(edit_name);
    $("#nub_update").val(edit_nub);
    $('#confirm_update').attr('data-index',orderIndex);
    $('#myModas').modal('show');

});
//编辑后上传

$("#confirm_update").on("click",function () {
    var order_index =$(this).data("index");
    console.log(order_index);
   var name_update=$("#name_update").val();
    var nub_update=$("#nub_update").val();
    var datas={
        "id":parseInt(order_index),
        "name":name_update,
        "money":nub_update*100
    }
    console.log(datas)
    $.ajax({
        type:'post',
        url:'/redstrategy/update',
        data: JSON.stringify(datas),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success:function (data) {
            console.log(data);
            window.location.href="/redstrategy/home";
        }




    })


});

$("#Table").on("click",'.detele',function () {
    var delete_index =$(this).prev().data("index");
    console.log(delete_index);
    $.ajax({    
        type:'detele',
        url:'/redstrategy/remove',      
        data: JSON.stringify(delete_index),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success:function (data) {
            console.log(data);
            window.location.href="/redstrategy/home";

        }




    })


});
//新增
$("#confirm_add").on("click",function () {

    var add_name=$("#name").val();
    var add_nub=$("#nub").val();
    var datas={
        "name":add_name,
        "money":add_nub*100
    }
    $.ajax({type:'post',
        url:'/redstrategy/add',
        data: JSON.stringify(datas),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success:function (data) {
            console.log(data);  
        }




    })


});


