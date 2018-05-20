package com.tokenbank.tokeninformation;

import android.app.Application;

import com.tokenbank.tokeninformation.util.upgrade.UpgradeManager;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class TokenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //检查app版本更新
        UpgradeManager.getInstance().init();
    }
}
