package com.tbc.ddd.common.tools;

import org.apache.commons.lang3.StringUtils;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

/**
 * UserAgent 处理工具类
 *
 * @author Johnson.Jia
 */
public class UserAgentUtils {

    static String APPLE_IPHONE = "Apple iPhone";

    static final UserAgentAnalyzer parse;

    static {
        parse = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(2000).withField(UserAgent.DEVICE_NAME)
            .withField(UserAgent.DEVICE_VERSION).withField(UserAgent.OPERATING_SYSTEM_NAME_VERSION).build();
    }

    public static void init() {
        parse.initializeMatchers();
    }

    /**
     * 获取设备型号 信息
     *
     * @param userAgent
     * @return
     * @author Johnson.Jia
     * @date 2018年6月25日 下午4:53:33
     */
    public static String getModelInfo(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return UserAgent.UNKNOWN_VALUE;
        }
        UserAgent agent = parse.parse(userAgent);
        StringBuilder sb = new StringBuilder();
        String operatingSystemNameVersion = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION);
        if (StringUtils.isBlank(operatingSystemNameVersion)
            || UserAgent.UNKNOWN_NAME_VERSION.equals(operatingSystemNameVersion)) {
            return UserAgent.UNKNOWN_VALUE;
        }
        sb.append(operatingSystemNameVersion);
        String deviceName = agent.getValue(UserAgent.DEVICE_NAME);
        if (StringUtils.isNotBlank(deviceName) && !deviceName.contains(APPLE_IPHONE)) {
            sb.append(" ");
            sb.append(deviceName);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String userAgent =
            "Mozilla/5.0 (iPhone; CPU iPhone OS 11_1_2 like Mac OS X) AppleWebKit/604.3.5 (KHTML, like Gecko) "
                + "Mobile/15B202 cloudstudy-revive-applestore/6.3.6 (iPhone7,2; iOS 11.1.2; Scale/2.00)";
        System.out.println(getModelInfo(userAgent));

        userAgent = "Dalvik/2.1.0 (Linux; U; Android 8.1.0; PACM00 Build/O11019)";
        System.out.println(getModelInfo(userAgent));

        userAgent = "Dalvik/2.1.0 (Linux; U; Android 8.1.0; PACM00 Build/O11019)";
        System.out.println(getModelInfo(userAgent));

        userAgent =
            "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Mobile Safari/537.36";
        System.out.println(getModelInfo(userAgent));

        userAgent = "Airmail 1.3.3 rv:237 (Macintosh; Mac OS X 10.9.3; nl_NL)";
        System.out.println(getModelInfo(userAgent));

        userAgent =
            "Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Mobile Safari/537.36";
        System.out.println(getModelInfo(userAgent));
        userAgent = "okhttp/3.5.0";
        System.out.println(getModelInfo(userAgent));
    }
}
