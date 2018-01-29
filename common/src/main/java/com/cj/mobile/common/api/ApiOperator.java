package com.cj.mobile.common.api;

import com.cj.mobile.common.api.mapper.IRequestMapper;
import com.cj.mobile.common.constant.ErrorCode;
import com.cj.mobile.common.exception.ApiException;
import com.cj.mobile.common.model.ErrorInfo;
import com.cj.mobile.common.model.GsonRequest;
import com.cj.mobile.common.model.GsonResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Title: ApiOperator
 * @Package com.cj.mobile.common.api
 * @Description: Api请求转换, 对请求过程进行封装, 简化请求处理过程
 * @author: 王力杨
 * @date: 16/5/24 上午10:01
 */
public class ApiOperator {
    private IRequestMapper mapper;

    public ApiOperator(IRequestMapper mapper) {
        this.mapper = mapper;
    }


    public final <T, P> Observable<P> chain(T t, final ApiAction<T, P> action) {
        return Observable.just(t)//生成Observable<T>
                .map(new Func1<T, GsonRequest<T>>() {//转化成Observable<GsonRequest<T>>
                    @Override
                    public GsonRequest<T> call(T t) {
                        return mapper.transform(t);
                    }
                }).flatMap(new Func1<GsonRequest<T>, Observable<GsonResponse<P>>>() {//转化成Observable<GsonResponse<P>>
                    @Override
                    public Observable<GsonResponse<P>> call(GsonRequest<T> tGsonRequest) {
                        return action.chain(tGsonRequest);
                    }
                }).flatMap(this.<P>transformResponse());
    }

    public static final <P> Func1<GsonResponse<P>, Observable<P>> transformResponse() {
        return new Func1<GsonResponse<P>, Observable<P>>() {
            //转化成Observable<P>
            @Override
            public Observable<P> call(GsonResponse<P> pGsonResponse) {
                if (pGsonResponse == null) {
                    ErrorInfo errorinfo = new ErrorInfo();
                    errorinfo.setErrorcode(ErrorCode.SERVER_ERROR);
                    ApiException exp = new ApiException(errorinfo);
                    return Observable.error(exp);
                } else {
                    P data = pGsonResponse.getData();
                    ErrorInfo errorinfo = pGsonResponse.getErrorinfo();
                    if (errorinfo != null) {
                        return Observable.error(new ApiException(errorinfo));
                    } else {
                        return Observable.just(data);
                    }
                }
            }
        };
    }

    public static final <P> Func1<P, Observable<P>> transformCustomResponse() {
        return new Func1<P, Observable<P>>() {
            //转化成Observable<P>
            @Override
            public Observable<P> call(P pGsonResponse) {
                if (pGsonResponse == null) {
                    ErrorInfo errorinfo = new ErrorInfo();
                    errorinfo.setErrorcode(ErrorCode.SERVER_ERROR);
                    ApiException exp = new ApiException(errorinfo);
                    return Observable.error(exp);
                } else {
                    return Observable.just(pGsonResponse);
                }
            }
        };
    }
}
