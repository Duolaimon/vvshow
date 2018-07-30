package com.duol.interceptor;

import com.duol.cache.ValueCache;
import com.duol.common.ServerResponse;
import com.duol.util.SessionUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author Duolaimon
 * 18-7-27 上午9:00
 */
public class ManageInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(ManageInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String[] info = SessionUtil.verifyManageLogin(session);
        if (Objects.nonNull(info) && ValueCache.verifyManageSessionId(info[0], info[1])) {
            return true;
        }
        logger.error("管理员未登录");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(new Gson().toJson(ServerResponse.createByErrorMessage("管理员未登录")));
        response.flushBuffer();
//        response.sendRedirect("/index");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
