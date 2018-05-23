package com.tokenbank.tokeninformation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tokenbank.tokeninformation.R;
import com.tokenbank.tokeninformation.util.upgrade.common.Constants;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeInfo;
import com.tokenbank.tokeninformation.util.upgrade.common.UpgradeUtils;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:下载进度的弹框
 */

public class UpgradeDialog extends Dialog {

    private TextView tvUpgradeTips;
    private TextView mTvUpgrade;
    private UpgradeInfo mUpgradeInfo;

    private OnClickListener mListener;

    public UpgradeDialog(@NonNull Context context, UpgradeInfo upgradeInfo) {
        super(context, R.style.BaseDialogStyle);
        this.mUpgradeInfo = upgradeInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_upgrade);
        initView();
    }

    @Override
    public void onBackPressed() {
        if (canCancel()) {
            super.onBackPressed();
        } else {
            //do nothing 屏蔽返回事件，强制升级
        }
    }

    private void initView() {
        tvUpgradeTips = findViewById(R.id.tv_upgrade_tips);
        mTvUpgrade = findViewById(R.id.tv_upgrade);
        mTvUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示下载弹框，并开始下载
                showDownloadProgressDialog(mUpgradeInfo);
            }
        });

        String upgradeTypeStr = "";
        if (mUpgradeInfo.getUpgradeMode() == Constants.UPGRADE_MODE_SUGGEST) {
            //推荐升级
            upgradeTypeStr = getContext().getString(R.string.recommended_upgrade);
        } else if (mUpgradeInfo.getUpgradeMode() == Constants.UPGRADE_MODE_FORCE) {
            //强制升级
            upgradeTypeStr = getContext().getString(R.string.strongly_recommended_upgrade);
        } else {
            //可不升级
            upgradeTypeStr = getContext().getString(R.string.can_be_upgraded_later);
        }
        //设置是否取消
        setCancelable(canCancel());
        String fileSize = UpgradeUtils.getFileSize(mUpgradeInfo.getSize());
        //升级语句提示
        String upgradeTips =
                getContext().getString(R.string.new_version_is_detected, fileSize, upgradeTypeStr);
        tvUpgradeTips.setText(upgradeTips);
    }

    private boolean canCancel() {
        //推荐升级
        return mUpgradeInfo.getUpgradeMode() != Constants.UPGRADE_MODE_FORCE;
    }

    /**
     * 显示下载进度的弹框
     */
    private void showDownloadProgressDialog(UpgradeInfo upgradeInfo) {
        this.dismiss();
        DownloadProgressDialog dialog = new DownloadProgressDialog(getContext(), upgradeInfo);
        dialog.show();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public interface OnClickListener {
        void onPositiveClick(Dialog dialog, View view);
    }
}
