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


function indexAjax(datas,pageState){
    $.ajax({
        type: 'post',
        url: '/order/list',
        data: JSON.stringify(datas),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            // console.log(data.list[0].giftState);
            console.log(data);
            var tbody='';

            var than='';
            var thao='';
            $.each(data.list,function(i,order){
                if(order.giftState==0){
                    order.giftState="未申领";

                }
                if(order.giftState==1){
                    order.giftState="审核中";
                    than="通过";
                    thao="驳回";

                }
              // for(var a=0;a<order.length;a++){
              //     console.log(2)
              //     if(order[a]==null){
              //         console.log(1)
              //         order[a]="";
              //     }
              //
              // }
                for(var key in order){
                    if(order[key] == null){
                        order[key] = '';
                    }
                }
                tbody +='<tr id="'+ order.id +'"><td><input type="checkbox" id="statistic"></td><td class="mailbox-date"><span>'+order.orderNumber+'</span></td>';
                tbody +='<td class="mailbox-date"><span class="label label-success"><a href="proxydetails.html">详情</span></a></td>';
                tbody +='<td class="mailbox-date"></td>';
                tbody +='<td class="mailbox-date"><span>'+order.applyDate+'</span></td>';
                tbody +='<td id="order_state" class="mailbox-date"><span class="state ' + order.id + '">'+order.giftState+'</span></td>';
                tbody +='<td class="mailbox-date"><span class="top">'+order.giftDetail+'</span></td>';
                tbody +='<td class="mailbox-date"><span>'+order.sendDate+'</span></td>';
                tbody +='<td class="mailbox-date"><span>'+order.recieveDate+'</span></td>';
                tbody +='<td class="mailbox-date"><span class="label label-success order_confirm_btn" data-index="' + order.id + '" >'+than+'</span>';
                tbody +='<span class="label label-success turn" data-toggle="modal" data-target="#myModal">'+thao+'</span></td></tr>';




            });
            $("#Table").find("tbody").html(tbody);






            if(pageState == 1){
                $("#jqueryPage").pagination({
                    count: data.page.count, //总数
                    size:data.page.pageSize, //每页数量
                    index: 1,//当前页
                    lrCount:1,//当前页左右最多显示的数量
                    lCount: 1,//最开始预留的数量
                    rCount: 1,//最后预留的数量
                    callback: function (options){
                        var index=options.index;
                        indexAjax({status:1,currentPageIndex:index});
                        //options.count = 300;
                        //return options;
                    },
                });
            }




        },
        error: function (jqXHR) {
            if (jqXHR.status == 406) {

            }
        }

    })
}
indexAjax({status:1,currentPageIndex:1},1);

$("#Table").on('click','.order_confirm_btn',function(){

    var orderIndex =$(this).data("index");
    var that = $(this);
    console.log(orderIndex);
    $.ajax({
        type: 'post',
        url: '/order/passAuditing/'+orderIndex,
        data: JSON.stringify({status:1,currentPageIndex:1}),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            if(data.status==1){
                $('.'+orderIndex).html("审核通过");
                // $(".label.label-success.turn").remove();
                var link='<span class="label label-success turn" data-toggle="modal" data-target="#hongbao"><a href="#modalt">发送红包</a></span>';
                console.log(that);
                that.parent().html(link);


            }

        }
    })
    $('#confim_l').attr('data-index',orderIndex);
});

$("#confim_l").on('click',function(){
    var order_index =$(this).data("index");

    var Idx='';

    for(var i=0;i< $("#span").find('.text-color').length;i++){

        if($("#span").find('.text-color').eq(i).hasClass('actives')){
            console.log($('#span .text-color').eq(i).attr('class'))
            Idx=(i+1)*100;
        }
    }



    var parameter={
        "id":order_index,
        "count":Idx
    }
   $.ajax({
        type: 'post',
        url:"/order/redSend",
        data: JSON.stringify(parameter),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
        $('#'+order_index+' .top').html("发送成功");
        $('#'+order_index+' .turn').remove();
       }
    })
});


 $(".order-discount-line .text-color.border-color").on("click",function(){
                $(this).addClass("actives");
                $(this).siblings().removeClass("actives");
 });


$('#basic-usage-demo').fancySelect();


$('#basic-time-demo').fancySelect();
$('#basic-status-demo').fancySelect();
$('#basic-details-demo').fancySelect();

$(".trigger").on('click',function(){
   var sub=$(this).html();
    if(sub=="导入时间"){
        $("div.fancy-select ul.options").css('left','180px');

    }else if(sub=="未申领"){
        $("div.fancy-select ul.options").css('left','360px');

    }else if(sub=="红包详情"){
        $("div.fancy-select ul.options").css('left','540px');

    }else{
        $("div.fancy-select ul.options").css('left','0');

    }




});

$("#clear").click(function(){
    $("#Text").val("");
});


