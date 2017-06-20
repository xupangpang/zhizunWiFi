package com.zhizun.zhizunwifi.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.Version;
import com.zhizun.zhizunwifi.dialog.AlertDialog;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.DeviceUtil;

import net.duohuo.dhroid.ioc.annotation.InjectView;

import java.io.File;
import java.text.DecimalFormat;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SettingActivityNew extends BaseActivity {
	@InjectView(id = R.id.headerLeft)
	ImageView headleft;
	@InjectView(id = R.id.headerTitle)
	TextView headerTitle;
	@InjectView(id = R.id.application_settings)
	RelativeLayout application_settings;
	@InjectView(id = R.id.evaluation)
	RelativeLayout evaluation;
	@InjectView(id = R.id.version_update)
	RelativeLayout version_update;
	@InjectView(id = R.id.about_us)
	RelativeLayout about_us;
	@InjectView(id = R.id.arrow_upgrade)
	TextView arrow_upgrade;
	private Context mContext = this;
	protected CompositeSubscription mCompositeSubscription;
	AlertDialog alertDialog;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder mBuilder;
	private final int  DOWNLOAD = 1000;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_new);

		initView();
	}

	private void initView() {
		headerTitle.setText("设置");
		headleft.setOnClickListener(onClickListener);
		application_settings.setOnClickListener(onClickListener);
		evaluation.setOnClickListener(onClickListener);
		version_update.setOnClickListener(onClickListener);
		about_us.setOnClickListener(onClickListener);
		mCompositeSubscription = new CompositeSubscription();

		mNotificationManager = (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(mContext);
		Intent intent = new Intent(mContext,MainTabActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(mContext,1,intent,0);
		mBuilder.setContentTitle("至尊WiFi正在准备更新...")//设置通知栏标题
				.setProgress(0,0,true)
				.setTicker("至尊WiFi正在更新")
				.setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_ALL)//默认通知声音和震动
				.setContentIntent(pIntent)
				.setFullScreenIntent(pIntent,true)
				.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON

		arrow_upgrade.setText("V"+DeviceUtil.getVersionName(this));
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			int id = view.getId();
			switch (id){
				case R.id.headerLeft:
					finish();
					break;
				case R.id.application_settings:
					Intent intent61 = new Intent(mContext,
							SettingActivityOld.class);
					startActivity(intent61);
					break;
				case R.id.evaluation:
					Uri uri = Uri.parse("market://details?id="+getPackageName());
					Intent intentpf = new Intent(Intent.ACTION_VIEW,uri);
					intentpf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intentpf);
					break;
				case R.id.version_update:
					checkVersion();
					break;
				case R.id.about_us:
					Intent intent6 = new Intent(mContext, AboutMeActivity.class);
					startActivity(intent6);
					break;
			}
		}
	};

	/**
	 * 连接服务器,检查版本信息.
	 */
	private void checkVersion() {
		baseShowProgressDialog("检查更新...",false);
		HttpService apiService =  HttpManager.getService();
		Subscription subscription = apiService.Version("至尊免费WiFi", String.valueOf(DeviceUtil.getVersionCode(mContext)),"zhizunwifi").subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<Version>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								basehideProgressDialog();
								//Log.e("updateError------>",e.getMessage());
								Toast.makeText(mContext,"获取版本信息失败",Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onNext(BaseResultEntity<Version> baseResultEntity) {
								basehideProgressDialog();
								if (baseResultEntity.ret == 200){
									//Log.e("update--->",baseResultEntity.data.app_version+"     "+baseResultEntity.data.app_url);
									if (Integer.parseInt(baseResultEntity.data.app_version) > DeviceUtil.getVersionCode(mContext)) {
										showUpdateDialog(baseResultEntity.data.desc, baseResultEntity.data.app_name, baseResultEntity.data.app_url, baseResultEntity.data.update);
									}else {
										Toast.makeText(mContext,"当前已是最新版本",Toast.LENGTH_SHORT).show();
									}
								}else {
									//Log.e("update---->","获取更新信息失败");
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
	/**
	 * 显示升级提醒的对话框
	 *
	 */
	private void showUpdateDialog(final String updataMsg,final String apkname,final String downloadurl,final String isMandatory) {
		if (alertDialog != null) {
			alertDialog.dismiss();
			alertDialog = null;
		}
		alertDialog = new AlertDialog(mContext, null).builder()
				.setCancelable(true)
				.setTitle("版本更新")
				.setMsg(Html.fromHtml(updataMsg).toString())
				.setNegativeButton("下次再说", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isMandatory.equals("2")){
							alertDialog.dismiss();
						}else {
							alertDialog.dismiss();
						}
					}
				})
				.setPositiveButton("马上更新", new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mNotificationManager.notify(DOWNLOAD, mBuilder.build());
						Toast.makeText(mContext,"至尊WiFi正在更新中，更新进度请查看通知栏！",Toast.LENGTH_SHORT).show();
						// 下载新版本的apk , 替换安装
						HttpUtils httputils = new HttpUtils();
						final File file = new File(Environment
								.getExternalStorageDirectory(), apkname);
						httputils.download(downloadurl, file.getAbsolutePath(),
								false, new RequestCallBack<File>() {
									@Override
									public void onSuccess(ResponseInfo<File> arg0) {
										// 替换安装apk
										Intent intent = new Intent();
										intent.setAction("android.intent.action.VIEW");
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.addCategory("android.intent.category.DEFAULT");
										intent.setDataAndType(Uri.fromFile(file),
												"application/vnd.android.package-archive");
										startActivity(intent);

										mNotificationManager.cancel(DOWNLOAD);
									}

									@Override
									public void onFailure(HttpException arg0,
														  String arg1) {
									}

									@Override
									public void onLoading(long total, long current, boolean isUploading) {
										super.onLoading(total, current, isUploading);
										double x_double = current * 1.0;
										double tempresult = x_double / total;
										DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
										// 百分比格式，后面不足2位的用0补齐
										String result = df1.format(tempresult);

										NotificationCompat.Builder mBuilder02 = new NotificationCompat.Builder(mContext);
										mBuilder02.setContentTitle("至尊WiFi正在下载中...")//设置通知栏标题
												.setProgress(100,(int)(Float.parseFloat(result) * 100),false)
												.setTicker("至尊WiFi正在更新")
												.setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
												.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
												.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
										mNotificationManager.notify(DOWNLOAD,mBuilder02.build());
									}
								});
					}
				});
		alertDialog.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mCompositeSubscription != null){
			mCompositeSubscription.unsubscribe();
		}
	}

}
