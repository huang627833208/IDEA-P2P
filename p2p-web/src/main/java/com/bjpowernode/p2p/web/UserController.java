package com.bjpowernode.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.common.util.Result;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.*;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.service.user.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:UserController
 * Package:com.bjpowernode.p2p.web
 * Description:TODO
 *
 * @date:2019/8/30 15:50
 * @author:guoxin
 */
@Controller
//@RestController     // @RestController 等同于 类上加@Controller + 方法上@ResponseBody
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private IncomeRecordService incomeRecordService;

    @Autowired
    private RedisService redisService;


    /**
     * 接口名称：验证手机号是否重复
     * 接口地址：http://localhost:8080/p2p/loan/checkPhone
     * 请求方式：http GET POST
     * @param phone 必填 String
     * @return  code message  响应参数格式是:JSON格式，{"code":"10000","message":"消息"}
     */
    @RequestMapping(value = "/loan/checkPhone")
    @ResponseBody
    public Result checkPhone(@RequestParam (value = "phone",required = true) String phone) {

        //查询手机号码是否重复(手机号) -> 返回：int|boolean
        //根据手机号查询用户信息(手机号) -> 返回：User
        User user = userService.queryUserByPhone(phone);

        if (ObjectUtils.allNotNull(user)) {
            return Result.error("该手机号已被注册");
        }

        return Result.success();
    }


    @RequestMapping(value = "/loan/checkCaptcha")
    @ResponseBody
    public Result checkCaptcha(HttpServletRequest request,
                               @RequestParam (value = "captcha",required = true) String captcha) {
        //从session中获取图形验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.CAPTCHA);

        //比较用户输入图形验证码和session中的图形验证码
        if (!StringUtils.equalsIgnoreCase(sessionCaptcha, captcha)) {
            return Result.error("请输入正确的图形验证码");
        }

        return Result.success();
    }


//    @RequestMapping(value = "/loan/register",method =RequestMethod.POST) //等同于 @PostMapping(value="/loan/register")
    @PostMapping(value = "/loan/register")
    @ResponseBody
    public Result register(HttpServletRequest request,
                           @RequestParam (value = "phone",required = true) String phone,
                           @RequestParam (value = "loginPassword",required = true) String loginPassword) {

        try {
            //用户注册【1.新增用户 2.开立一个帐户】(手机号,登录密码)
            User user = userService.register(phone,loginPassword);

            //将用户的信息存放到session中
            BaseController.setSession(request,Constants.SESSION_USER,user);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("用户注册失败");
        }


        return Result.success();
    }

    @RequestMapping(value = "/loan/myFinanceAccount", method = RequestMethod.GET)
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request) {
        //从session中获取用户的信息
        User sessionUser = (User) BaseController.getSessionValue(request, Constants.SESSION_USER);

        //根据用户标识获取帐户余额
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());

        return financeAccount;
    }


    @PostMapping(value = "/loan/verifyRealName")
    public @ResponseBody Result verifyRealName(HttpServletRequest request,
                                               @RequestParam (value = "realName",required = true) String realName,
                                               @RequestParam (value = "idCard",required = true) String idCard) {

        try {
            //准备实名认证的参数
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("appkey","467e4b8d2a8f2f787e34f");
            paramMap.put("cardNo",idCard);
            paramMap.put("realName",realName);

            //实名认证 -> 调用互联网接口(京东万象平台)实名认证接口 -> 该接口响应的是json格式的字符串
//            String jsonString = HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test", paramMap);
            String jsonString = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \"乐天磊\",\n" +
                    "            \"idcard\": \"350721197702134399\",\n" +
                    "            \"isok\": true\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            //解析json格式的字符串，使用fastjson来解析
            //1.将json格式的字符串转换为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);

            //获取通信标识code
            String code = jsonObject.getString("code");

            //判断是否通信成功
            if (!StringUtils.equals("10000", code)) {
                return Result.error(jsonObject.getString("msg"));
            }

            //获取实名认证是否一致的结果isok
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");

            //判断是否一致
            if (!isok) {
                return Result.error("真实姓名和身份证号码不一致");
            }

            //从session中获取用户信息
            User sessionUser = (User) BaseController.getSessionValue(request, Constants.SESSION_USER);

            //更新用户的信息
            User updateUser = new User();
            updateUser.setId(sessionUser.getId());
            updateUser.setName(realName);
            updateUser.setIdCard(idCard);
            int modifyUserCount = userService.modifyUserById(updateUser);

            if (modifyUserCount <= 0) {
                return Result.error("用户实名认证异常");
            }

            //更新session中用户的信息
            sessionUser.setName(realName);
            sessionUser.setIdCard(idCard);
            BaseController.setSession(request,Constants.SESSION_USER,sessionUser);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("用户实名认证异常");
        }


        return Result.success();
    }


    @RequestMapping(value = "/loan/logout")
    public String logout(HttpServletRequest request) {

        //清除session 或者 让session失效
        BaseController.removeSession(request,Constants.SESSION_USER);

        return "redirect:/index";
    }

    @RequestMapping(value = "/loan/loadStat")
    public @ResponseBody Result loadStat() {

        //获取平台历史平均年化收益率
        Double historyAverageYearRate = loanInfoService.queryHistoryAverageYearRate();

        //获取平台注册总人数
        Long allUserCount = userService.queryAllUserCount();

        //获取累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();

        Result result = new Result();
        result.put(Constants.HISTORY_AVERAGE_YEAR_RATE,historyAverageYearRate);
        result.put(Constants.ALL_USER_COUNT,allUserCount);
        result.put(Constants.ALL_BID_MONEY,allBidMoney);

        return result;
    }

    @PostMapping(value = "/loan/login")
    public @ResponseBody Result login(HttpServletRequest request,
                                      @RequestParam (value = "phone",required = true) String phone,
                                      @RequestParam (value = "loginPassword",required = true) String loginPassword,
                                      @RequestParam (value = "messageCode",required = true) String messageCode) {
        try {

            //从redis中获取验证码
            String redisMessageCode = redisService.get(phone);

            //将用户输入的短信验证码与redis中的短信验证码做校验
            if (!StringUtils.equals(messageCode, redisMessageCode)) {
                return Result.error("请输入正确的验证码");
            }


            //用户登录【1.根据手机号和密码查询用户信息 2.更新最近登录的时间】
            User user = userService.login(loginPassword,phone);

            //将用户的信息存放到session中
            BaseController.setSession(request,Constants.SESSION_USER,user);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("用户名或密码有误");
        }
        return Result.success();
    }


    @RequestMapping(value = "/loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model) {

        //从session中获取用户的信息
        User sessionUser = (User) BaseController.getSessionValue(request,Constants.SESSION_USER);

        //根据用户标识获取帐户信息
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());

        //准备以下查询的参数
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uid",sessionUser.getId());
        paramMap.put("currentPage",0);
        paramMap.put("pageSize",5);

        //根据用户标识获取最近投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryRecentlyBidInfoListByUid(paramMap);

        //根据用户标识获取最近充值记录
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryRecentlyRechargeRecordListByUid(paramMap);

        //根据用户标识获取最近收益记录
        List<IncomeRecord> incomeRecordList = incomeRecordService.queryRecentlyIncomeRecordListByUid(paramMap);

        model.addAttribute("financeAccount",financeAccount);
        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("incomeRecordList",incomeRecordList);

        return "myCenter";
    }

    @RequestMapping(value = "/loan/messageCode")
    public @ResponseBody Result messageCode(HttpServletRequest request,
                                            @RequestParam (value = "phone",required = true) String phone) {
        String messageCode = "";
        try {

            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("appkey","45dfad135c66716de18aec7c");
            paramMap.put("mobile",phone);

            //生成一个随机数字
            messageCode = getRandomCode(4);
            String content = "【凯信通】您的验证码是：" + messageCode;
            paramMap.put("content",content);

            //调用京东万象平台的106短信接口发送短信内容
//            String jsonString = HttpClientUtils.doPost("https://way.jd.com/kaixintong/kaixintong", paramMap);

            String jsonString = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 0,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-1146645</remainpoint>\\n <taskID>103938075</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                    "}";

            //解析json格式的字符串
            JSONObject jsonObject = JSONObject.parseObject(jsonString);

            //获取通信标识code
            String code = jsonObject.getString("code");

            if (!StringUtils.equals(code, "10000")) {
                return Result.error("短信平台通信异常");
            }

            //获取result，该值为xml格式的字符串
            String resultXml = jsonObject.getString("result");

            //将xml格式的字符串转换为document对象
            //使用Dom4j+Xpath来解析,需要在项目中dom4j的依赖
            //将xml格式的字符串转换为Document对象
            Document document = DocumentHelper.parseText(resultXml);

            //获取returnstatus节点对象
            //编写该节点的xpath路径表达式：/returnsms/returnstatus  或者  //returnstatus
            Node returnStatusNode = document.selectSingleNode("//returnstatus");

            //获取当前结果文本内容
            String returnStatusNodeText = returnStatusNode.getText();

            //判断是否发送成功
            if (!StringUtils.equals("Success", returnStatusNodeText)) {
                return Result.error("短信发送失败，请重试");
            }

            //将短信验证码存放到redis中
            redisService.put(phone,messageCode);



        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("短信平台异常");
        }


        return Result.success(messageCode);
    }

    private String getRandomCode(int count) {

        //字符串变量
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int index = (int) Math.round(Math.random()*9);
            stringBuilder.append(index);
        }

        return stringBuilder.toString();
    }
}
