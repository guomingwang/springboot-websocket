$(".toggle").on("click", function () {
  $(".container").stop().addClass("active");
});

$(".close").on("click", function () {
  $(".container").stop().removeClass("active");
});

$("#btn").click(function () {

  $.post("/login",{userName:$("#userName").val(),passwd:$("#password").val()},function(res){
    if (res.code==200){
      console.log(res);
      location.href = "getMain";
    } else {
      console.log(res);
      $("#err_msg").html(res.msg);
    }
  },"json");
});