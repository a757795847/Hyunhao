$(function () {
    $('#Import_date').datepicker({
        autoclose: true
    });
});

$(function () {
    $('#user_date').datepicker({
        autoclose: true
    });
});


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

var dataList =[];

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
            dataList = data.list;

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
                tbody +='<tr id="'+ order.id +'"><td><input type="checkbox" class="statistic"></td><td class="mailbox-date"><span>'+order.orderNumber+'</span></td>';
                tbody +='<td class="mailbox-date"><span class="label label-success proxylist_details" data-index="'+ i +'">详情</span></td>';
                tbody +='<td class="mailbox-date"><ul class="images"><li><img src="../images/125.jpg" alt=""></li>' +
                    '<li><img src="../images/3.jpg" alt=""></li>' +
                    '<li><img src="../images/125.jpg" alt=""></li></ul></td>';
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
indexAjax({status:0,currentPageIndex:1},1);

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
                var link='<span class="label label-success turn" data-toggle="modal" data-target="#myModals">发送红包</span>';
                console.log(that);
                that.parent().html(link);


            }

        }
    })
    $('#send').attr('data-index',orderIndex);
});

$("#send").on('click',function(){
    var order_index =$(this).data("index");
        console.log(order_index);
    var Idx='';

    for(var i=0;i< $("#span").find('.text-color').length;i++){

        if($("#span").find('.text-color').eq(i).hasClass('actives')){
            console.log($('#span .text-color').eq(i).attr('class'))
            Idx=(i+1)*100;
        }
    }


    console.log(Idx);
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
        $('#'+order_index+' #order_state span').html("红包发送成功");
        $('#'+order_index+' .top').html(Idx/100+'元红包');
        $('#'+order_index+' .turn').remove();
       }
    })
});

$("#Table").on('click',".proxylist_details",function(){
    var order_index =$(this).data("index");
       /* data[order_index]*/
    console.log(dataList);
    console.log(dataList[order_index]);
        $("#order_number").html(dataList[order_index].orderNumber);
        $("#buyer_name").html(dataList[order_index].buyerName);
        $("#order_create_time").html(dataList[order_index].orderCreateTime);
        $("#order_pay_time").html(dataList[order_index].orderPayTime);
        $("#buyer_zhifubao").html(dataList[order_index].buyerZhifubao);
        $("#amount").html(dataList[order_index].amount);
        $("#receiver").html(dataList[order_index].receiver);
        $("#receiver_address").html(dataList[order_index].receiverAddress);
        $("#receiver_mobile").html(dataList[order_index].receiverMobile);

    $('#isModal').modal('show');



});


 $(".order-discount-line .text-color.border-color").on("click",function(){
                $(this).addClass("actives");
                $(this).siblings().removeClass("actives");
 });


$("#basic-time-demo").select2();
$("#basic-status-demo").select2();
$("#basic-details-demo").select2();


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

function filterAjax(datas,pageState){
    $.ajax({
        type: 'post',
        url: '/order/list',
        data: JSON.stringify(datas),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            // console.log(data.list[0].giftState);
            console.log(data);
            dataList = data.list;

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
                tbody +='<td class="mailbox-date"><span class="label label-success proxylist_details" data-index="'+ i +'">详情</span></td>';
                tbody +='<td class="mailbox-date"><ul class="images"><li><img src="../images/125.jpg" alt=""></li>' +
                    '<li><img src="../images/3.jpg" alt=""></li>' +
                    '<li><img src="../images/125.jpg" alt=""></li></ul></td>';
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
                        filterAjax({status:datas.status,currentPageIndex:index});
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

$("#filter").on("click",function(){
    console.log(1)
    var state_r='';
    var receive=$("#basic-status-demo").val();
    console.log(receive);
    if(receive=="未申领"){
        state_r=0;
    }else if(receive=="申领中"){
        state_r=1;
    }else{
        state_r=2;

    }
    console.log(state_r);
    $(".pager").remove();
    filterAjax({status:state_r,currentPageIndex:1},1);





});


