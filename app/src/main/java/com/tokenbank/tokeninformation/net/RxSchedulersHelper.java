package com.tokenbank.tokeninformation.net;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

/**
 * Author: Clement
 * Create: 2018/5/21
 * Desc: 用于接收Json返回的数据
 */

public class RxSchedulersHelper {

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> schedulerNewThreadToMain() {
        return (ObservableTransformer<T, T>) sSchedulersTransformer;
    }

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> schedulerNewThreadToMainWithDelay() {
        return (ObservableTransformer<T, T>) sSchedulersTransformerWithDelay;
    }

    /**
     * 线程转换器
     */
    private static ObservableTransformer sSchedulersTransformer = new ObservableTransformer() {
        @Override public ObservableSource apply(@NonNull Observable upstream) {
            return upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * 有时间延迟的线程转换器
     */
    private static ObservableTransformer sSchedulersTransformerWithDelay =
        new ObservableTransformer() {
            @Override public ObservableSource apply(@NonNull Observable upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
}
