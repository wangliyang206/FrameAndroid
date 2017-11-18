package com.cj.mobile.common.domain.usecase;

import android.content.Context;

import com.cj.mobile.common.R;
import com.cj.mobile.common.constant.ErrorCode;
import com.cj.mobile.common.exception.ApiException;
import com.cj.mobile.common.exception.HttpException;
import com.cj.mobile.common.http.rx.RxBus;
import com.cj.mobile.common.model.ErrorInfo;
import com.cj.mobile.common.model.TokenInvalid;

import java.io.IOException;

/**
 * 包名:com.chinamall21.mobile.flyingelectric.execption
 * 对象名: ErrorBundle
 * 描述:错误解析类的工厂类，用于解析错误
 * 作者: 赵志军
 * 邮箱：zhijun.zhao@21chinamall.com
 * 创建日期: 2016/5/27 10:02
 */
public class ErrorBundle implements IErrorBundle {
    private Context mContext;

    public static ErrorBundle errorBundle;

    /**
     * token失效(业务定义时不能是-1)
     */
    private static int tokenInvalid = -1;

    public static ErrorBundle init(Context mContext,int tokenCode) {
        tokenInvalid = tokenCode;

        if (errorBundle == null) {
            errorBundle = new ErrorBundle(mContext);
        }

        return errorBundle;
    }

    private ErrorBundle(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public <T> void praseError(Throwable throwable, UseCase.UseCaseCallback<T> useCase) {
        ParseThrowable parseThrowable = new ParseThrowable(throwable);

        //错误分两种，一种是Token失效，另一种是业务错误。
        if (parseThrowable.getErrorCode() == tokenInvalid && tokenInvalid > -1) {
            RxBus.getDefault().post(new TokenInvalid(parseThrowable.getErrorCode(), parseThrowable.getErrorMessage()));
        } else {
            useCase.onError(parseThrowable.getErrorCode(), parseThrowable.getErrorMessage());
        }
    }

    private class ParseThrowable {
        private Throwable exception;

        public ParseThrowable(Throwable exception) {
            this.exception = exception;
        }


        public Exception getException() {
            return new Exception(exception);
        }

        public String getErrorMessage() {
            if (exception instanceof ApiException) {
                ErrorInfo errorInfo = ((ApiException) exception).getErrorInfo();
                if (errorInfo != null) {
                    return errorInfo.getErrorcode() + ":" + errorInfo.getErrormessage();
                }
            } else if (exception instanceof IOException) {
                return mContext.getString(R.string.net_error);
            } else if (exception instanceof HttpException) {
                return mContext.getString(R.string.common_server_error);
            }
            return exception.getMessage();
        }

        public int getErrorCode() {
            if (exception instanceof ApiException) {
                ErrorInfo errorInfo = ((ApiException) exception).getErrorInfo();
                if (errorInfo != null) {
                    return errorInfo.getErrorcode();
                }
            } else if (exception instanceof IOException) {
                return ErrorCode.SERVER_ERROR;
            } else if (exception instanceof HttpException) {
                return ((HttpException) exception).getExceptionCode();
            }
            return ErrorCode.OTHER_ERROR;
        }
    }
}
