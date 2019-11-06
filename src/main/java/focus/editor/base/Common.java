package focus.editor.base;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunc
 * @date 2019/11/6 10:39
 * @description Common
 */

public class Common {
    private static final Logger logger = Logger.getLogger(Common.class);

    private static final String UNKNOWN = "unKnown";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = getIp(request);
        logger.info("Current ip address:" + ipAddress);
        return ipAddress;
    }

    private static String getIp(HttpServletRequest request) {
        String xRealIp = request.getHeader(X_REAL_IP);
        String xForwardedFor = request.getHeader(X_FORWARDED_FOR);

        if (StringUtils.isNotEmpty(xForwardedFor) && !UNKNOWN.equalsIgnoreCase(xForwardedFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xForwardedFor.indexOf(",");
            if (index != -1) {
                return xForwardedFor.substring(0, index);
            }
            return xForwardedFor;
        }

        String ipAddress = xRealIp;
        if (isInvalid(ipAddress)) {
            ipAddress = request.getHeader(PROXY_CLIENT_IP);
        }
        if (isInvalid(ipAddress)) {
            ipAddress = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (isInvalid(ipAddress)) {
            ipAddress = request.getHeader(HTTP_CLIENT_IP);
        }
        if (isInvalid(ipAddress)) {
            ipAddress = request.getHeader(HTTP_X_FORWARDED_FOR);
        }
        if (isInvalid(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    /**
     * 校验 ip address 是否非法
     * <p>
     * 非法返回 true 否则返回 false
     *
     * @param ipAddress ip address
     * @return bool
     */
    private static Boolean isInvalid(String ipAddress) {
        return StringUtils.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress);
    }

}
