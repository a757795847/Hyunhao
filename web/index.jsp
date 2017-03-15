<!DOCTYPE html>
<html>
  <head>
    <title>$Title$</title>
    <script src="/documents/jquery.min.js"></script>
    <script>
       $(function(){
           $.ajax({
               type: 'POST',
               url: "/pay/asyn",
               dataType: 'text',
               async:true,
               success: function (data) {
                   console.log(data)
               },
               error: function () {

               }

           })
           console.log("之后")
       })



    </script>
  </head>
  <body>

  </body>
</html>
