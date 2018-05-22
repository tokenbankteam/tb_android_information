package com.tokenbank.tokeninformation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokenbank.tokeninformation.model.Article;
import com.tokenbank.tokeninformation.net.manager.RetrofitHelper;
import com.tokenbank.tokeninformation.util.upgrade.UpgradeManager;
import com.tokenbank.tokeninformation.util.upgrade.common.Constants;

import java.util.List;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout slRefresh;
    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getArticlesFromNet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpgradeManager.getInstance().checkUpgrade(this);
    }

    private void initView() {
        slRefresh = findViewById(R.id.sl_refresh);
        //设置下拉刷新动画的颜色
        slRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        slRefresh.setRefreshing(true);
        slRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArticlesFromNet();
            }
        });

        RecyclerView rvArticle = findViewById(R.id.rv_article);
        rvArticle.setLayoutManager(new LinearLayoutManager(this));
        rvArticle.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ArticleAdapter();
        rvArticle.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Article article = mAdapter.getData().get(position);
                WebBrowserActivity.startWebBrowserActivity(MainActivity.this,
                        article.getTitle(), article.getUrl());
            }
        });
    }

    private void getArticlesFromNet() {
        RetrofitHelper.getArticles().subscribe(new Action1<List<Article>>() {
            @Override
            public void call(List<Article> articles) {
                mAdapter.setData(articles);
                mAdapter.notifyDataSetChanged();
                slRefresh.setRefreshing(false);
            }
        });
    }

    class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
        private List<Article> mArticles;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ArticleAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_article, parent, false);
            return new ArticleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArticleAdapter.ArticleViewHolder holder, int position) {
            Article article = mArticles.get(position);
            holder.tvTitle.setText(article.getTitle());
            String createTime = article.getCreateTime();
            if (!TextUtils.isEmpty(createTime) && createTime.length() >= 10) {
                createTime = createTime.substring(0, 10);
            }
            holder.tvTime.setText(createTime);
            holder.tvAuthor.setText(article.getAuthor());
            Glide.with(MainActivity.this).load(article.getImageUrl()).into(holder.ivLogo);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(holder.getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mArticles != null) {
                return mArticles.size();
            }
            return 0;
        }

        public void setData(List<Article> articles) {
            this.mArticles = articles;
        }

        public List<Article> getData() {
            return mArticles;
        }

        class ArticleViewHolder extends RecyclerView.ViewHolder {

            TextView tvTitle;
            TextView tvTime;
            TextView tvAuthor;
            ImageView ivLogo;

            public ArticleViewHolder(final View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvTime = itemView.findViewById(R.id.tv_time);
                tvAuthor = itemView.findViewById(R.id.tv_author);
                ivLogo = itemView.findViewById(R.id.iv_logo);
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mListener = listener;
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.UNKNOWN_APP_INSTALL_REQUEST_CODE) {
            //再次执行安装流程，包含权限判等
            UpgradeManager.getInstance().startInstall(MainActivity.this);
        }
    }
}
