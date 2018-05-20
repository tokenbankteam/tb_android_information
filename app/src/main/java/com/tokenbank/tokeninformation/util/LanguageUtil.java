package com.tokenbank.tokeninformation.util;

import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class LanguageUtil {

    /**
     * 根据系统语言获取用于http接口的lang
     *
     * @return cn
     */
    public static String getHttpLang() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }

        //TODO 目前仅支持简体和英文
        String lang = locale.getLanguage();
        if ("en".equalsIgnoreCase(lang)) {
            return "en";
        } else if ("zh".equalsIgnoreCase(lang)) {
            //目前暂不支持繁体
            return "zh-Hans";
        } else {
            return "en";
        }
    }

}
