package com.tokenbank.tokeninformation.net.manager;

import com.tokenbank.tokeninformation.model.Article;
import com.tokenbank.tokeninformation.net.HttpResult;
import com.tokenbank.tokeninformation.net.RxSchedulersHelper;
import com.tokenbank.tokeninformation.util.LanguageUtil;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeInfo;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class RetrofitHelper extends BaseRetrofit {

    /**
     * 获取文章列表
     */
    public static Observable<List<Article>> getArticles() {
        return getService().getArticles(LanguageUtil.getHttpLang())
            .compose(RxSchedulersHelper.<HttpResult<List<Article>>>schedulerNewThreadToMain())
            .map(new Function<HttpResult<List<Article>>, List<Article>>() {
                @Override public List<Article> apply(HttpResult<List<Article>> listHttpResult)
                    throws Exception {
                    if (listHttpResult.isSuccess()) {
                        return listHttpResult.getData();
                    }
                    return null;
                }
            });
    }

    /**
     * 检查更新
     */
    public static Observable<UpgradeInfo> checkUpgrade() {
        Map<String, String> options = new HashMap<>();
        options.put("platform", "1");
        options.put("sys_ver", "7");
        options.put("software_version", "0.0.7");
        return getService().checkUpgrade(options)
            .compose(RxSchedulersHelper.<HttpResult<UpgradeInfo>>schedulerNewThreadToMain())
            .map(new Function<HttpResult<UpgradeInfo>, UpgradeInfo>() {
                @Override public UpgradeInfo apply(HttpResult<UpgradeInfo> upgradeInfoHttpResult)
                    throws Exception {
                    if (upgradeInfoHttpResult.isSuccess()) {
                        return upgradeInfoHttpResult.getData();
                    }
                    return null;
                }
            });
    }
}
