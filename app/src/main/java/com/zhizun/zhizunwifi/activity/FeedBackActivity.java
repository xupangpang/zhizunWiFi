package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.qiujuer.genius.ui.widget.Button;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class FeedBackActivity extends BaseActivity {
    @InjectView(id = R.id.headerLeft)
    ImageView headleft;
    @InjectView(id = R.id.headerTitle)
    TextView headerTitle;
    @InjectView(id = R.id.feedback_edittext)
    EditText feedback_edittext;
    @InjectView(id = R.id.feedback_submit)
    Button feedback_submit;
    @InjectView(id = R.id.feedback_why)
    LinearLayout feedback_why;
    @InjectView(id = R.id.feedback_cancel)
    LinearLayout feedback_cancel;
    private Context mContext = this;
    protected CompositeSubscription mCompositeSubscription;
    private HttpService apiService;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_feedback);
		initView();

        apiService =  HttpManager.getService();
        mCompositeSubscription = new CompositeSubscription();

	}

	private void initView() {
        feedback_submit.setOnClickListener(onClickListener);
        feedback_why.setOnClickListener(onClickListener);
        feedback_cancel.setOnClickListener(onClickListener);
        headerTitle.setText("帮助与反馈");
        headleft.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.headerLeft:
                    finish();
                    break;
                case R.id.feedback_submit:
                    if(TextUtils.isEmpty(feedback_edittext.getText())){
                        ShowToast("请先填写意见反馈");
                    }else {
                        submitFeedback(feedback_edittext.getText().toString());
                    }
                    break;
                case R.id.feedback_why:
                    //商家合作
                    Intent intent0 = new Intent(mContext,WebViewActivity.class);
                    intent0.putExtra("Url","file:///android_asset/为什么进行合作.html");
                    startActivity(intent0);
                    break;
                case R.id.feedback_cancel:
                    //取消分享
                    Intent intent1 = new Intent(mContext,WebViewActivity.class);
                    intent1.putExtra("Url","file:///android_asset/取消热点分享.html");
                    startActivity(intent1);
                    break;

            }

        }
    };

    /**
     * 修改资料 updateInfo
     *
     * @Title: updateInfo
     * @return void
     * @throws
     */
    private void submitFeedback(String feedbackText) {
        baseShowProgressDialog("正在提交...",false);
        Subscription subscription = apiService.UserFeedback(feedbackText).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<BaseResultEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                basehideProgressDialog();
                                ShowToast("反馈信息提交失败");
                            }

                            @Override
                            public void onNext(BaseResultEntity baseResultEntity) {
                                basehideProgressDialog();
                                if (baseResultEntity.ret == 200){
                                    ShowToast("反馈信息提交成功");
                                    finish();
                                }else {
                                    ShowToast("反馈信息提交失败");
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
    }
}
