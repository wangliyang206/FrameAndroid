package com.cj.mobile.common.model;

/**
 * @Title: ErrorInfo
 * @Package com.cj.mobile.common.model
 * @Description: 网络请求的错误信息
 * @author: 王力杨
 * @date: 16/5/24 上午9:35
 */
public class ErrorInfo {
    // 错误代码（负数表示错误，正数是警告）
    private Integer errorcode;
    // 错误提示
    private String errormessage = "";
    // 错误原因
    private String cause = "";
    // 解决方式
    private String action = "";

    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
