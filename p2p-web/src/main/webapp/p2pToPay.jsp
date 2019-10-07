<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/9/7
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>p2p调用pay工程的支付请求页面接口</title>
</head>
<body>
<form method="post" action="http://localhost:9090/pay/api/alipay">
    <input name="out_trade_no" value="${rechargeNo}"/>
    <input name="total_amount" value="${rechargeMoney}"/>
    <input name="subject" value="${subject}"/>
</form>
<script>document.forms[0].submit();</script>
</body>
</html>
