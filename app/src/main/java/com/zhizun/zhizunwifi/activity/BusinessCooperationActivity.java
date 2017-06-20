package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.adapter.BussinessCooperationAdapter;
import com.zhizun.zhizunwifi.bean.BussCooper;
import com.zhizun.zhizunwifi.bean.WiFiDetailEntity;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.qiujuer.genius.ui.widget.Button;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * @author xupp
 * @date 2017/5/24
 */

public class BusinessCooperationActivity extends BaseActivity {
    @InjectView(id = R.id.no_businss_layout)
    RelativeLayout no_businss_layout;
    @InjectView(id = R.id.yes_businss_layout)
    LinearLayout yes_businss_layout;
    @InjectView(id = R.id.RecyclerView_business)
    PullLoadMoreRecyclerView RecyclerView_business;
    @InjectView(id = R.id.agreement_cb)
    CheckBox agreement_cb;
    @InjectView(id = R.id.agreement_message)
    TextView agreement_message;
    @InjectView(id = R.id.business_add_wifi)
    Button business_add_wifi;
    @InjectView(id = R.id.headerTitle)
    TextView headerTitle;
    @InjectView(id = R.id.headerLeft)
    ImageView headerLeft;
    @InjectView(id = R.id.headerRightTextView)
    TextView headerRightTextView;
    protected CompositeSubscription mCompositeSubscription;
    private Context mContext = this;
    private BussinessCooperationAdapter bAdaptet;
    private List<BussCooper> bussCooperList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_cooperation_main);
        initView();
    }

    private void initView(){
        headerTitle.setText("商家合作");
        headerRightTextView.setVisibility(View.VISIBLE);
        headerRightTextView.setText("合作说明");
        headerRightTextView.setOnClickListener(onClickListener);
        agreement_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    business_add_wifi.setEnabled(true);
                    business_add_wifi.setBackgroundResource(R.color.blue);
                }else {
                    business_add_wifi.setEnabled(false);
                    business_add_wifi.setBackgroundResource(R.color.paysafe_plugin_user_input_hint_color);
                }
            }
        });
        agreement_message.setOnClickListener(onClickListener);
        headerLeft.setOnClickListener(onClickListener);
        mCompositeSubscription = new CompositeSubscription();
        RecyclerView_business.setLinearLayout();
        bAdaptet = new BussinessCooperationAdapter(mContext);
        RecyclerView_business.setAdapter(bAdaptet);
        bAdaptet.setItemClickListener(new BussinessCooperationAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, WiFiDetailsActivity.class);
                if (bussCooperList.get(position).getStatus().equals("审核中")){
                    intent.putExtra("audit","0");
                }else if (bussCooperList.get(position).getStatus().equals("审核失败")){
                    intent.putExtra("nothrough","0");
                }else {
                    intent.putExtra("WiFiDetail", new WiFiDetailEntity(bussCooperList.get(position).getSsid(),
                            0,false,BaseUtils.getSharedPreferences("address",context),0,0,"","",false));
                    intent.putExtra("storemsg", bussCooperList.get(position).getBusiness());
                    intent.putExtra("nobutton","true");
                }
                mContext.startActivity(intent);
            }
        });
        RecyclerView_business.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        business_add_wifi.setOnClickListener(onClickListener);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           int id = view.getId();
            switch (id){
                case R.id.agreement_message:
                    //商家合作协议
                    Intent intent0 = new Intent(mContext,WebViewActivity.class);
                    intent0.putExtra("Url","file:///android_asset/合作协议.html");
                    startActivity(intent0);
                    break;
                case R.id.business_add_wifi:
                    //新增wifi信息
                    finish();
                    startActivity(new Intent(mContext,AddWiFiMessageActivity.class));

                    break;
                case R.id.headerLeft:
                    finish();
                    break;
                case R.id.headerRightTextView:
                    //合作说明
                    Intent intent1 = new Intent(mContext,WebViewActivity.class);
                    intent1.putExtra("Url","file:///android_asset/合作说明.html");
                    startActivity(intent1);
                    break;

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        RecyclerView_business.setRefreshing(true);
        HttpService apiService =  HttpManager.getService();
        Subscription subscription = apiService.CheckUserWifiStatus(BaseUtils.getSharedPreferences("userName",mContext)).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<BaseResultEntity<List<BussCooper>>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                RecyclerView_business.setPullLoadMoreCompleted();
                            }

                            @Override
                            public void onNext(BaseResultEntity<List<BussCooper>> baseResultEntity) {
                                RecyclerView_business.setPullLoadMoreCompleted();
                                if (baseResultEntity.ret == 200){
                                    if (baseResultEntity.data.size() > 0){
                                        bussCooperList = baseResultEntity.data;
                                        no_businss_layout.setVisibility(View.GONE);
                                        yes_businss_layout.setVisibility(View.VISIBLE);
                                        bAdaptet.setList(bussCooperList);
                                        bAdaptet.notifyDataSetChanged();
                                    }else {
                                        no_businss_layout.setVisibility(View.VISIBLE);
                                        yes_businss_layout.setVisibility(View.GONE);
                                    }

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
}
