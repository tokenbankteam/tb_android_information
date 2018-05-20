package com.tokenbank.tokeninformation.net;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc: 用于接收Json返回的数据
 */

public class HttpResult<T> {

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private int result;

    @SerializedName("data")
    private T data;

    public boolean isSuccess() {
        //result的值等于0，表示请求成功
        return result == 0;
    }

    public T getData() {
        return data;
    }

}
