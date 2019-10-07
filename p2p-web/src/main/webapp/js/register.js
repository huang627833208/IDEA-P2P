


//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});


	$("#phone").on("blur",function () {
		
		var phone = $.trim($("#phone").val());

		if (!phone) {
			showError("phone","请输入手机号码");
		} else if (!/^1[1-9]\d{9}$/.test(phone)) {
			showError("phone", "请输入正确的手机号码");
		} else {
			$.ajax({
				url:"loan/checkPhone",
				type:"get",//往往get是向服务器获取数据，post往往是向服务传递数据
				data:"phone="+phone,
				success:function (data) {
					if (data.code == "10000") {
						showSuccess("phone");
					} else {
						showError("phone",data.message);
					}
				},
				error:function () {
					showError("phone","系统异常");
				}
			});
		}
		
	});


	$("#loginPassword").on("blur",function () {
		var loginPassword = $.trim($("#loginPassword").val());

		if (!loginPassword) {
			showError("loginPassword","请设置密码");
		} else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)) {
			showError("loginPassword","密码只可使用数字和大小英文字母");
		} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)) {
			showError("loginPassword","密码必须包含英文和数字");
		} else if (loginPassword.length < 6 || loginPassword.length > 20) {
			showError("loginPassword", "密码长度应为6-20位");
		} else {
			showSuccess("loginPassword");
		}
	});


	$("#captcha").on("blur",function () {
		var captcha = $.trim($("#captcha").val());

		if (!captcha) {
			showError("captcha", "请输入图形验证码");
		} else {
			$.ajax({
				url:"loan/checkCaptcha",
				type:"post",
				data:{
					"captcha":captcha
				},
				success:function (data) {
					if (data.code == "10000") {
						showSuccess("captcha");
					} else {
						showError("captcha",data.message);
					}
				},
				error:function () {
					showError("captcha","系统开小差儿");
				}
			});
		}
	});


	$("#btnRegist").on("click",function () {

		var flag = true;
		var phone = $.trim($("#phone").val());
		var loginPassword = $.trim($("#loginPassword").val());

		//首先得去触发这些事件
		$("#phone").blur();
		$("#loginPassword").blur();
		$("#captcha").blur();

		//获取触发后的结果，当前这些没有验证通过的元素
		//获取div中id属性值以Err结尾的对象 -> 返回数组
		// alert($("div[id$='Err']").size());

		$("div[id$='Err']").each(function () {
			//获取到每一个div，判断当前div是否显示
			if (!$(this).is(":hidden")) {
				flag = false;
				return;
			}
		});

		if (flag) {
			$("#loginPassword").val($.md5(loginPassword));

			//验证通过：提交注册请求
			$.ajax({
				url:"loan/register",
				type:"post",
				data:{
					"phone":phone,
					"loginPassword":$.md5(loginPassword)
				},
				success:function (data) {
					if (data.code == "10000") {
						window.location.href = "realName.jsp";
					} else {
						$("#loginPassword").val("");
						showError("captcha",data.message);
					}
				},
				error:function () {
					$("#loginPassword").val("");
					showError("captcha","注册异常");
				}
			});
		}


	});


});

//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}