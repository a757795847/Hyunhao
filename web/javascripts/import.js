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
        console.log(res);
      var tbody='';
        $.each(res.list,function(i,order){
            tbody +='<tr><td><input type="checkbox" id="statistic"></td><td class="mailbox-date"><span>'+order.order.orderNumber+'</span></td>';
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

