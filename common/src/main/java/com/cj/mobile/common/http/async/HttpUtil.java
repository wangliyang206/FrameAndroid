package com.cj.mobile.common.http.async;

import android.content.Context;

import com.cj.mobile.common.R;
import com.cj.mobile.common.model.JsonMsgIn;
import com.cj.mobile.common.model.JsonMsgOut;
import com.cj.mobile.common.util.ActivityUtils;
import com.cj.mobile.common.util.JsonUtil;
import com.cj.mobile.common.util.MobileLoadingUtil;
import com.cj.mobile.common.util.MobileUtil;
import com.cj.mobile.common.util.Validate;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.StringEntity;
import timber.log.Timber;

import static okhttp3.internal.Util.UTF_8;

/**
 * 网络请求工具类
 *
 * @author 王力杨
 */
public class HttpUtil {
    private HttpUtil() {
    }

    public final static Byte[] locks = new Byte[0];

    /**
     * 超时时间原为20秒，现为30秒
     */
    private final static int TIMEOUT_MISECOND = 1000 * 30;
    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 请求网络时展示的效果
     */
    private static MobileLoadingUtil.LoadingHandler loadingHandler = null;

    /**
     * 请求监听接口，每个类在调用(HTTP、HTTPS)的请求时都需要实现此接口
     */
    public interface CSSHttpListener {
        public void onOver(JsonMsgOut jsonMsgOut);

        public void onError(Throwable error, String content);
    }

    /**
     * 请求监听接口，每个类在调用(HTTP、HTTPS)的请求时都需要实现此接口
     */
    public interface WWYLHttpListener {
        public void onOver(int statusCode, Map<String, Object> params, String responseString);

        public void onError(int statusCode, Throwable error, String content);
    }

    /**
     * 请求监听接口，每个类在调用(HTTP、HTTPS)的请求时都需要实现此接口
     */
    public interface CSSStringListener {
        public void onOver(String str);

        public void onError(Throwable error, String content);
    }

    /**
     * 监听下载请求接口
     */
    public interface CSSHttpFileListener {
        public void onOver(byte[] fileData);

        public void onError(Throwable error, byte[] fileData);
    }

    /**
     * 监听下载文件请求接口
     */
    public interface CSSHttpSourceFileListener {
        public void onOver(String id, String localPath);

        public void onError(Throwable error, String id, String localPath);
    }

    /**
     * 监听上传请求接口
     */
    public interface CSSHttpUploadListener {
        public void onOver(JsonMsgOut jsonMsgOut);

        public void onError(Throwable error, String content);

        public void onProgress(long bytesWritten, long totalSize);
    }

	/*------------------------------------------------http网络请求------------------------------------------------*/

    /**
     * 发送数据到服务器(Http)
     *
     * @param context         句柄
     * @param jsonMsgIn       发送的内容
     * @param url             访问的地址
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttp(final Context context, JsonMsgIn jsonMsgIn, String url, final boolean isProcess, String processTips, final CSSHttpListener cssHttpListener) {
        synchronized (locks) {
            RequestParams params = null;
            if (jsonMsgIn != null) {
                // 转换为Json字符串
                String jsonStr = JsonUtil.createJsonString(jsonMsgIn);
                // 注意：服务器端接受Json字符串的属性必须是jsonstr
                params = new RequestParams("jsonstr", jsonStr);
            }

            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);
            post(context, url, params, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        responseString = ActivityUtils.getString(context, R.string.common_check_network);
                        cssHttpListener.onError(throwable, responseString);
                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null && responseString != null && !"".equals(responseString)) {
                        JsonMsgOut jsonMsgOut = JsonUtil.getObject(responseString, JsonMsgOut.class);
                        cssHttpListener.onOver(jsonMsgOut);
                    } else {
                        cssHttpListener.onError(null, ActivityUtils.getString(context, R.string.common_server_error));
                    }
                }
            });
        }
    }

    /**
     * 发送数据到服务器(Http)
     *
     * @param context         句柄
     * @param jsonMsgIn       发送的内容
     * @param url             访问的地址
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttp(final Context context, JsonMsgIn jsonMsgIn, String url, final boolean isProcess, String processTips, final CSSStringListener cssHttpListener) {
        synchronized (locks) {
            RequestParams params = null;
            if (jsonMsgIn != null) {
                // 转换为Json字符串
                String jsonStr = JsonUtil.createJsonString(jsonMsgIn);
                // 注意：服务器端接受Json字符串的属性必须是jsonstr
                params = new RequestParams("jsonstr", jsonStr);
            }

            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);

            post(context, url, params, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        responseString = ActivityUtils.getString(context, R.string.common_check_network);
                        cssHttpListener.onError(throwable, responseString);
                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null && responseString != null && !"".equals(responseString)) {
                        cssHttpListener.onOver(responseString);
                    } else {
                        cssHttpListener.onError(null, ActivityUtils.getString(context, R.string.common_server_error));
                    }
                }
            });
        }
    }

	/*------------------------------------------------https网络请求------------------------------------------------*/

    /**
     * 发送数据到服务器(Https)
     *
     * @param context         句柄
     * @param jsonMsgIn       发送的内容
     * @param url             访问地址
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param desKey          加密密钥
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttps(final Context context, JsonMsgIn jsonMsgIn, String url, final boolean isProcess, String processTips, final String desKey, final CSSHttpListener cssHttpListener) {
        synchronized (locks) {
            RequestParams params = null;
            if (jsonMsgIn != null) {
                // 转换为Json字符串
                String jsonStr = JsonUtil.createJsonString(jsonMsgIn);
                // 注意：服务器端接受Json字符串的属性必须是jsonstr
                params = new RequestParams("jsonstr", jsonStr);
            }

            try {
                SSLSocketFactory sslFactory = new MySSLSocketFactory(null);
                sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client.setSSLSocketFactory(sslFactory);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);
            post(context, url, params, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        responseString = ActivityUtils.getString(context, R.string.common_check_network);
                        cssHttpListener.onError(throwable, responseString);
                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null && responseString != null && !"".equals(responseString)) {
                        JsonMsgOut jsonMsgOut = JsonUtil.getObject(responseString, JsonMsgOut.class);
                        cssHttpListener.onOver(jsonMsgOut);
                    } else {
                        cssHttpListener.onError(null, ActivityUtils.getString(context, R.string.common_server_error));
                    }
                }
            });
        }
    }

    /**
     * 发送数据到服务器(Https)Post
     *
     * @param context         句柄
     * @param params          发送的内容
     * @param url             访问地址
     * @param contentType     内容类型，例如：application/x-www-form-urlencoded或application/json
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttps(final Context context, Map<String, Object> params, String url, String contentType, String token, final boolean isProcess, String processTips, final WWYLHttpListener cssHttpListener) {
        synchronized (locks) {
            StringEntity stringEntity = null;
            try {
                if (params != null) {
                    // 转换为Json字符串
                    String jsonStr = JsonUtil.createJsonString(params);
                    stringEntity = new StringEntity(jsonStr, UTF_8);
//                    stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
//                    stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, HTTP.UTF_8));
                }
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }

            try {
                SSLSocketFactory sslFactory = new MySSLSocketFactory(null);
                sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client.setSSLSocketFactory(sslFactory);

                client.addHeader("Authorization", "Bearer " + token);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);
            post(context, url, stringEntity, contentType, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {//如果Map为空并且String不为空情况下说明服务端有返回错误；如果map有内容则说明有特殊格式
                            cssHttpListener.onError(statusCode, throwable, responseString);
                        } else {
                            if (Validate.isNotEmpty(params) && !Validate.isEmptyObjByStr(params.get("message"))) {//如果params对象中message不为空，则提示message
                                cssHttpListener.onError(statusCode, throwable, params.get("message").toString());
                            } else {
                                cssHttpListener.onError(statusCode, throwable, getErrorMsg(context, statusCode));
                            }
                        }

                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (statusCode == 200 || statusCode == 201) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {
                            if (responseString.equalsIgnoreCase("true")) {
                                params.put("responseString", true);
                            } else if (responseString.equalsIgnoreCase("false")) {
                                params.put("responseString", false);
                            } else
                                params.put("responseString", responseString);
                        }
                        cssHttpListener.onOver(statusCode, params, responseString);
                    } else {
                        cssHttpListener.onError(statusCode, null, getErrorMsg(context, statusCode));
                    }
                }
            });
        }
    }

    /**
     * 发送数据到服务器(Https)Delete
     *
     * @param context         句柄
     * @param params          发送的内容
     * @param url             访问地址
     * @param contentType     内容类型，例如：application/x-www-form-urlencoded或application/json
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttpsDelete(final Context context, Map<String, Object> params, String url, String contentType, String token, final boolean isProcess, String processTips, final WWYLHttpListener cssHttpListener) {
        synchronized (locks) {
            StringEntity stringEntity = null;
            try {
                if (params != null) {
                    // 转换为Json字符串
                    String jsonStr = JsonUtil.createJsonString(params);
                    stringEntity = new StringEntity(jsonStr, UTF_8);
//                    stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
                }
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }

            client.addHeader("Authorization", "Bearer " + token);

            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);
            del(context, url, stringEntity, contentType, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {//如果Map为空并且String不为空情况下说明服务端有返回错误；如果map有内容则说明有特殊格式
                            cssHttpListener.onError(statusCode, throwable, responseString);
                        } else {
                            if (Validate.isNotEmpty(params) && !Validate.isEmptyObjByStr(params.get("message"))) {//如果params对象中message不为空，则提示message
                                cssHttpListener.onError(statusCode, throwable, params.get("message").toString());
                            } else {
                                cssHttpListener.onError(statusCode, throwable, getErrorMsg(context, statusCode));
                            }
                        }

                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (statusCode == 200 || statusCode == 201) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {
                            if (responseString.equalsIgnoreCase("true")) {
                                params.put("responseString", true);
                            } else if (responseString.equalsIgnoreCase("false")) {
                                params.put("responseString", false);
                            } else
                                params.put("responseString", responseString);
                        }
                        cssHttpListener.onOver(statusCode, params, responseString);
                    } else {
                        cssHttpListener.onError(statusCode, null, getErrorMsg(context, statusCode));
                    }
                }
            });
        }
    }

    /**
     * 发送数据到服务器(Https)Put
     *
     * @param context         句柄
     * @param params          发送的内容
     * @param url             访问地址
     * @param contentType     内容类型，例如：application/x-www-form-urlencoded或application/json
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttpsPut(final Context context, Map<String, Object> params, String url, String contentType, String token, final boolean isProcess, String processTips, final WWYLHttpListener cssHttpListener) {
        synchronized (locks) {
            StringEntity stringEntity = null;
            try {
                if (params != null) {
                    // 转换为Json字符串
                    String jsonStr = JsonUtil.createJsonString(params);
                    stringEntity = new StringEntity(jsonStr, UTF_8);
//                    stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
                }
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }

            client.addHeader("Authorization", "Bearer " + token);

            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);
            put(context, url, stringEntity, contentType, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {//如果Map为空并且String不为空情况下说明服务端有返回错误；如果map有内容则说明有特殊格式
                            cssHttpListener.onError(statusCode, throwable, responseString);
                        } else {
                            if (Validate.isNotEmpty(params) && !Validate.isEmptyObjByStr(params.get("message"))) {//如果params对象中message不为空，则提示message
                                cssHttpListener.onError(statusCode, throwable, params.get("message").toString());
                            } else {
                                cssHttpListener.onError(statusCode, throwable, getErrorMsg(context, statusCode));
                            }
                        }

                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (statusCode == 200 || statusCode == 201) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {
                            if (responseString.equalsIgnoreCase("true")) {
                                params.put("responseString", true);
                            } else if (responseString.equalsIgnoreCase("false")) {
                                params.put("responseString", false);
                            } else
                                params.put("responseString", responseString);
                        }
                        cssHttpListener.onOver(statusCode, params, responseString);
                    } else {
                        cssHttpListener.onError(statusCode, null, getErrorMsg(context, statusCode));
                    }
                }
            });
        }
    }

    /**
     * 获取错误信息
     *
     * @param context
     * @param statusCode
     * @return
     */
    private static String getErrorMsg(Context context, int statusCode) {
        String result = ActivityUtils.getString(context, R.string.common_server_error);
        switch (statusCode) {
//            case 201:
//                result = "Created";
//                break;
            case 400://请求无效
                result = "Bad Request";//ActivityUtils.getString(context, R.string.common_check_network);
                break;
            case 401://未授权 登录失败
                result = "Unauthorized";
                break;
            case 403://禁止访问
                result = "Forbidden";
                break;
            case 404://无法找到 Web 站点
                result = "Not Found";
                break;
            case 500://内部服务器错误
                result = ActivityUtils.getString(context, R.string.common_server_error);
                break;
            case 501://未实现
                result = "Not Found";
                break;
            case 502://网关错误
                result = "Not Found";
                break;
        }
        return result;
    }

    /**
     * 发送数据到服务器(Https)GET请求
     *
     * @param context         句柄
     * @param params          发送的内容
     * @param token           token
     * @param url             访问地址
     * @param contentType     内容类型，例如：application/x-www-form-urlencoded或application/json
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttpsByGet(final Context context, Map<String, Object> params, String token, String url, String contentType, final boolean isProcess, String processTips, final WWYLHttpListener cssHttpListener) {
        synchronized (locks) {
            StringEntity stringEntity = null;
            try {
                if (params != null) {
                    // 转换为Json字符串
                    String jsonStr = JsonUtil.createJsonString(params);
                    stringEntity = new StringEntity(jsonStr, UTF_8);
//                    stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
                }
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
            try {
                SSLSocketFactory sslFactory = new MySSLSocketFactory(null);
                sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client.setSSLSocketFactory(sslFactory);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            client.addHeader("Authorization", "Bearer " + token);
            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);
            get(context, url, stringEntity, contentType, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {//如果Map为空并且String不为空情况下说明服务端有返回错误；如果map有内容则说明有特殊格式
                            cssHttpListener.onError(statusCode, throwable, responseString);
                        } else {
                            if (Validate.isNotEmpty(params) && !Validate.isEmptyObjByStr(params.get("message"))) {//如果params对象中message不为空，则提示message
                                cssHttpListener.onError(statusCode, throwable, params.get("message").toString());
                            } else {
                                cssHttpListener.onError(statusCode, throwable, getErrorMsg(context, statusCode));
                            }
                        }
                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (statusCode == 200 || statusCode == 201) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {
                            if (responseString.equalsIgnoreCase("true")) {
                                params.put("responseString", true);
                            } else if (responseString.equalsIgnoreCase("false")) {
                                params.put("responseString", false);
                            } else
                                params.put("responseString", responseString);
                        }
                        cssHttpListener.onOver(statusCode, params, responseString);
                    } else {
                        cssHttpListener.onError(statusCode, null, getErrorMsg(context, statusCode));
                    }
                }
            });

        }
    }

    /**
     * 发送数据到服务器(Https)
     *
     * @param context         句柄
     * @param jsonMsgIn       发送的内容
     * @param url             访问地址
     * @param isProcess       是否有loading效果
     * @param processTips     过程提示
     * @param desKey          加密密钥
     * @param cssHttpListener 回调函数
     */
    public static void sendToServerHttps(final Context context, JsonMsgIn jsonMsgIn, String url, final boolean isProcess, String processTips, final String desKey, final CSSStringListener cssHttpListener) {
        synchronized (locks) {
            RequestParams params = null;
            if (jsonMsgIn != null) {
                // 转换为Json字符串
                String jsonStr = JsonUtil.createJsonString(jsonMsgIn);
                // 注意：服务器端接受Json字符串的属性必须是jsonstr
                params = new RequestParams("jsonstr", jsonStr);
            }

            try {
                SSLSocketFactory sslFactory = new MySSLSocketFactory(null);
                sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client.setSSLSocketFactory(sslFactory);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);
            post(context, url, params, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        responseString = ActivityUtils.getString(context, R.string.common_check_network);
                        cssHttpListener.onError(throwable, responseString);
                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null && responseString != null && !"".equals(responseString)) {
                        cssHttpListener.onOver(responseString);
                    } else {
                        cssHttpListener.onError(null, ActivityUtils.getString(context, R.string.common_server_error));
                    }
                }
            });
        }
    }

	/*------------------------------------------------下载------------------------------------------------*/

    /**
     * 下载图片，返回图片流
     *
     * @param context             句柄
     * @param isAsync             是否为异步请求(true=异步；false=同步)；外部调用注意：如果当前为异步请求，请不要在子线程中调用。
     * @param jsonMsgIn           请求的内容
     * @param url                 请求的URL
     * @param cssHttpFileListener 请求结束后的回调
     */
    public static void DownloadImageFile(Context context, boolean isAsync, JsonMsgIn jsonMsgIn, String url, final CSSHttpFileListener cssHttpFileListener) {
        synchronized (locks) {
            RequestParams params = null;
            if (jsonMsgIn != null) {
                // 转换为Json字符串
                String jsonStr = JsonUtil.createJsonString(jsonMsgIn);
                // 注意：服务器端接受Json字符串的属性必须是jsonstr
                params = new RequestParams("jsonstr", jsonStr);
            }

            /**请求回调(监听请求返回结果)*/
            BinaryHttpResponseHandler binaryHttpResponseHandler = new BinaryHttpResponseHandler() {
                @Override
                public String[] getAllowedContentTypes() {
                    // Allowing all data for debug purposes
                    return new String[]{".*"};
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] binaryData) {
                    // 回调程序界面中的接口实现
                    if (cssHttpFileListener != null) {
                        cssHttpFileListener.onOver(binaryData);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] binaryData, Throwable error) {
                    // 回调程序界面中的接口实现
                    if (cssHttpFileListener != null) {
                        cssHttpFileListener.onError(error, binaryData);
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    //("onProgress：bytesWritten="+bytesWritten+"，totalSize="+totalSize);
                }
            };

            if (isAsync) {            //异步
                post(context, url, params, binaryHttpResponseHandler);
            } else {                //同步
                SyncHttpClient client = new SyncHttpClient();
                post(client, context, url, params, binaryHttpResponseHandler);
            }

        }
    }

    /*------------------------------------------------上传------------------------------------------------*/
    public static void UploadFile(final Context context, JsonMsgIn jsonMsgIn, String url, List<String> files, String token, final boolean isProcess, String processTips, final WWYLHttpListener cssHttpListener) {
        synchronized (locks) {
            int index = -1;
            RequestParams params = new RequestParams();
            if (jsonMsgIn != null) {
                // 转换为Json字符串
                String jsonStr = JsonUtil.createJsonString(jsonMsgIn);
                params.put("jsonstr", jsonStr);
            }

            for (String file : files) {
                try {
                    index++;
                    params.put("file-" + index, new File(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            params.put("filesCount", String.valueOf(index + 1));

            client.addHeader("Authorization", "Bearer " + token);

            if (isProcess)
                loadingHandler = MobileLoadingUtil.showLoadingDialog(context, processTips, null);

            post(context, url, params, new TextHttpResponseHandler() {

                /**此处为服务器调用失败的返回*/
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("failure");

                    Timber.i("[失败:" + statusCode + "]响应结构：" + responseString.toString());

                    // 回调程序界面中的接口实现
                    if (cssHttpListener != null) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {//如果Map为空并且String不为空情况下说明服务端有返回错误；如果map有内容则说明有特殊格式
                            cssHttpListener.onError(statusCode, throwable, responseString);
                        } else {
                            cssHttpListener.onError(statusCode, throwable, getErrorMsg(context, statusCode));
                        }
                    }
                }

                /**此处为服务器调用成功的返回*/
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // 关闭loading
                    if (isProcess)
                        if (loadingHandler != null)
                            loadingHandler.sendMsg("success");

                    Timber.i("响应结构：" + responseString.toString());
                    // 回调程序界面中的接口实现
                    if (statusCode == 200 || statusCode == 201) {
                        Map<String, Object> params = JsonUtil.getMapObj(responseString);
                        if (!Validate.isNotEmpty(params) && !Validate.isEmpty(responseString)) {
                            if (responseString.equalsIgnoreCase("true")) {
                                params.put("responseString", true);
                            } else if (responseString.equalsIgnoreCase("false")) {
                                params.put("responseString", false);
                            } else
                                params.put("responseString", responseString);
                        }
                        cssHttpListener.onOver(statusCode, params, responseString);
                    } else {
                        cssHttpListener.onError(statusCode, null, getErrorMsg(context, statusCode));
                    }
                }
            });
        }
    }

	/*---------------------------------------------请求方式---------------------------------------------*/

    /**
     * 请求方式(GET)
     *
     * @param context         句柄
     * @param url             请求地址
     * @param stringentity    请求参数
     * @param contentType     内容类型
     * @param responseHandler 回调
     */
    public static void get(Context context, String url, StringEntity stringentity, String contentType, AsyncHttpResponseHandler responseHandler) {
        Timber.i("请求地址：" + url + " \n," + "请求结构：" + stringentity.toString());
        client.addHeader("Accept-Encoding", "gzip");
        client.setTimeout(TIMEOUT_MISECOND);
//		client.get(url, params, responseHandler);
        client.get(context, url, stringentity, contentType, responseHandler);
        client.setUserAgent(MobileUtil.getUserAgentBrowser(context));
    }

    /**
     * 请求方式(GET)
     *
     * @param context         句柄
     * @param url             请求地址
     * @param params          请求参数
     * @param responseHandler 回调
     */
    public static void get(Context context, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        Timber.i("请求地址：" + url + " \n," + "请求结构：" + params.toString());
        client.addHeader("Accept-Encoding", "gzip");
        client.setTimeout(TIMEOUT_MISECOND);
//		client.get(url, params, responseHandler);
        client.get(context, url, params, responseHandler);
        client.setUserAgent(MobileUtil.getUserAgentBrowser(context));
    }

    /**
     * 请求方式(PUT)
     *
     * @param context         句柄
     * @param url             请求地址
     * @param stringentity    请求参数
     * @param contentType     内容类型
     * @param responseHandler 回调
     */
    public static void put(Context context, String url, StringEntity stringentity, String contentType, AsyncHttpResponseHandler responseHandler) {
        Timber.i("请求地址：" + url + " \n," + "请求结构：" + stringentity.toString());
        client.addHeader("Accept-Encoding", "gzip");
        client.setTimeout(TIMEOUT_MISECOND);
        client.put(context, url, stringentity, contentType, responseHandler);
        client.setUserAgent(MobileUtil.getUserAgentBrowser(context));
    }

    /**
     * 请求方式(DEL)
     *
     * @param context         句柄
     * @param url             请求地址
     * @param stringentity    请求参数
     * @param contentType     内容类型
     * @param responseHandler 回调
     */
    public static void del(Context context, String url, StringEntity stringentity, String contentType, AsyncHttpResponseHandler responseHandler) {
        Timber.i("请求地址：" + url + " \n," + "请求结构：" + stringentity.toString());
        client.addHeader("Accept-Encoding", "gzip");
        client.setTimeout(TIMEOUT_MISECOND);
        client.delete(context, url, stringentity, contentType, responseHandler);
        client.setUserAgent(MobileUtil.getUserAgentBrowser(context));
    }

    /**
     * 请求方式(POST)
     *
     * @param context         句柄
     * @param url             请求地址
     * @param stringentity    请求参数
     * @param contentType     内容类型
     * @param responseHandler 回调
     */
    public static void post(Context context, String url, StringEntity stringentity, String contentType, AsyncHttpResponseHandler responseHandler) {
        Timber.i("请求地址：" + url + " \n," + "请求结构：" + stringentity.toString());
        client.addHeader("Accept-Encoding", "gzip");
        client.setTimeout(TIMEOUT_MISECOND);
        client.post(context, url, stringentity, contentType, responseHandler);
        client.setUserAgent(MobileUtil.getUserAgentBrowser(context));
    }

    /**
     * 请求方式(POST)
     *
     * @param context         句柄
     * @param url             请求地址
     * @param params          请求参数
     * @param responseHandler 回调
     */
    public static void post(Context context, String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        Timber.i("请求地址：" + url + " \n," + "请求结构：" + params.toString());
        client.addHeader("Accept-Encoding", "gzip");
        client.setTimeout(TIMEOUT_MISECOND);
//		client.post(url, params, responseHandler);
        client.post(context, url, params, responseHandler);
        client.setUserAgent(MobileUtil.getUserAgentBrowser(context));
    }

    /**
     * 同步请求方式(POST)
     *
     * @param context         句柄
     * @param url             请求地址
     * @param params          请求参数
     * @param responseHandler 回调
     */
    public static void post(SyncHttpClient client, Context context, String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        Timber.i("请求地址：" + url + " \n," + "请求结构：" + params.toString());
        client.addHeader("Accept-Encoding", "gzip");
        client.setTimeout(TIMEOUT_MISECOND);
//		client.post(url, params, responseHandler);
        client.post(context, url, params, responseHandler);
        client.setUserAgent(MobileUtil.getUserAgentBrowser(context));
    }

	/*---------------------------------------------取消请求---------------------------------------------*/

    /**
     * 取消指定的请求
     *
     * @param context               句柄
     * @param mayInterruptIfRunning specifies if active requests should be cancelled along with pending requests.
     */
    public static void cancelRequest(Context context, boolean mayInterruptIfRunning) {
        client.cancelRequests(context, mayInterruptIfRunning);
    }

    /**
     * 取消全部请求
     *
     * @param mayInterruptIfRunning specifies if active requests should be cancelled along with pending requests.
     */
    public static void cancelAllRequest(boolean mayInterruptIfRunning) {
        client.cancelAllRequests(mayInterruptIfRunning);
    }
}
