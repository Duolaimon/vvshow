package com.duol.util;

import com.duol.common.Const;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Duolaimon
 * 18-7-23 下午6:55
 */
public class SessionUtil {


    public static String[] verifyUserLogin(HttpSession session) {
        return verify(session, Const.CURRENT_USER);
    }

    public static String[] verifyManageLogin(HttpSession session) {
        return verify(session, Const.CURRENT_ADMIN);
    }

    private static String[] verify(HttpSession session, String currentAdmin) {
        String sessionId = (String) session.getAttribute(currentAdmin);
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }
        return sessionId.split("-");
    }

    public static boolean verifySessionId(HttpSession session, List<String> sessionInfo) {
        String sessionId = (String) session.getAttribute(Const.CURRENT_USER);
        if (StringUtils.isBlank(sessionId)) {
            return false;
        }
        String[] strings = sessionId.split("-");
        sessionInfo.add(strings[0]);
        sessionInfo.add(strings[1]);
        return true;
    }
}
