function getData(){
    $.ajax({
        type: "POST",
        data: {"userId": userId},
        url:"/testQuestion/getTestResult",
        success:function(data) {
            //填充数据
            var _data = data[data.length-1];
            document.getElementById("testResult").innerHTML =  _data.testResult;
            var radarChartData = {
                labels : ["职业素质","创业素质","公司商法知识","财务营销知识","运营管理知识","创业基本能力","运营管理能力","市场营销能力"],
                datasets : [

                    {
                        fillColor : "rgba(151,187,205,0.5)",
                        strokeColor : "rgba(151,187,205,1)",
                        pointColor : "rgba(151,187,205,1)",
                        pointStrokeColor : "#fff",
                        // data : [51,50,79,44,50,80,33,50]
                        data : [_data.zysz,_data.cysz,_data.gssfzs,_data.cwyxzs,_data.yyglzs,_data.cyjbnl,_data.yyglnl,_data.scyxnl]
                    }
                ]

            }

            new Chart(document.getElementById("canvas").getContext("2d")).Line(radarChartData);

        }
        ,error: function (data) {
            // console.log(data);
        }
    });
}



$(function() {
    $("#printId").find('.print-link').on('click', function() {
        var canvas=document.getElementById("canvas");
        var image = document.getElementById("iii");
        image.src = canvas.toDataURL("image/png");
        canvas.style.display='none';
        $.print("#printId");
    });
});