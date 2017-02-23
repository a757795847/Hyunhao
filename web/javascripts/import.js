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
var datas='';
$("#submit").on("click",function(e){
    e.preventDefault();

    $.ajax({
        url: '/order/parseCsv',
        type: 'POST',
        cache: false,
        dataType: 'json',
        data: new FormData($('#uploadForm')[0]),
        processData: false,
        contentType: false
    }).done(function(res) {
            datas=res.list;
        console.log(res);
      var tbody='';
        $.each(res.list,function(i,order){
            order_State=order.state;
            var icon='';
            if(order.state<1){

                icon='<img src="../images/21133.jpg" class="Icon">';
            }
            tbody +='<tr><td><input type="checkbox" class="statistic" data-index="'+ i +'">'+icon+'</td><td class="mailbox-date"><span>'+order.order.orderNumber+'</span></td>';
            tbody +='<td class="mailbox-date"><span>'+order.order.buyerName+'</span></td>';
            tbody +='<td class="mailbox-date ltime""><span>'+order.order.orderCreateTime+'</span></td>';
            tbody +='<td class="mailbox-date ltime"><span>'+order.order.orderPayTime+'</span></td>';
            tbody +='<td class="mailbox-date"><span>'+order.order.buyerZhifubao+'</span></td>';
            tbody +='<td class="mailbox-date ltime"><span>'+order.order.amount+'</span></td>';
            tbody +='<td class="mailbox-date ltime"><span>'+order.order.receiver+'</span></td>';
            tbody +='<td class="mailbox-date"><span id="long">'+order.order.receiverAddress+'</span></td>';
            tbody +='<td class="mailbox-date"><span>'+order.order.receiverMobile+'</span>';


        });
        $("#Table").find("tbody").html(tbody);


    }).fail(function(res) {});
        




});
$("#click").on("click",function(){
    console.log(datas);
    console.log(datas[0].order);
    var checbox_index='';
    var dataList=[];
    for(var i=0;i<$('#dis').find('input[type=checkbox]').length;i++){
        var sub=$('#dis').find('input[type=checkbox]').eq(i).is(":checked");
        if(sub){
            var checbox_index=$('#dis').find('input[type=checkbox]').eq(i).attr('data-index');
            if(datas[checbox_index].state=1){
                dataList.push(datas[checbox_index].order);
                console.log("hu");
            }

        }
    }


    $.ajax({
        type: 'post',
        url: '/order/importCsv',
        data: JSON.stringify(dataList),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            // console.log(data.list[0].giftState);
            console.log(data);


        },
        error: function (jqXHR) {
            if (jqXHR.status == 406) {

            }
        }

    })


    console.log(dataList);






});

