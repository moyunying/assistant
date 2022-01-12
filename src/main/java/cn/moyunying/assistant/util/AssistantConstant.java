package cn.moyunying.assistant.util;

public interface AssistantConstant {

    /**
     * 会员超时时间
     */
    int MEMBER_EXPIRE_SECONDS = 3600 * 24 * 7;

    /**
     * 会员续费时间
     */
    long RECHARGE_EXPIRE_SECONDS = 3600 * 24 * 30;

    /**
     * 登录凭证超时时间
     */
    int LOGIN_EXPIRE_SECONDS = 3600 * 24;

    /**
     * 汉语
     */
    String ZH = "zh";
    String CN = "cn";

    /**
     * 日语
     */
    String JP = "jp";
    String JA = "ja";
}
