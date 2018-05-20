package com.tokenbank.tokeninformation.net.manager;

import com.tokenbank.tokeninformation.model.Article;
import com.tokenbank.tokeninformation.net.HttpResult;
import com.tokenbank.tokeninformation.net.subscriber.BaseSubscriber;
import com.tokenbank.tokeninformation.util.LanguageUtil;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class RetrofitHelper extends BaseRetrofit {

    /**
     * 获取文章列表
     *
     * @return
     */
    public static Observable<List<Article>> getArticles() {
        return getService().getArticles(LanguageUtil.getHttpLang())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HttpResult<List<Article>>, List<Article>>() {
                    @Override
                    public List<Article> call(HttpResult<List<Article>> httpResult) {
                        if (httpResult.isSuccess()) {
                            return httpResult.getData();
                        }
                        return null;
                    }
                });

    }

    /**
     * 检查更新
     *
     * @return
     */
    public static Observable<UpgradeInfo> checkUpgrade() {
        Map<String, String> options = new HashMap<>();
        options.put("platform", "1");
        options.put("sys_ver", "7");
        options.put("software_version", "0.0.7");
        return getService().checkUpgrade(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HttpResult<UpgradeInfo>, UpgradeInfo>() {
                    @Override
                    public UpgradeInfo call(HttpResult<UpgradeInfo> upgradeInfoHttpResult) {
                        if (upgradeInfoHttpResult.isSuccess()) {
                            return upgradeInfoHttpResult.getData();
                        }
                        return null;
                    }
                });
    }

}
