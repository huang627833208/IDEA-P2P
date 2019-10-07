package com.bjpowernode.p2p.common.util;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.common.constant.StatusCodeConstants;

import java.util.HashMap;

/**
 * ClassName:Result
 * Package:com.bjpowernode.p2p.common.util
 * Description:TODO
 *
 * @date:2019/8/30 15:56
 * @author:guoxin
 */
public class Result extends HashMap<Object,Object> {


    /**
     * 成功(没有数据)
     * @return
     */
    public static Result success() {
        Result result = new Result();
        result.put(Constants.CODE, StatusCodeConstants.SUCCESS_CODE);
        result.put(Constants.MESSAGE,StatusCodeConstants.OK);
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.put(Constants.CODE, StatusCodeConstants.SUCCESS_CODE);
        result.put(Constants.MESSAGE,StatusCodeConstants.OK);
        result.put(Constants.DATAS,object);
        return result;

    }

    /**
     * 错误(指定错误信息)
     * @param message
     * @return
     */
    public static Result error(String message) {
        Result result = new Result();
        result.put(Constants.CODE,StatusCodeConstants.ERROR_CODE);
        result.put(Constants.MESSAGE,message);
        return result;
    }



}
