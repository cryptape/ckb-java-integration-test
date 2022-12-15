package com.ckb.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class HttpUtils {

    /**
     * POST 请求
     *
     * @param url        接口地址
     * @param headersMap 消息头
     * @param paramsMap  参数集合
     * @return Response
     */
    public static Response post(String url, Map<String, Object> headersMap, Map<String, Object> paramsMap) {
        RequestSpecification given = given();
        if (headersMap != null) {
            given.headers(headersMap);
        }
        if (paramsMap != null) {
            given.params(paramsMap);
        }
        return given().
                log().all().
        contentType(ContentType.JSON).
                headers(headersMap).
//                params(paramsMap).
                body(paramsMap).
                post(url);
    }

    public static Response post(String url, Map<String, Object> headersMap, String body) {
        RequestSpecification given = given();
        if (headersMap != null) {
            given.headers(headersMap);
        }
        if (body != null) {
            given.body(body);
        }
        return given().
                log().all().
        contentType(ContentType.JSON).
                headers(headersMap).
                body(body).
                post(url);
    }

    /**
     * GET 请求
     *
     * @param url        接口地址
     * @param headersMap 消息头
     * @param paramsMap  参数集合
     * @return Response
     */
    public static Response get(String url, Map<String, Object> headersMap, Map<String, Object> paramsMap) {
        RequestSpecification given = given();
        if (headersMap != null) {
            given.headers(headersMap);
        }
        if (paramsMap != null) {
            given.params(paramsMap);
        }
        return given.
                log().all().
        contentType(ContentType.JSON).
                get(url);
    }

    /**
     * getByQueryParams 请求
     *
     * @param url        接口地址
     * @param headersMap 消息头
     * @param paramsMap  参数集合
     * @return Response
     */
    public static Response getByQueryParams(String url, Map<String, Object> headersMap, Map<String, Object> paramsMap) {
        RequestSpecification given = given();
        if (headersMap != null) {
            given.headers(headersMap);
        }
        if (paramsMap != null) {
            given.queryParams(paramsMap);
        }
        return given.
                log().all().
                contentType(ContentType.JSON).
                get(url);
    }
}
