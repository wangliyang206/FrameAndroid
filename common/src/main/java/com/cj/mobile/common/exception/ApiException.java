package com.cj.mobile.common.exception;

import com.cj.mobile.common.model.ErrorInfo;
import com.cj.mobile.common.constant.ErrorCode;

/**
 * @Title: ApiException
 * @Package com.cj.mobile.common.exception
 * @Description: Api请求异常信息
 * @author: 王力杨
 * @date: 16/5/24 上午10:52
 */
public class ApiException extends Exception {
    private ErrorInfo errorInfo;

    public ApiException(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public int getErrorCode() {
        if (errorInfo != null) {
            return errorInfo.getErrorcode();
        } else {
            return ErrorCode.OTHER_ERROR;
        }
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
}
