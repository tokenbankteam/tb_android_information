package com.tokenbank.tokeninformation.util.upgrade;

import android.app.Activity;
import android.util.Log;
import com.tokenbank.tokeninformation.dialog.UpgradeDialog;
import com.tokenbank.tokeninformation.net.manager.RetrofitHelper;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeInfo;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *     author : Clement
 *     time   : 2018/05/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UpgradeManager {

    private static UpgradeManager sInstance;
    private UpgradeInfo mUpgradeInfo;
    private boolean isUpgrading = false;

    private UpgradeManager() {

    }

    public static UpgradeManager getInstance() {
        if (sInstance == null) {
            synchronized (UpgradeManager.class) {
                if (sInstance == null) {
                    sInstance = new UpgradeManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化数据，检查更新
     */
    public void init() {
        RetrofitHelper.checkUpgrade().subscribe(new Consumer<UpgradeInfo>() {
            @Override public void accept(UpgradeInfo upgradeInfo) throws Exception {
                mUpgradeInfo = upgradeInfo;
            }
        }, new Consumer<Throwable>() {
            @Override public void accept(Throwable throwable) throws Exception {
                Log.d("UpgradeManager", throwable.getMessage());
            }
        });
    }

    /**
     * 检查更新，如果需要更新，则显示弹框
     */
    public void checkUpgrade(Activity activity) {
        if (mUpgradeInfo != null && !isUpgrading) {
            //需要升级
            UpgradeDialog dialog = new UpgradeDialog(activity, mUpgradeInfo);
            dialog.show();
            isUpgrading = true;
        }
    }
}
