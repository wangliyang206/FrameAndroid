package com.cj.mobile.common.model;

/**
 * 包名： com.cj.mobile.common.model
 * 对象名： TokenInvalid
 * 描述：Token失效
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/11/6 9:48
 */

public class TokenInvalid {
    /** 错误编码 */
    private int code = -1;

    /** 错误提示 */
    private String msg = "";

    public TokenInvalid(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public TokenInvalid() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
