package com.duol.interceptor;

import com.duol.cache.ValueCache;
import com.duol.common.ResponseCode;
import com.duol.common.ServerResponse;
import com.duol.util.SessionUtil;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

/**
 * 验证登录状态,避免重复登录
 *
 * @author Duolaimon
 * 18-7-26 下午7:23
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String userId = (String) pathVariables.get("userId");
        logger.info("userId={}",userId);
        String[] info = SessionUtil.verifyUserLogin(session);
        ServerResponse serverResponse;
        if (Objects.nonNull(info) && ValueCache.verifySessionID(info[0], info[1])) {
            if (StringUtils.isNotBlank(userId)) {      //uri中用userId变量
                if ( StringUtils.equals(info[0], userId)) {
                    return true;
                }else {
                    logger.error("无操作权限");
                    serverResponse = ServerResponse.createByErrorMessage("该用户无操作权限");
                }
            } else {
                return true;
            }
        } else {
            logger.error("用户未登录");
            serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(new GsonBuilder().create().toJson(serverResponse));
        response.flushBuffer();

//        response.sendRedirect("/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
