/*
 * Copyright (c) 2018 - 2019 Maishapay
 * All rights reserved
 * Contact: contact@maishapay.online
 * Website: http://www.maishapay.online
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Maishapay developers at contact@maishapay.online.
 */

package com.maishapay.smssync.data.net;


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Base64;

import com.maishapay.smssync.BuildConfig;
import com.maishapay.smssync.data.util.Logger;
import com.maishapay.smssync.domain.entity.HttpNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * A Singleton class for accessing RequestQueue instance. It instantiates the
 * RequestQueue using the application context. This way possible memory leaks
 * are avoided in case user passes in an Activity's context.
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public abstract class BaseHttpClient {

    private static final int TIME_OUT_CONNECTION = 30;

    private static final String DEFAULT_ENCODING = "UTF-8";

    public static final MediaType JSON = MediaType.parse("application/json; charset=" + DEFAULT_ENCODING);

    public static final MediaType XML = MediaType.parse("application/json; charset=" + DEFAULT_ENCODING);

    public static final MediaType YAML = MediaType.parse("application/xml; charset=" + DEFAULT_ENCODING);

    private static final String CLASS_TAG = BaseHttpClient.class.getSimpleName();

    protected OkHttpClient mHttpClient;

    protected Context mContext;

    protected String mUrl;

    private Response mResponse;

    private Request request;

    private ArrayList<HttpNameValuePair> mParams;

    private Map<String, String> mHeader;

    private Headers mHeaders;

    private HttpMethod method = HttpMethod.GET;

    private RequestBody requestBody;


    public BaseHttpClient(Context context) {
        mContext = context;
        mParams = new ArrayList<>();
        mHeader = new HashMap<>();
        initOkHttp();
    }

    protected static String base64Encode(String str) {
        byte[] bytes = str.getBytes();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setHeader(String name, String value) {
        this.mHeader.put(name, value);
    }

    public void setHeaders(Headers mHeaders) {
        this.mHeaders = mHeaders;
    }

    private void initOkHttp() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT_CONNECTION, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_CONNECTION, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_CONNECTION, TimeUnit.SECONDS)
                .addInterceptor(getLoggingInterceptor())
                .build();
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Timber.tag("OkHttp").v(message));
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.BASIC);
        return loggingInterceptor;
    }

    private void addHeader() {

        try {
            URI uri = new URI(mUrl);
            String userInfo = uri.getUserInfo();
            if (userInfo != null) {
                setHeader("Authorization", "Basic " + base64Encode(userInfo));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // add user-agent mHeader
        try {
            final String versionName = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName;
            // Add version name to user agent
            StringBuilder userAgent = new StringBuilder("SMSSync-Android/");
            userAgent.append("v");
            userAgent.append(versionName);
            setHeader("User-Agent", userAgent.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // set mHeaders on request
        Headers.Builder mHeaderBuilder = new Headers.Builder();
        for (String key : mHeader.keySet()) {
            mHeaderBuilder.set(key, mHeader.get(key));
        }
        setHeaders(mHeaderBuilder.build());
    }

    public void addParam(String name, String value) {
        this.mParams.add(new HttpNameValuePair(name, value));
    }

    public ArrayList getParams() {
        return mParams;
    }

    public void execute() throws Exception {
        prepareRequest();
        if (request != null) {
            final Response resp = mHttpClient.newCall(request).execute();
            setResponse(resp);
        }
    }

    public boolean isMethodSupported(HttpMethod method) {
        return (method.equals(HttpMethod.GET) || method.equals(HttpMethod.POST) || method
                .equals(HttpMethod.PUT));
    }

    public void setMethod(HttpMethod method) throws Exception {
        if (!isMethodSupported(method)) {
            throw new Exception(
                    "Invalid method '" + method + "'."
                            + " POST, PUT and GET currently supported."
            );
        }
        this.method = method;
    }

    public void setRequestBody(RequestBody requestBody) throws Exception {
        this.requestBody = requestBody;
    }

    public Request getRequest() {
        return request;
    }

    private void prepareRequest() throws Exception {
        addHeader();
        // setup parameters on request
        if (method.equals(HttpMethod.GET)) {
            request = new Request.Builder()
                    .url(mUrl + getQueryString())
                    .headers(mHeaders)
                    .build();
        } else if (method.equals(HttpMethod.POST)) {
            request = new Request.Builder()
                    .url(mUrl)
                    .headers(mHeaders)
                    .post(requestBody)
                    .build();

        } else if (method.equals(HttpMethod.PUT)) {
            request = new Request.Builder()
                    .url(mUrl)
                    .headers(mHeaders)
                    .put(requestBody)
                    .build();
        }
    }

    private String getQueryString() throws Exception {
        //add query parameters
        String combinedParams = "";
        if (!mParams.isEmpty()) {
            combinedParams += "?";
            for (HttpNameValuePair p : mParams) {
                String paramString = p.getName() + "=" + URLEncoder
                        .encode(p.getValue(), DEFAULT_ENCODING);
                if (combinedParams.length() > 1) {
                    combinedParams += "&" + paramString;
                } else {
                    combinedParams += paramString;
                }
            }
        }
        return combinedParams;
    }

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

    protected void log(String message) {
        Logger.log(getClass().getName(), message);
    }

    protected void log(String format, Object... args) {
        Logger.log(getClass().getName(), format, args);
    }

    protected void log(String message, Exception ex) {
        Logger.log(getClass().getName(), message, ex);
    }

    public enum HttpMethod {
        POST("POST"),
        GET("GET"),
        PUT("PUT");

        private final String mMethod;

        HttpMethod(String method) {
            mMethod = method;
        }

        public String value() {
            return mMethod;
        }
    }
}
