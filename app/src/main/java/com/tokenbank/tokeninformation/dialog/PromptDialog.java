package com.tokenbank.tokeninformation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokenbank.tokeninformation.R;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class PromptDialog extends Dialog {

    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvPositive;

    private Builder mBuilder;

    private PromptDialog(Builder builder) {
        super(builder.context, R.style.BaseDialogStyle);
        this.mBuilder = builder;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prompt);
        initView();
    }

    private void initView() {
        ivIcon = findViewById(R.id.iv_icon);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvPositive = findViewById(R.id.tv_positive);
        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuilder.listener != null) {
                    mBuilder.listener.onPositiveClick(PromptDialog.this, tvPositive);
                }
            }
        });
        //设置点击其它地方不让消失弹窗
        setCancelable(false);
        setTitle(mBuilder.title);
        setContent(mBuilder.content);
        setDrawable(mBuilder.drawableId);
        setPositiveText(mBuilder.positiveText);
    }

    /**
     * 制定弹框的宽高
     *
     * @return LayoutParams
     */
    private WindowManager.LayoutParams getLayoutParams() {
        //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return lp;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setDrawable(@DrawableRes int drawableId) {
        ivIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
    }

    public void setPositiveText(String positiveText) {
        tvPositive.setText(positiveText);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String content;
        private @DrawableRes
        int drawableId;
        private String positiveText;
        private OnClickListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder drawable(@DrawableRes int drawableId) {
            this.drawableId = drawableId;
            return this;
        }

        public Builder positiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder positiveListener(OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        @UiThread
        public PromptDialog show() {
            PromptDialog dialog = new PromptDialog(this);
            dialog.show();
            return dialog;
        }

        public interface OnClickListener {
            void onPositiveClick(Dialog dialog, View view);
        }
    }
}
