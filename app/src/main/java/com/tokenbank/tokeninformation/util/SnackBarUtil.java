package com.tokenbank.tokeninformation.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class SnackBarUtil {

    private static final int DURATION = Snackbar.LENGTH_SHORT;

    /**
     * 显示snackBar
     *
     * @param view
     * @param content
     */
    public static void show(@NonNull View view, String content) {
        Snackbar.make(view, content, DURATION).show();
    }

    public static void show(@NonNull View view, int resId) {
        Snackbar.make(view, resId, DURATION).show();
    }
}
