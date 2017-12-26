package com.bird.core.sso;

import java.util.List;

public class JavascriptCodeGenerator {

    /**
     * 登录成功后通知的js方法
     */
    private String notifyFuncName = "sso.notify";

    /**
     * 登录失败通知的js方法
     */
    private String errorFuncName = "sso.error";


    /**
     * 获取登录成功后前端js执行代码
     *
     * @param token       token
     * @param notifyUrls  通知的地址集合
     * @param redirectUrl 登录成功后跳转地址
     * @return
     */
    public String getLoginCode(String token, List<String> notifyUrls, String redirectUrl) {
        StringBuilder sb = new StringBuilder(notifyFuncName + "('");
        sb.append(redirectUrl);
        sb.append("'");

        for (int i = 0; i < notifyUrls.size(); i++) {
            sb.append(",'" + notifyUrls.get(i) + token + "'");
        }
        sb.append(");");
        return sb.toString();
    }

    /**
     * 获取注销前端执行js代码
     * @param notifyUrls 需要通知的地址集合
     * @return
     */
    public String getLogoutCode(List<String> notifyUrls){
        StringBuilder sb = new StringBuilder(notifyFuncName + "('");
        sb.append("refresh");
        sb.append("'");

        for (int i = 0; i < notifyUrls.size(); i++) {
            sb.append(",'" + notifyUrls.get(i) + "'");
        }
        sb.append(");");
        return sb.toString();
    }



    /**
     * 获取登录失败后前端执行的js代码
     *
     * @param message 错误信息
     * @return
     */
    public String getErrorCode(String message) {
        return errorFuncName + "('" + message + "');";
    }
}
