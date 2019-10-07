package com.bjpowernode.p2p.web;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName:BaseController
 * Package:com.bjpowernode.p2p.web
 * Description:TODO
 *
 * @date:2019/8/31 10:59
 * @author:guoxin
 */
public abstract class BaseController {

    public static void setSession(HttpServletRequest request,String key,Object value) {
        request.getSession().setAttribute(key,value);
    }

    public static Object getSessionValue(HttpServletRequest request,String key) {
        return request.getSession().getAttribute(key);
    }

    public static void removeSession(HttpServletRequest request, String key) {
        request.getSession().removeAttribute(key);
//        request.getSession().invalidate();
    }
}
