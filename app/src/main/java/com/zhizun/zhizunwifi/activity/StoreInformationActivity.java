package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.dialog.LoadingDialog;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.Constants;
import com.zhizun.zhizunwifi.utils.StoreTypeListener;
import com.zhizun.zhizunwifi.widget.ImageViewRoundOval;
import com.zhizun.zhizunwifi.widget.PopStoreType;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.qiujuer.genius.ui.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class StoreInformationActivity extends BaseActivity {
	@InjectView(id = R.id.headerLeft)
	ImageView headleft;
	@InjectView(id = R.id.headerTitle)
	TextView headerTitle;
	@InjectView(id = R.id.store_name)
	EditText store_name;
	@InjectView(id = R.id.store_positioning)
	TextView store_positioning;
	@InjectView(id = R.id.store_address)
	EditText store_address;
	@InjectView(id = R.id.store_type)
	TextView store_type;
	@InjectView(id = R.id.store_sample)
	LinearLayout store_sample;
	@InjectView(id = R.id.store_signature)
	ImageViewRoundOval store_signature;
	@InjectView(id = R.id.store_environment)
	ImageViewRoundOval store_environment;
	@InjectView(id = R.id.store_product)
	ImageViewRoundOval store_product;
	@InjectView(id = R.id.store_review)
	Button store_review;
	@InjectView(id = R.id.store_submit)
	Button store_submit;
	@InjectView(id = R.id.store_main_lay)
	RelativeLayout store_main_lay;

	private int imageType;//0 招牌  1 环境  2 产品

	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	RelativeLayout cancel;
	PopupWindow avatorPop;
	private Uri mImageUri;
	private String mImagePath;
	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照旋转
	int degree = 0;
	private Bitmap bitmap;
	private LoadingDialog dialog;
	private String user_id;
	private boolean updateIconFlag = true;
	private String path;

	public String filePath = "";

	private PopStoreType storepop;
	private Context mContext = this;
	private ArrayList<String> Images = new ArrayList<>();
	protected CompositeSubscription mCompositeSubscription;
	private HttpService apiService;
	private Intent intent0;
	private static int PHOTO = 10086;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_information_main);
		initView();

		apiService =  HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();

		Images.add("");
		Images.add("");
		Images.add("");
		intent0 = this.getIntent();

	}
	private void initView() {
		headleft.setOnClickListener(onClickListener);
		headerTitle.setText("店铺信息");

		store_signature.setType(ImageViewRoundOval.TYPE_ROUND);
		store_signature.setRoundRadius(12);//矩形凹行大小
		store_signature.setOnClickListener(onClickListener);

		store_environment.setType(ImageViewRoundOval.TYPE_ROUND);
		store_environment.setRoundRadius(12);//矩形凹行大小
		store_environment.setOnClickListener(onClickListener);

		store_product.setType(ImageViewRoundOval.TYPE_ROUND);
		store_product.setRoundRadius(12);//矩形凹行大小
		store_product.setOnClickListener(onClickListener);

		store_sample.setOnClickListener(onClickListener);
		store_review.setOnClickListener(onClickListener);
		store_submit.setOnClickListener(onClickListener);
		store_type.setOnClickListener(onClickListener);

		store_positioning.setText(BaseUtils.getSharedPreferences("address",mContext));
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
	      int id = view.getId();
			switch (id){
				case R.id.headerLeft:
					finish();
					break;
				case R.id.store_type:
					if (storepop == null) {
						//自定义的单击事件
						storepop = new PopStoreType(mContext, typeListener);
						//监听窗口的焦点事件，点击窗口外面则取消显示
						storepop.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									storepop.dismiss();
								}
							}
						});
					}
					storepop.showAtLocation(store_main_lay, Gravity.BOTTOM, 0, 0);
					storepop.update();
					break;
				case R.id.store_sample:
			          startActivity(new Intent(mContext,StoreSampleActivity.class));
					break;
				case R.id.store_signature:
					imageType = 0;
					if (TextUtils.isEmpty(Images.get(imageType))){
						showAvatarPop();
					}else {
						Intent intent = new Intent(mContext, PhotoViewActivity.class);
						intent.putExtra("photoTitle", "室外招牌");
						intent.putExtra("photoShow", true);
						intent.putExtra("photoUrl",Images.get(imageType));
						intent.putExtra("imageType",imageType+"");
						startActivityForResult(intent,PHOTO);
					}
					break;
				case R.id.store_environment:
					imageType = 1;
					if (TextUtils.isEmpty(Images.get(imageType))){
						showAvatarPop();
					}else {
						Intent intent = new Intent(mContext, PhotoViewActivity.class);
						intent.putExtra("photoTitle", "室内环境");
						intent.putExtra("photoShow", true);
						intent.putExtra("photoUrl",Images.get(imageType));
						intent.putExtra("imageType",imageType+"");
						startActivityForResult(intent,PHOTO);
					}
					break;
				case R.id.store_product:
					imageType = 2;
					if (TextUtils.isEmpty(Images.get(imageType))){
						showAvatarPop();
					}else {
						Intent intent = new Intent(mContext, PhotoViewActivity.class);
						intent.putExtra("photoTitle", "特色产品");
						intent.putExtra("photoShow", true);
						intent.putExtra("photoUrl",Images.get(imageType));
						intent.putExtra("imageType",imageType+"");
						startActivityForResult(intent,PHOTO);
					}
					break;
				case R.id.store_review:
					Intent intent = new Intent(mContext,StoreReviewActivity.class);
					intent.putStringArrayListExtra("images",Images);
					intent.putExtra("storename",store_name.getText().toString());
					intent.putExtra("storetype",store_type.getText().toString());
					startActivity(intent);
					break;
				case R.id.store_submit:
					if (TextUtils.isEmpty(store_name.getText().toString())){
						ShowToast("请输入店铺名");
						return;
					}else if (TextUtils.isEmpty(store_type.getText().toString())){
					    ShowToast("请选择店铺类型");
					    return;
				    }else {
						if (intent0.getStringExtra("phone")!= null){
							putBusinessInfo(intent0.getStringExtra("router"),intent0.getStringExtra("phone"),
									intent0.getStringExtra("passwd"),intent0.getStringExtra("ssid"),Images.get(0),Images.get(1),Images.get(2));
						}
					}

					break;

			}
		}
	};


	private void putBusinessInfo(String router,String phone,String passwd,String ssid,String img1,String img2,String img3) {
		baseShowProgressDialog("正在提交...",false);
		RequestBody requestBody1 = null;
		RequestBody requestBody2 = null;
		RequestBody requestBody3 = null;
		if (!TextUtils.isEmpty(img1)) {
			requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), new File(img1));
		}
	    if (!TextUtils.isEmpty(img2)) {
			requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), new File(img2));
		}
		if (!TextUtils.isEmpty(img3)){
			requestBody3 = RequestBody.create(MediaType.parse("multipart/form-data"),new File(img3));
		}
		Subscription subscription = apiService.PutBusinessInfo(BaseUtils.getSharedPreferences("lat",mContext),BaseUtils.getSharedPreferences("lon",mContext),router,phone,passwd,
				BaseUtils.getSharedPreferences("userName",mContext),ssid,store_name.getText().toString(),store_positioning.getText().toString(),store_type.getText().toString(),
				store_address.getText().toString(),requestBody1,requestBody2,requestBody3).subscribeOn(Schedulers.io())
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
								ShowToast("提交失败");
							}

							@Override
							public void onNext(BaseResultEntity baseResultEntity) {
								basehideProgressDialog();
								if (baseResultEntity.ret == 200){
									finish();
									Intent intent = new Intent(mContext, WiFiDetailsActivity.class);
									intent.putExtra("audit","0");
									startActivity(intent);
								}else {
									ShowToast(baseResultEntity.msg);
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

	private StoreTypeListener  typeListener = new StoreTypeListener() {
		@Override
		public void onGetStoreTpye(int type) {
			//店铺类型
			if (type == 0)
			    store_type.setText("娱乐");
			else if (type == 1)
				store_type.setText("旅游");
			else if (type == 2)
				store_type.setText("酒店");
			else if (type == 3)
				store_type.setText("美食");
			else if (type == 4)
				store_type.setText("美容保健");
			else if (type == 5)
				store_type.setText("生活服务");
			else if (type == 6)
				store_type.setText("电影");
			else if (type == 7)
				store_type.setText("其它");
		}
	};

	private void showAvatarPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_showavator,
				null);
		// 从相册取
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		// 取消
		cancel = (RelativeLayout) view.findViewById(R.id.cancel);
		// 拍照
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new View.OnClickListener() {

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
		layout_choose.setOnClickListener(new View.OnClickListener() {

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

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				avatorPop.dismiss();
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new View.OnTouchListener() {
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
		avatorPop.showAtLocation(store_main_lay, Gravity.BOTTOM, 0, 0);

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHOTO){
			String type = data.getStringExtra("imageType");
			if ("0".equals(type)){
				Images.remove(imageType);
				Images.add(imageType,"");
				store_signature.setImageResource(R.drawable.me_icon_add);
			}else if ("1".equals(type)){
				Images.remove(imageType);
				Images.add(imageType,"");
				store_environment.setImageResource(R.drawable.me_icon_add);
			}else if ("2".equals(type)){
				Images.remove(imageType);
				Images.add(imageType,"");
				store_product.setImageResource(R.drawable.me_icon_add);
			}

		}else {
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
						startImageAction(mImageUri, 800, 800,
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
							ShowToast("SD不可用");
							return;
						}
						isFromCamera = false;
						mImageUri = data.getData();
						startImageAction(mImageUri, 800, 800,
								Constants.REQUESTCODE_UPLOADAVATAR_CROP, true);
					} else {
						ShowToast("照片获取失败");
					}
					break;
				case Constants.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
					// TODO sent to crop
					System.out.println("-----"+resultCode);
					if (avatorPop != null) {
						avatorPop.dismiss();
					}
					if (resultCode == RESULT_OK) {
						if (imageType == 0) {
							Glide.with(mContext).load(mImagePath).into(store_signature);
							Images.add(imageType,mImagePath);
						}else if (imageType == 1) {
							Glide.with(mContext).load(mImagePath).into(store_environment);
							Images.add(imageType,mImagePath);
						}else if (imageType == 2) {
							Glide.with(mContext).load(mImagePath).into(store_product);
							Images.add(imageType,mImagePath);
						}
					}

					break;
				default:
					break;
		    }
		}
	}






}
