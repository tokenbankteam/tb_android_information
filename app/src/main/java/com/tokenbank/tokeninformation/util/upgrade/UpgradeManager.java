package com.tokenbank.tokeninformation.util.upgrade;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tokenbank.tokeninformation.dialog.UpgradeDialog;
import com.tokenbank.tokeninformation.net.manager.RetrofitHelper;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeInfo;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeUtils;

import android.util.Log;
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
                if (TextUtils.isEmpty(upgradeInfo.getUrl())) {
                    return;
                }
                //插入apkName
                mUpgradeInfo.setApkName(upgradeInfo.getUrl().substring(upgradeInfo.getUrl().lastIndexOf("/") + 1));
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


    /**
     * 执行安装
     *
     * @param context
     */
    public void startInstall(Context context) {
        UpgradeUtils.startInstall(context, mUpgradeInfo.getApkName());
    }
}
