package com.bjpowernode.p2p.interceptor;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.web.BaseController;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:UserInterceptor
 * Package:com.bjpowernode.p2p.interceptor
 * Description:TODO
 *
 * @date:2019/9/9 12:15
 * @author:guoxin
 */
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从session中获取用户的信息
        User sessionUser = (User) BaseController.getSessionValue(request, Constants.SESSION_USER);

        if (!ObjectUtils.allNotNull(sessionUser)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
