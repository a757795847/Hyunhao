//日期控件
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

//checkbox 全选
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

var dataList = [];
//显示页面获取列表
function indexAjax(datas, pageState) {
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

            var tbody = '';

            var than = '';
            var thao = '';
            $.each(data.list, function (i, order) {
                console.log(order.giftState);
                if (order.giftState == 0) {
                    order.giftState = "未申领";


                }
                else if (order.giftState == 1) {
                    order.giftState = "审核中";
                    than = "通过";
                    thao = "驳回";

                }else{
                    order.giftState = "审核通过";
                }

                var curTime = new Date(parseInt(order.applyDate)).toLocaleString().replace(/:\d{1,2}$/,' ');
                console.log(order.applyDate);
                if(order.applyDate==''||order.applyDate==null){
                    curTime='';
                }

                for (var key in order) {
                    if (order[key] == null) {
                        order[key] = '';
                    }
                }
                tbody += '<tr id="' + order.id + '"><td><input type="checkbox" class="statistic"></td><td class="mailbox-date"><span>' + order.orderNumber + '</span></td>';
                tbody += '<td class="mailbox-date"><span class="label label-success proxylist_details" data-index="' + i + '">详情</span></td>';
                tbody += '<td class="mailbox-date"></td>';
                tbody += '<td class="mailbox-date"><span>' + curTime + '</span></td>';
                tbody += '<td id="order_state" class="mailbox-date"><span class="state ' + order.id + '">' + order.giftState + '</span></td>';
                tbody += '<td class="mailbox-date"><span class="top">' + order.giftDetail + '</span></td>';
                tbody += '<td class="mailbox-date"><span>' + order.sendDate + '</span></td>';
                tbody += '<td class="mailbox-date"><span>' + order.recieveDate + '</span></td>';
                tbody += '<td class="mailbox-date"><span class="label label-success order_confirm_btn" data-index="' + order.id + '" >' + than + '</span>';
                tbody += '<span class="label label-success turn" data-toggle="modal" data-target="#myModal">' + thao + '</span></td></tr>';

            
            });
            $("#Table").find("tbody").html(tbody);


            if (pageState == 1) {
                $("#jqueryPage").pagination({
                    count: data.page.count, //总数
                    size: data.page.pageSize, //每页数量
                    index: 1,//当前页
                    lrCount: 1,//当前页左右最多显示的数量
                    lCount: 1,//最开始预留的数量
                    rCount: 1,//最后预留的数量
                    callback: function (options) {
                        var index = options.index;
                        indexAjax({status: 1, currentPageIndex: index});
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
indexAjax({status: 0, currentPageIndex: 1}, 1);

//点击通过按钮
$("#Table").on('click', '.order_confirm_btn', function () {

    var orderIndex = $(this).data("index");
    var that = $(this);
    console.log(orderIndex);
    $.ajax({
        type: 'post',
        url: '/order/passAuditing/' + orderIndex,
        data: JSON.stringify({status: 1, currentPageIndex: 1}),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            if (data.status == 1) {
                $('.' + orderIndex).html("审核通过");
                // $(".label.label-success.turn").remove();
                var link = '<span class="label label-success turn">发送红包</span>';
                console.log(that);
                that.parent().html(link);


            }

        }
    })
    $('#send').attr('data-index', orderIndex);
});
//显示红包策略
var date_lists=[];
$("#Table").on("click",".label.label-success.turn",function(){
    console.log("1");
    var orders=$(this).data("index");
    $.ajax({
       type:'post',
        url:'/redstrategy/list',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            console.log(data);
            date_lists=data.list;
            var tbody='';
            for(var i=0;i<data.list.length;i++){
                tbody +='<span class="text-color border-color" data-index="' + data.list[i] .id + '">'+data.list[i].name+'</span>';
            }

            $("#span").find('.father').html(tbody);
            }

        })


    $('#send').attr('data-index', orders);
    $("#myModals").modal('show');

    })




$("#span").on("click",'.text-color.border-color', function () {
    $(this).addClass("actives");
    $(this).siblings().removeClass("actives");
});


//选择红包并发送红包
$("#send").on('click', function () {
    var order_index = $(this).data("index");
    console.log(order_index);
    var Idx = '';

    for (var i = 0; i < $("#span").find('.text-color').length; i++) {

        if ($("#span").find('.text-color').eq(i).hasClass('actives')) {
            console.log($('#span .text-color').eq(i).attr('class'))
            Idx = date_lists[i].id;
        }
    }



    console.log(Idx);
    var parameter = {
        "id": order_index,
        "strategyid": Idx
    }
    console.log(parameter);
    $.ajax({
        type: 'post',
        url: "/order/redSend",
        data: JSON.stringify(parameter),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            console.log(data);
            if(data.status=="1") {
                $('#' + order_index + ' #order_state span').html("红包发送成功");
                $('#' + order_index + ' .top').html(Idx / 100 + '元红包');
                $('#' + order_index + ' .turn').remove();
            }
        }
    })
});
//点击详情显示弹框

$("#Table").on('click', ".proxylist_details", function () {
    var order_index = $(this).data("index");
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




//下拉框
$("#basic-time-demo").select2();
$("#basic-status-demo").select2();
$("#basic-details-demo").select2();


$(".trigger").on('click', function () {
    var sub = $(this).html();
    if (sub == "导入时间") {
        $("div.fancy-select ul.options").css('left', '180px');

    } else if (sub == "未申领") {
        $("div.fancy-select ul.options").css('left', '360px');

    } else if (sub == "红包详情") {
        $("div.fancy-select ul.options").css('left', '540px');

    } else {
        $("div.fancy-select ul.options").css('left', '0');

    }


});

$("#clear").click(function () {
    $("#Text").val("");
});
//点击筛选 重新显示列表
function filterAjax(datas, pageState) {
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

            var tbody = '';
            var than = '';
            var thao = '';
            $.each(data.list, function (i, order) {
                if (order.giftState == 0) {
                    order.giftState = "未申领";
                } else if (order.giftState == 1) {
                    order.giftState = "审核中";
                    than = '<span class="label label-success order_confirm_btn" data-index="' + order.id + '" >通过</span>';
                    thao = '<span class="label label-success turn" data-toggle="modal" data-target="#myModal">驳回</span>';

                }else{
                    order.giftState = "审核通过";
                    thao='<span class="label label-success turn" data-index="' + order.id + '" >发送红包</span>';
                }

                var curTime = new Date(parseInt(order.applyDate)).toLocaleString().replace(/:\d{1,2}$/,' ');
                if(order.applyDate==''||order.applyDate==null){
                    curTime='';
                }
                console.log(curTime);

                for (var key in order) {
                    if (order[key] == null) {
                        order[key] = '';
                    }
                }
                var comment_file='';
                if(order.commentFile3!=''){
                    comment_file='<li><img src="http://open.izhuiyou.com/order/picture/'+order.commentFile1+'" alt="" class="tooltips"></li>';
                     comment_file +='<li><img src="http://open.izhuiyou.com/order/picture/'+order.commentFile2+'" alt="" class="tooltips"></li>';
                    comment_file +='<li><img src="http://open.izhuiyou.com/order/picture/'+order.commentFile3+'" alt="" class="tooltips"></li>';
                    console.log("1");
                }
                 else if(order.commentFile2!=''){
                    comment_file='<li><img src="http://open.izhuiyou.com/order/picture/'+order.commentFile1+'" alt="" class="tooltips"></li>';
                    comment_file +='<li><img src="http://open.izhuiyou.com/order/picture/'+order.commentFile2+'" alt="" class="tooltips"></li>';
                    console.log("2");
                }else if(order.commentFile1!=''){
                    comment_file='<li><img src="http://open.izhuiyou.com/order/picture/'+order.commentFile1+'" alt="" class="tooltips"></li>';
                }else{
                    comment_file='';
                }
                tbody += '<tr id="' + order.id + '"><td><input type="checkbox" id="statistic"></td><td class="mailbox-date"><span>' + order.orderNumber + '</span></td>';
                tbody += '<td class="mailbox-date"><span class="label label-success proxylist_details" data-index="' + i + '">详情</span></td>';
                tbody += '<td class="mailbox-date"><ul class="images">'+comment_file+'</ul></td>';
                tbody += '<td class="mailbox-date" style="width:12%;"><span>' + curTime + '</span></td>';
                tbody += '<td id="order_state" class="mailbox-date"><span class="state ' + order.id + '">' + order.giftState + '</span></td>';
                tbody += '<td class="mailbox-date"><span class="top">' + order.giftDetail + '</span></td>';
                tbody += '<td class="mailbox-date"><span>' + order.sendDate + '</span></td>';
                tbody += '<td class="mailbox-date"><span>' + order.recieveDate + '</span></td>';
                tbody += '<td class="mailbox-date">'+than+'';
                tbody += ''+thao+'</td></tr>';


            });
            $("#Table").find("tbody").html(tbody);

            if (pageState == 1) {
                $("#jqueryPage").pagination({
                    count: data.page.count, //总数
                    size: data.page.pageSize, //每页数量
                    index: 1,//当前页
                    lrCount: 1,//当前页左右最多显示的数量
                    lCount: 1,//最开始预留的数量
                    rCount: 1,//最后预留的数量
                    callback: function (options) {
                        var index = options.index;
                        filterAjax({status: datas.status, currentPageIndex: index});
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

$("#filter").on("click", function () {
    var state_r = '';
    var receive = $("#basic-status-demo").val();
    if (receive == "未申领") {
        state_r = 0;
    } else if (receive == "申领中") {
        state_r = 1;
    } else {
        state_r = 2;
    }
    var import_date=$("#Import_date").val();
    var user_date=$("#user_date").val();

    import_date=new Date(Date.parse(import_date.replace(/:\d{1,2}$/,' ')));
    import_date=import_date.getTime();

    user_date=new Date(Date.parse(user_date.replace(/:\d{1,2}$/,' ')));
    user_date=user_date.getTime();
    if(import_date ==""){
        filterAjax({status: state_r, currentPageIndex: 1,applyTime:user_date}, 1);
    }
    else if(user_date ==""){
        filterAjax({status: state_r, currentPageIndex: 1,importTime:import_date}, 1);
    }
    else{
        filterAjax({status: state_r, currentPageIndex: 1,importTime:import_date,applyTime:user_date}, 1);
    }

    $(".pager").remove();



});


// this.tooltipsPreview = function(){
//     console.log(1);
//     var xOffset = 20;
//     var yOffset = 20;
//     var winHeight = $(window).height();
//     var winWidth = $(window).width();
//     var obj;
//     var objHeight = 0;
//     var objWidth = 0;
//
//     $(window).resize(function(){
//         winHeight = $(window).height();
//         winWidth =  $(window).width();
//     });
//
//     var setHover = function(e){
//         if(objHeight == 0) objHeight = $(obj).find('iframe').height();
//         var top = (e.pageY + yOffset);
//         var left = (e.pageX + xOffset);
//         if(e.pageY > winHeight/2) top = (e.pageY - objHeight - yOffset);
//         if(e.pageX > winWidth/2) left = (e.pageX - objWidth - xOffset);
//         $(obj).css({'top':top,'left':left});
//         if( $(obj).css('visibility') == 'hidden' ){
//             $(obj).css({'visibility':'visible'}).fadeIn('fast');
//         }
//     };
//
//     $('.tooltips').hover(function(e){
//
//             var pacList =[];
//             console.log('111');
//             for(var i=0;i<$(this).parent().parent().find('.tooltips').length; i++){
//               /*  var $(this).parent().find('.tooltips').eq(i).attr("src");*/
//                 pacList.push($(this).parent().parent().find('.tooltips').eq(i).attr("src"));
//
//             }
//             console.log(pacList);
//          /*   var pic = $(this).attr('src') || $(this).attr('value') || $(this).attr('href');*/
//             if(!pacList) return;
//             var width = parseInt($(this).attr('_width'));
//             if(!width) width = 280;
//             objWidth = width + 2;
//             $('body').append('<div id="tooltips" style="position:absolute;top:0;left:0; border:1px solid #ddd;visibility:hidden;color:#fff;z-index:10001;">' +
//                 '<img style="position:absolute;left:0;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="'+ width +'" src="'+ pacList[0] +'" />' +
//                 '<img style="position:absolute;left:290px;top:0;z-index:10003;border:1px solid #ddd;" height="500px";width="'+ width +'" src="'+ pacList[1] +'" />' +
//                 '<img style="position:absolute;left:580px;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="'+ width +'" src="'+ pacList[2] +'" />' +
//                 '<iframe width="'+ objWidth +'" style="position:absolute;border:none;filter:alpha(opacity=0);-moz-opacity:0;-khtml-opacity: 0;opacity:0;z-index:10002;"></iframe></div>');
//             var top = (e.pageY + yOffset);
//             var left = (e.pageX + xOffset);
//             obj = $('#tooltips');
//             $(obj).find('img').load(function(){ //如果图片从缓存读取，有的浏览器（如chrome）这里不会执行，所以后面再加一个setHover
//                 objHeight = $(this).height()+2;
//                 $(obj).find('iframe').height( objHeight );
//                 setHover(e);
//             });
//             setHover(e);
//         },
//         function(){
//             $(obj).remove();
//         });
//
//     $('.tooltips').on('mousemove',function(e){
//         setHover(e);
//     });
// };
//
// $(window).load(function(){
//     tooltipsPreview();
// });
//鼠标悬停 图片放大
$('#Table').on('mouseover', '.images', function () {
    var pacList = [];
    for (var i = 0; i < $(this).find('.tooltips').length; i++) {
        /*  var $(this).parent().find('.tooltips').eq(i).attr("src");*/
        pacList.push($(this).find('.tooltips').eq(i).attr("src"));

    }
    var winHeight = $(window).height();
    var winWidth = $(window).width();
    var offset = $(this).offset();
    var objTop = offset.top;
    var objLeft = offset.left;

    if(($(document).scrollTop() + winHeight) - objTop < 500){
        objTop = objTop - 450;
    }else{
        objTop = objTop +19;
    }
    if(pacList.length==3){
        pacText='<img style="position:absolute;left:0;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="280" src="' + pacList[0] + '" />' +
            '<img style="position:absolute;left:280px;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="280" src="' + pacList[1] + '" />' +
            '<img style="position:absolute;left:560px;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="280" src="' + pacList[2] + '" />';
    }else if(pacList.length==2){
        pacText='<img style="position:absolute;left:0;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="280" src="' + pacList[0] + '" />' +
        '<img style="position:absolute;left:280px;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="280" src="' + pacList[1] + '" />';
    }else{
        pacText='<img style="position:absolute;left:0;top:0;z-index:10003;border:1px solid #ddd;" height="500px" width="280" src="' + pacList[0] + '"/>';
    }

    $('#tooltips').html(pacText);
    $('#tooltips').css({
        'top':objTop,
        'left':objLeft +114,
    })
    $('#tooltips').show();
});
$('#Table').on('mouseout','.images',function(){
    $('#tooltips').hide();
})