package com.zhizun.zhizunwifi.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.PutAccount;
import com.zhizun.zhizunwifi.bean.UserHead;
import com.zhizun.zhizunwifi.dialog.LoadingDialog;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.Constants;
import com.zhizun.zhizunwifi.utils.PhotoUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 个人资料页面
 * 
 * @ClassName: SetMyInfoActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-10 下午2:55:19
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint({ "SimpleDateFormat", "ClickableViewAccessibility",
		"InflateParams" })
public class SetMyInfoActivity extends BaseActivity implements OnClickListener {

	private TextView tv_set_name, tv_set_nick, tv_set_gender, tv_nickarraw,
			headerTitle, tv_genderArraw;
	private ImageView iv_set_avator, iv_arraw, headerLeft;

	private RelativeLayout layout_head, layout_nick, layout_all,ll_nickarraw,ll_genderArraw;
	private Button layout_black_tips;
	private LoadingDialog loadingDialog;
	String username = "";
	private String path;
	private String[] sexs = new String[] { "男", "女" };

	private boolean updateIconFlag = true;
	protected CompositeSubscription mCompositeSubscription;
	private HttpService apiService;
	private Uri mImageUri;
	private String mImagePath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*// 因为魅族手机下面有三个虚拟的导航按钮，需要将其隐藏掉，不然会遮掉拍照和相册两个按钮，且在setContentView之前调用才能生效
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 14) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}*/
		setContentView(R.layout.activity_set_info);
		apiService =  HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();
		username = BaseUtils.getSharedPreferences("userName", this);
		initView();


	}

	private void initView() {
		layout_all = (RelativeLayout)findViewById(R.id.layout_all);
		iv_set_avator = (ImageView) findViewById(R.id.iv_set_avator);
		iv_arraw = (ImageView) findViewById(R.id.iv_arraw);
		tv_nickarraw = (TextView) findViewById(R.id.tv_nickarraw);
		tv_set_name = (TextView) findViewById(R.id.tv_set_name);
		tv_set_nick = (TextView) findViewById(R.id.tv_set_nick);
		tv_genderArraw = (TextView) findViewById(R.id.tv_genderArraw);
		layout_head = (RelativeLayout) findViewById(R.id.layout_head);
		headerLeft = (ImageView) findViewById(R.id.headerLeft);
		headerTitle = (TextView) findViewById(R.id.headerTitle);
		ll_nickarraw = (RelativeLayout) findViewById(R.id.ll_nickarraw);
		ll_genderArraw = (RelativeLayout) findViewById(R.id.ll_genderArraw);
		// 黑名单提示语
		layout_black_tips = (Button) findViewById(R.id.layout_black_tips);
		tv_set_gender = (TextView) findViewById(R.id.tv_set_gender);
		layout_head.setOnClickListener(this);
		ll_nickarraw.setOnClickListener(this);
		ll_genderArraw.setOnClickListener(this);
		headerLeft.setOnClickListener(this);
		layout_black_tips.setOnClickListener(this);

		headerTitle.setText("个人账户资料");

	}

	// private void initMeData() {
	// MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
	// initOtherData(user.getUsername());
	// }

	// private void initOtherData(String name) {
	// BmobQuery<MyUser> query = new BmobQuery<MyUser>();
	// query.addWhereEqualTo("username", name);
	// query.findObjects(this, new FindListener<MyUser>() {
	// @Override
	// public void onSuccess(List<MyUser> arg0) {
	// // TODO Auto-generated method stub
	// // toast("查询用户成功："+object.size());
	// if (arg0 != null && arg0.size() > 0) {
	// user = (MyUser) arg0.get(0);
	// updateUser(user);
	// } else {
	// // ShowLog("onSuccess 查无此人");
	// }
	// }
	//
	// @Override
	// public void onError(int code, String msg) {
	// // TODO Auto-generated method stub
	// // toast("查询用户失败："+msg);
	// }
	// });
	// }

	/**
	 * 更新UI
	 */
	private void updateUser() {
		// 更改
		String userName =  BaseUtils.getSharedPreferences("userName", this);
		String nickName = BaseUtils.getSharedPreferences("nickName", this);
		String sex = BaseUtils.getSharedPreferences("sex", this);
		String iconPath = BaseUtils.getSharedPreferences("userIcon",this);
		String userIconlocal = BaseUtils.getSharedPreferences("userIconlocal", this);
		String userIcon = BaseUtils.getSharedPreferences("userIcon", this);

			if(!TextUtils.isEmpty(userName) && userName.length() > 6 ){
				StringBuilder sb  =new StringBuilder();
				for (int i = 0; i < userName.length(); i++) {
					char c = userName.charAt(i);
					if (i >= 3 && i <= 6) {
						sb.append('*');
					} else {
						sb.append(c);
					}
				}

				tv_set_name.setText(sb.toString());
			}




		if (!TextUtils.isEmpty(nickName))
		tv_set_nick.setText(nickName);
		if (!TextUtils.isEmpty(sex)){
			if (sex.equals("1"))
				tv_set_gender.setText("男");
			else
				tv_set_gender.setText("女");
		}
		Glide.with(this).load(userIcon).error(R.drawable.icon_default_portal).into(iv_set_avator);

	}

	@Override
	public void onResume() {
		updateUser();
		super.onResume();
		// initMeData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {// 更换头像
		case R.id.layout_head:
			showAvatarPop();
			break;
		case R.id.ll_nickarraw:
			Intent it = new Intent(SetMyInfoActivity.this,
					UpdateInfoActivity.class);
			startActivity(it);
			// addBlog();
			break;
		case R.id.ll_genderArraw:// 性别
			showsexPop();
			break;
		case R.id.headerLeft:// 返回
			finish();
			break;
		case R.id.layout_black_tips:// 退出登录
			BaseUtils.setSharedPreferences("nickName", "", SetMyInfoActivity.this);
			BaseUtils.setSharedPreferences("userName", "", SetMyInfoActivity.this);
			BaseUtils.setSharedPreferences("sex", "", SetMyInfoActivity.this);
			BaseUtils.setSharedPreferences("sign", "", SetMyInfoActivity.this);
			BaseUtils.setSharedPreferences("money", "", SetMyInfoActivity.this);
			BaseUtils.setSharedPreferences("userIcon", "", SetMyInfoActivity.this);
			finish();
			break;
		}
	}

	private void showSexChooseDialog() {
		new AlertDialog.Builder(this,R.style.ActionSheetDialogStyle)
				.setTitle("性别")
				.setSingleChoiceItems(sexs, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// 去更新
								if (username.length() == 11)
									updateInfo(username,null,which+1);
								else
									updateInfo(null,username,which+1);


								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();
	}

	/**
	 * 修改资料 updateInfo
	 * 
	 * @Title: updateInfo
	 * @return void
	 * @throws
	 */
	private void updateInfo(String username,String oid,final int which) {

			baseShowProgressDialog("正在提交...",false);
			Subscription subscription = apiService.UploadUserInfo(username,oid,"",String.valueOf(which)).subscribeOn(Schedulers.io())
					.unsubscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(
							new Subscriber<BaseResultEntity<PutAccount>>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									basehideProgressDialog();
									ShowToast("性别修改失败，请重试！");
								}

								@Override
								public void onNext(BaseResultEntity<PutAccount> baseResultEntity) {
									basehideProgressDialog();
									if (!baseResultEntity.data.result.equals("success")){
										ShowToast("性别修改失败，请重试！");
									}else {
										ShowToast("性别修改成功");
										BaseUtils.setSharedPreferences("sex",String.valueOf(which), SetMyInfoActivity.this);
										updateUser();
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
	PopupWindow sexPop;
	private void showsexPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_showsex,
				null);
		// 从相册取
		RelativeLayout layout_woman = (RelativeLayout) view.findViewById(R.id.layout_woman);
		// 取消
		RelativeLayout cancel = (RelativeLayout) view.findViewById(R.id.cancel);
		// 拍照
		final RelativeLayout layout_man= (RelativeLayout) view.findViewById(R.id.layout_man);
		 layout_man.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_man.setBackgroundColor(getResources().getColor(
						R.color.white));
				layout_man.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));

				if (username.length() == 11)
					updateInfo(username,null,1);
				else
					updateInfo(null,username,1);

				if (sexPop.isShowing()) {
					sexPop.dismiss();
				}
			}
		});
		layout_woman.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));

				if (username.length() == 11)
					updateInfo(username,null,0);
				else
					updateInfo(null,username,0);


				if (sexPop.isShowing()) {
					sexPop.dismiss();
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sexPop.dismiss();
			}
		});

		sexPop = new PopupWindow(view, mScreenWidth, 600);
		sexPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					if (sexPop.isShowing()) {
						sexPop.dismiss();
					}
					return true;
				}
				return false;
			}
		});

		sexPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		sexPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		sexPop.setFocusable(true);
		sexPop.setOutsideTouchable(true);
		sexPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		// avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		sexPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}


		/*String user_id = BaseUtils.getSharedPreferences("id",
				SetMyInfoActivity.this);
		System.out.println(user_id);
		if (user_id != null) {

			// 设置传递的参数
			HashMap<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("user_id", user_id);
			paramsMap.put("sex", which);

			HttpConnections gender = new HttpConnections() {

				@Override
				public void requestSuccess(String json) {
					// 修改成功
					// 解析json
					JSONObject obj;
					try {
						obj = new JSONObject(json);
						String result = obj.getString("result");
						if ("ok".equals(result)) {
							BaseUtils.setSharedPreferences("sex", sexs[which],
									SetMyInfoActivity.this);
							ShowToast("性别成功修改为：" + sexs[which]);
							//updateUser();
							if (loadingDialog.isShowing()) {
								loadingDialog.cancel();
							}
						} else {
							if (loadingDialog.isShowing()) {
								loadingDialog.cancel();
							}
							ShowToast("性别修改失败，请稍后尝试！");
							System.out.println(json);
						}

					} catch (JSONException e) {
						ShowToast("性别修改失败，请稍后尝试！");
						e.printStackTrace();
					}
				}

				@Override
				public void requestFailure(HttpException arg0, String arg1) {
					// 修改失败
					if (loadingDialog.isShowing()) {
						loadingDialog.cancel();
					}
					ShowToast("昵称修改失败，请稍后尝试！");
					System.out.println(arg1);

				}
			};

			loadingDialog = new LoadingDialog(this, true);
			loadingDialog.show();

			gender.DoPostRequest(Constants.ALTER_GENDER_URL, paramsMap);
		}*/



	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	RelativeLayout cancel;
	PopupWindow avatorPop;

	public String filePath = "";

	@SuppressWarnings("deprecation")
	private void showAvatarPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_showavator,
				null);
		// 从相册取
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		// 取消
		cancel = (RelativeLayout) view.findViewById(R.id.cancel);
		// 拍照
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));

				// 头像保存目录
				File dir = new File(Constants.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// 获取相片的保存路径
				mImageUri = Uri.fromFile(file);

				// 打开系统相机
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
				startActivityForResult(intent,
						Constants.REQUESTCODE_UPLOADAVATAR_CAMERA);


				if (avatorPop.isShowing()) {
					avatorPop.dismiss();
				}
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));

				// 头像保存目录
				File dir = new File(Constants.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}


				Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, Constants.REQUESTCODE_UPLOADAVATAR_LOCATION);

				if (avatorPop.isShowing()) {
					avatorPop.dismiss();
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				avatorPop.dismiss();
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		// avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);

	}

	/**
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {

		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		mImagePath = Constants.MyAvatarDir + System.currentTimeMillis();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImagePath)));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG);
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照旋转
	int degree = 0;
	private Bitmap bitmap;
	private LoadingDialog dialog;
	private String user_id;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 标记此时不能更新头像
		updateIconFlag = false;
		switch (requestCode) {
		case Constants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改头像
			if (resultCode == RESULT_OK  || path != null) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					return;
				}
				isFromCamera = true;
				startImageAction(mImageUri, 500, 500,
						Constants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			}
			break;
		case Constants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改头像
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// ShowToast("SD不可用");
					return;
				}
				isFromCamera = false;
				mImageUri = data.getData();
				startImageAction(mImageUri, 500, 500,
						Constants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			} else {
				// ShowToast("照片获取失败");
			}

			break;
		case Constants.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
			// TODO sent to crop
			System.out.println("-----"+resultCode);
			if (avatorPop != null) {
				avatorPop.dismiss();
			}

				                baseShowProgressDialog("正在上传头像中...",false);
				                File  head_img = new File(mImagePath);
								RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),head_img);
				                RequestBody user = RequestBody.create(MediaType.parse("text/plain"),username);

				if (username.length() == 11)
					UploadUserHead(requestBody,user);
				else
					UploadUserHeadOpen(requestBody,user);


				/*System.out.println("取消选择拍照的头像 去删除文件");
				BaseUtils.delFile(filePath);
				return;
			} else {
				saveCropAvator(data);*/


			break;
		default:
			break;

		}
	}

	private void UploadUserHead(RequestBody head,RequestBody user){
		Subscription subscription = apiService.UploadUserHead(head,user).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<UserHead>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								basehideProgressDialog();
								ShowToast("上传失败，请重试！");
							}

							@Override
							public void onNext(BaseResultEntity<UserHead> baseResultEntity) {
								basehideProgressDialog();
								if (!baseResultEntity.data.result.equals("success")){
									ShowToast("头像上传失败，请重试！");
								}else {
									ShowToast("头像上传成功");
									BaseUtils.setSharedPreferences("userIcon", baseResultEntity.data.url, SetMyInfoActivity.this);
									BaseUtils.setSharedPreferences("userIconlocal",mImagePath, SetMyInfoActivity.this);
									updateUser();
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

	private void UploadUserHeadOpen(RequestBody head,RequestBody oid){
		Subscription subscription = apiService.UploadUserHeadOpen(head,oid).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<UserHead>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								basehideProgressDialog();
								ShowToast("上传失败，请重试！");
							}

							@Override
							public void onNext(BaseResultEntity<UserHead> baseResultEntity) {
								basehideProgressDialog();
								if (!baseResultEntity.data.result.equals("success")){
									ShowToast("头像上传失败，请重试！");
								}else {
									ShowToast("头像上传成功");
									BaseUtils.setSharedPreferences("userIcon", baseResultEntity.data.url, SetMyInfoActivity.this);
									BaseUtils.setSharedPreferences("userIconlocal",mImagePath, SetMyInfoActivity.this);
									updateUser();
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
	 * 保存裁剪的头像 并删除之前的头像
	 * 
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		System.out.println("执行保存文件");
		Bundle extras = data.getExtras();
		if (extras != null) {
			bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				// 保存图片
				String filename = new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()) + ".png";
				path = Constants.MyAvatarDir + filename;
				PhotoUtil.saveBitmap(Constants.MyAvatarDir, filename, bitmap,
						true);
				// 删除旧头像
				String oldUserIcon = BaseUtils.getSharedPreferences("userIcon",
						this);
				String oldFileName = BaseUtils.getFileName(oldUserIcon)
						+ ".png";
				String oldFilePath = Constants.MyAvatarDir + oldFileName;
				BaseUtils.delFile(oldFilePath);
				System.out.println("旧头像路径是" + oldFilePath);
				System.out.println("新头像路径是" + path);
				// 上传头像
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}






	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCompositeSubscription.unsubscribe();
	}
}
