package com.tokenbank.tokeninformation.util.upgrade.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tokenbank.tokeninformation.dialog.DownloadProgressDialog;
import com.tokenbank.tokeninformation.util.upgrade.common.Constants;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeInfo;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeUtils;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */
public class DownloadTask extends AsyncTask<String, Integer, Boolean> {
    private Context mContext = null;
    private String mApkName = null;
    private UpgradeInfo mUpgradeInfo = null;
    private DownloadProgressDialog mDialog;

    public DownloadTask(Context context, UpgradeInfo upgradeInfo, DownloadProgressDialog dialog) {
        mContext = context;
        mUpgradeInfo = upgradeInfo;
        mDialog = dialog;
        mApkName = UpgradeUtils.getApkName(context, mUpgradeInfo.getVersion());
    }

    @Override protected Boolean doInBackground(String... args) {
        String downloadPath = UpgradeUtils.getDownloadPath(mContext);
        //检查是否已经下载
        if (UpgradeUtils.isNewestApkDownloaded(downloadPath, mUpgradeInfo.getHashCode())) {
            // 已经下载了直接安装
            publishProgress(100);
            return true;
        }
        //清理已下载过的apk
        UpgradeUtils.clearDownloadApk(downloadPath);
        //执行下载
        if (doDownload(mContext, mUpgradeInfo)) {
            return true;
        }else{
            // download fail
            UpgradeUtils.clearDownloadApk(downloadPath);
            return false;
        }
    }

    @Override protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d("DownloadTask", String.valueOf(values[0]));
        //刷新UI
        mDialog.updateProgress(values[0]);
    }

    @Override protected void onPostExecute(Boolean needUpgrade) {
        super.onPostExecute(needUpgrade);
        if (needUpgrade) {
            //when download finished
            mDialog.downloadFinished();
        }
    }

    private boolean doDownload(Context context, UpgradeInfo upgradeInfo) {
        if (context == null) {
            Log.e(Constants.TAG, "DownloadTask: doDownload " + "packageName or context is null");
            return false;
        }

        DownloadHelper mDownloadHelper = new DownloadHelper(context);
        if (upgradeInfo == null) {
            Log.e(Constants.TAG, "DownloadTask: failed to get upgrade info, ");
            return false;
        }
        mDownloadHelper.downloadAPK(upgradeInfo.getUrl(), mApkName);
        while (mDownloadHelper.getDownloadPercentage() < Constants.PROGRESS_FINISH) {
            try {
                Thread.sleep(Constants.PROCESS_UPDATE_DURATION);
            } catch (InterruptedException e) {
                Log.w(Constants.TAG, "DownloadAndInstallTask:install task cancel ");
            }
            publishProgress(mDownloadHelper.getDownloadPercentage());
        }
        return true;
    }
}
