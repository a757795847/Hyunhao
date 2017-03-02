$("#basic-details-demo").select2();
$("#basic-status-demo").select2();
$("#basic-application-demo").select2();


function indexAjax(datas, pageState) {
    console.log(datas);
    $.ajax({
        type: 'post',
        url: '/proxy/list',
        data: JSON.stringify(datas),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            console.log(data);
            var tbody='';
            $.each(data.list, function (i, order) {
                console.log(order.beginTime);
                order.beginTime=order.beginTime-86400000*30;
                console.log(order.beginTime);
                var authorization='';
                var curTime = new Date(parseInt(order.beginTime)).toLocaleString().replace(/:\d{1,2}$/,' ');
                var endTime = new Date(parseInt(order.endTime)).toLocaleString().replace(/:\d{1,2}$/,' ');
                endTime = endTime.replace(/:\d{1,2}$/,' ');
                var end_time= new Date(endTime).getTime();
                console.log(end_time);
                if(order.beginTime==''){
                    curTime='';
                }
                if(order.endTime==''){
                    curTime='';
                }
                if(order.isAuthentication=='0'){
                    authorization='未授权';
                }else{
                    authorization='已授权';
                }
                if(order.phone==null){
                    order.phone='';
                }
                tbody += '<tr><td class="mailbox-date"><span>' + order.serverId + '</span></td>';
                tbody += '<td class="mailbox-date"><span class="phone">'+order.phone+'</span></td>';
                tbody += '<td class="mailbox-date"><span>'+order.publicName+'</span></td>';
                tbody += '<td class="mailbox-date"><span>' + authorization + '</span></td>';
                tbody += '<td class="mailbox-date"><span>' + order.zyappName + '</span></td>';
                tbody += '<td class="mailbox-date"><span>' + order.serverType + '</span></td>';
                tbody += '<td class="mailbox-date"><span>' + curTime + '</span></td>';
                tbody += '<td class="mailbox-date"><span class="End">' + endTime + '</span></td>';
                tbody += '<td class="mailbox-date">' +
                    '<span class="label label-success minus" style="margin-right:5px; background-color:rgb(191,191,191);"data-index="' + order.serverId + '">减时</span>';
                tbody += '<span class="label label-success"style="background-color:rgb(41,204,254);">延期</span></td></tr>';


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
                        indexAjax({currentPageIndex: index});

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

var zyappid=$("#basic-application-demo").val();
var isAuthentication=$("#basic-details-demo").val();
var serverType=$("#basic-status-demo").val();

   
      




indexAjax({currentPageIndex: 1},1);

$("#Table").on("click",'.minus',function(){
   var phones=$(this).parent().parent().find(".phone").html();
   var end_time=$(this).parent().parent().find(".End").html();
    $("#moblie").html(phones);
    $("#this_time").html(end_time);


     $('#myModal').modal('show');
});
$("#ul_change").on("click","li",function(){
    var change_html=$(this).html();
  $(this).addClass('lactive');
    $(this).siblings().removeClass("lactive");
    $("#nub").html(change_html);


})
$("#cancel").on("click",function(){
    $("#nub").html("0");
    $('li').removeClass("lactive");

});
var date=new Date();

date=parseInt(date).toLocaleString().replace(/:\d{1,2}$/,' ');
/*date.setDate(date.getDate()-30);*/
console.log(date);

/* date=date.getDate()+1;
console.log(date);*/




