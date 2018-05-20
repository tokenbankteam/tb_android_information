package com.tokenbank.tokeninformation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokenbank.tokeninformation.R;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeInfo;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeUtils;
import com.tokenbank.tokeninformation.util.upgrade.helper.DownloadTask;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:下载进度的弹框
 */
public class DownloadProgressDialog extends Dialog {

    private ProgressBar progressBar;
    private TextView tvProgress;
    private TextView tvInstall;

    private UpgradeInfo mUpgradeInfo;
    private OnProgressListener mListener;

    public DownloadProgressDialog(@NonNull Context context, UpgradeInfo upgradeInfo) {
        super(context, R.style.BaseDialogStyle);
        this.mUpgradeInfo = upgradeInfo;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_progress);
        initView();
        //开始下载
        doDownload();
    }

    private void initView() {
        //设置点击其它地方不让消失弹窗
        setCancelable(false);
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tv_progress);
        tvInstall = findViewById(R.id.tv_install);
        tvInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //安装app
                UpgradeUtils.startInstall(getContext(),
                        UpgradeUtils.getApkName(getContext(), mUpgradeInfo.getVersion()));
            }
        });
    }

    /**
     * 开始下载
     */
    private void doDownload() {
        DownloadTask downloadTask = new DownloadTask(getContext(), mUpgradeInfo, this);
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 刷新进度
     */
    public void updateProgress(int progress) {
        progressBar.setProgress(progress);
        String strProgress = String.valueOf(progress) + "%";
        tvProgress.setText(strProgress);
        if (mListener != null) {
            mListener.onProgress(this, progress);
        }
    }

    /**
     * 下载完成
     */
    public void downloadFinished() {
        //更新UI
        tvInstall.setVisibility(View.VISIBLE);
        //安装app
        UpgradeUtils.startInstall(getContext(),
            UpgradeUtils.getApkName(getContext(), mUpgradeInfo.getVersion()));
    }

    public void setProgressListener(OnProgressListener listener) {
        this.mListener = listener;
    }

    public interface OnProgressListener {
        void onProgress(Dialog dialog, int progress);
    }
}
