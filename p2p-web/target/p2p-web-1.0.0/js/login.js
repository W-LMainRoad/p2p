//登录后返回页面
var referrer = "";

//获取到跳转至当前页面之前页面的URL
referrer = document.referrer;
//判断是否为空
//为空返回的是true,不为空为false
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，
            // Safari和Chrome下window.opener.location没有任何属性
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});

//验证图形验证码
function checkCaptcha() {
	var captcha = $.trim($("#captcha").val());
	var flag = true;
	if ("" == captcha){
		$("#showId").html("请输入图形验证码")
		return false;
	} else {
		$.ajax({
			url:"/p2p/loan/checkCaptcha",
			type:"post",
			data:"captcha="+ captcha,
			async:false,
			success:function (jsonObject) {
				if (jsonObject.errorMessage == "OK"){
					$("#showId").html("");
					flag = true;
				} else {
                    $("#showId").html(jsonObject.errorMessage);
					flag = false;
				}
			},
			error:function () {
                $("#showId").html("系统繁忙，请稍后重试...");
				flag = false;
			}
		});
		return flag;

	}

}

function checkPhone() {
    var phone = $.trim($("#phone").val());
    if("" == phone){
        $("#showId").html("请输入手机号码");
        return false;
    } else if (!/^1[1-9]\d{9}$/.test(phone)) {
        $("#showId").html("请输入正确的手机号码");
        return false;
    } else {
        $("#showId").html("");
        return true;
    }
}

function checkPassword() {
    var password = $.trim($("#loginPassword").val());
    if ("" == password) {
        $("#showId").html("请输入密码");
        return false;
    } else {
        $("#showId").html("");
        return true;
    }
}

function login() {
    var phone = $.trim($("#phone").val());
    var loginPassword = $.trim($("#loginPassword").val());

	if (checkPhone() && checkPassword() && checkCaptcha() ) {
		$("#loginPassword").val($.md5(loginPassword));
		$.ajax({
			url:"/p2p/loan/login",
			type:"post",
			data:{
				"phone":phone,
				"loginPassword":$.md5(loginPassword)
			},
			success:function (jsonObjet) {
				if (jsonObjet.errorMessage == "OK") {
				    //验证成功之后从哪来回哪去
					window.location.href = referrer;
				} else {
					$("#showId").html(jsonObjet.errorMessage);
				}
			},
			error:function () {
				$("#showId").html("系统繁忙，请稍后重试...");
			}
		});
	}



}
