(function($){
    var myChartCommercial = echarts.init(document.getElementById('commercial'));
optionCommercial = {
    title: {
        text: ''
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['在线公众号']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true  
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : ['2017/1/4','2017/1/5','2017/1/6','2017/1/7','2017/1/8','2017/1/9','2017/1/10','2017/1/11','2017/1/12','2017/1/13','2017/1/14','2017/1/15']
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'在线公众号',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290]
        }
    ]
};
myChartCommercial.setOption(optionCommercial);
})(jQuery)