package com.tokenbank.tokeninformation.net.subscriber;

import rx.Subscriber;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc: 继承，外层不必每次都重写这三个方法
 */

public class BaseSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }

}