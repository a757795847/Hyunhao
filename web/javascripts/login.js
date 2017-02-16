$("#btn").on("click", function () {

    var username = $("#username").val();
    var password = $("#password").val();

    var datas = {
        "username": username,
        "password": password
    }

    $.ajax({
        type: 'POST',
        url: '/operator/login',
        data: JSON.stringify(datas),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
        if(data.status=='0'){
            alert(data.message);
        }else {
            location.href = data.url;
        }
            console.log(data);
            console.log(data.url);

        },
        error: function (jqXHR) {
            if (jqXHR.status == 406) {

            }
        }

    })


});


