package cn.shh.project.reggie.util;

public class ReggieConstant {
    // 重新登录响应信息，触发跳到登录页
    public static final String NOT_LOGIN = "NOTLOGIN";

    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;
}
