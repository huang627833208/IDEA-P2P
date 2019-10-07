
//登录后返回页面的URL
var referrer = "";

// alert(!referrer); //true
// alert(referrer); // ""
referrer = document.referrer;//跳转至当前页面之前的URL
// alert(referrer); // URL
// alert(!referrer);//false

if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
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


/*
$(document).ready(function () {
	alert("=-==============");
});*/

$(function () {
	loadStat();

	var phoneFlag = true;
    var loginPasswordFlag = true;
    var messageCodeFlag = true;

	$("#phone").on("blur",function () {
	    var phone = $.trim($("#phone").val());

        if (!phone) {
            $("#showId").html("请输入手机号码");
            phoneFlag = false;
        } else if (!/^1[1-9]\d{9}$/.test(phone)) {
            $("#showId").html("请输入正确的手机号码");
            phoneFlag = false;
        } else {
            $("#showId").html("");
            phoneFlag = true;
        }
    });


	$("#loginPassword").on("blur",function () {
	    var loginPassword = $.trim($("#loginPassword").val());

        if (!loginPassword) {
            $("#showId").html("请输入登录密码");
            loginPasswordFlag = false;
            return;
        } else {
            $("#showId").html("");
            loginPasswordFlag = true;
        }

    });


	$("#messageCode").on("blur",function () {
        var messageCode = $.trim($("#messageCode").val());

        if (!messageCode) {
            $("#showId").html("请输入短信验证码");
            messageCodeFlag = false;
        } else {
            $("#showId").html("");
            messageCodeFlag = true;
        }
    });

    $("#loginBtn").on("click",function () {

        var phone = $.trim($("#phone").val());
        var loginPassword = $.trim($("#loginPassword").val());
        var messageCode = $.trim($("#messageCode").val());

        $("#phone").blur();

        if (phoneFlag) {

            $("#loginPassword").blur();
            if (loginPasswordFlag) {

                $("#messageCode").blur();
                if (messageCodeFlag) {

                    $("#loginPassword").val($.md5(loginPassword));

                    //发送登录的请求
                    $.ajax({
                        url:"loan/login",
                        type:"post",
                        data:{
                            "phone":phone,
                            "loginPassword":$.md5(loginPassword),
                            "messageCode":messageCode
                        },
                        success:function (data) {
                            if (data.code == "10000") {
                                window.location.href = referrer;
                            } else {
                                $("#loginPassword").val("");
                                $("#showId").html(data.message);
                            }
                        },
                        error:function () {
                            $("#loginPassword").val("");
                            $("#showId").html("系统异常");
                        }
                    });

                }
            }
        }
    });


    $("#dateBtn1").on("click",function () {
        var phone = $.trim($("#phone").val());

        //判断当前是否存在倒计时
        if (!$("#dateBtn1").hasClass("on")) {
            //首先触发验证手机号
           $("#phone").blur();

            if (phoneFlag) {

                //成功:
                $("#loginPassword").blur();

                // 触发密码
                if (loginPasswordFlag) {

                    $.ajax({
                        url:"loan/messageCode",
                        type:"get",
                        data:"phone="+phone,
                        success:function (data) {
                            alert("您的验证码是："+data.datas);
                            if (data.code == "10000") {
                                $.leftTime(60,function (d) {
                                    if (d.status) {
                                        $("#dateBtn1").addClass("on");
                                        $("#dateBtn1").html((d.s == "00"?"60":d.s) + "s重发");
                                    } else {
                                        $("#dateBtn1").removeClass("on");
                                        $("#dateBtn1").html("获取验证码");
                                    }
                                });
                            } else {
                                $("#showId").html(data.message);
                            }
                        },
                        error:function () {
                            $("#showId").html("短信发送异常");

                        }
                    });


                }
                //失败
            }

        }

        //失败
    });


});

function loadStat() {
	$.ajax({
		url:"loan/loadStat",
		type:"get",
		success:function (data) {
			$(".historyAverageYearRate").html(data.historyAverageYearRate);
			$("#allUserCount").html(data.allUserCount);
			$("#allBidMoney").html(data.allBidMoney);
		}
	});

}





















