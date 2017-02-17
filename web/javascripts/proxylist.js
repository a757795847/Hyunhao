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
                tbody +='<tr><td><input type="checkbox" id="statistic"></td><td class="mailbox-date"><span>'+order.orderNumber+'</span></td>';
                tbody +='<td class="mailbox-date"><span class="label label-success"><a href="proxydetails.html">详情</span></a></td>';
                tbody +='<td class="mailbox-date"></td>';
                tbody +='<td class="mailbox-date"><span>'+order.applyDate+'</span></td>';
                tbody +='<td class="mailbox-date"><span>'+order.giftState+'</span></td>';
                tbody +='<td class="mailbox-date"><span>'+order.giftDetail+'</span></td>';
                tbody +='<td class="mailbox-date"><span>'+order.sendDate+'</span></td>';
                tbody +='<td class="mailbox-date"><span>'+order.recieveDate+'</span></td>';
                tbody +='<td class="mailbox-date"><span class="label label-success" id="bys"><a href="#modalt">'+than+'</a></span>';
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
$("#bys").on("click",function(){
    
    
    
    
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
    console.log("1");
    $("#Text").val("");
});


var reds=true;
$(".text-color.border-color").click(function(){
    if(reds) {
        $(this).addClass("redbag");
        reds=false;
    }else{
        $(this).removeClass("redbag");  

    }

});
    //$(this).css("background-color","#2ca2fb");



// Boilerplate
