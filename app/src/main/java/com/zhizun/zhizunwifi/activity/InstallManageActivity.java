package com.zhizun.zhizunwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.adapter.InstallMangerAdapter;
import com.zhizun.zhizunwifi.bean.InstallManger;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.DownLoaderManger;
import com.zhizun.zhizunwifi.utils.NoAlphaItemAnimator;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author xupp
 * @date 2017/5/8
 */

public class InstallManageActivity extends BaseActivity {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private InstallMangerAdapter mInstallMangerAdapter;
    private List<InstallManger> Mlist = new ArrayList<>();
    protected CompositeSubscription mCompositeSubscription;
    private TextView headerTitle;
    private ImageView headerLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_manage_main);

        initView();
        loadData();
    }

    private void initView(){
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setItemAnimator(new NoAlphaItemAnimator());
        mInstallMangerAdapter = new InstallMangerAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mInstallMangerAdapter);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });

        headerLeft = (ImageView)findViewById(R.id.headerLeft);
        headerLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        headerTitle = (TextView)findViewById(R.id.headerTitle);
        headerTitle.setText("安装包管理");
        mCompositeSubscription = new CompositeSubscription();

    }

    private void loadData() {
        mPullLoadMoreRecyclerView.setRefreshing(true);
        HttpService apiService =  HttpManager.getService();
        Subscription subscription = apiService.Root().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<BaseResultEntity<List<InstallManger>>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                            }

                            @Override
                            public void onNext(BaseResultEntity<List<InstallManger>> baseResultEntity) {
                                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                if (baseResultEntity.ret == 200){
                                    Mlist = baseResultEntity.data;
                                    DecimalFormat decimalFormat =new DecimalFormat("0.00");
                                    for (InstallManger installManger : Mlist) {
                                        File file = new File(DownLoaderManger.FILE_PATH,installManger.getAPPname()+".apk");
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                        installManger.setAPPstag(0);
                                        float speedSize = Integer.parseInt(installManger.getAPPsize()) / 1024;
                                        String speedValue = speedSize < 1024 ? decimalFormat.format(speedSize) +" KB" : decimalFormat.format((speedSize / 1024)) +" MB";
                                        installManger.setAPPsize(speedValue);
                                    }
                                    mInstallMangerAdapter.setList(Mlist);
                                    mInstallMangerAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }
                        }
                );
        mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null){
            mCompositeSubscription.unsubscribe();
        }
        if (mInstallMangerAdapter.getDownLoader() != null){
            for (InstallManger installManger : Mlist) {
                mInstallMangerAdapter.getDownLoader().stopAll(installManger.getAPPurl());
            }
        }
    }
}

