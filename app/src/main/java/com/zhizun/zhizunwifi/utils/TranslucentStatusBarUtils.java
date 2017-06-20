package com.zhizun.zhizunwifi.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhizun.zhizunwifi.R;

/**
 *
 * @项目名:	ZhizunWIFI
 * @包名:	com.zhizun.zhizunwifi.utils
 * @类名:	TranslucentStatusBarUtils
 * @创建者:	梁辉
 * @创建时间:	2016-3-29 上午11:52:52
 * @描述:	TODO
 *
 * @SVN版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class TranslucentStatusBarUtils {
	/** * 设置状态栏背景状态 */
	public  static void setTranslucentStatus(Context context) {
		Activity activity = (Activity) context;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemStatusManager tintManager = new SystemStatusManager(activity);
			tintManager.setStatusBarTintEnabled(true);
			// 设置状态栏的颜色
			tintManager.setStatusBarTintResource(R.color.blue_bg);
			activity.getWindow().getDecorView().setFitsSystemWindows(true);
		}
	}

	@TargetApi(19)
	public static void transparencyBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = activity.getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	public static int getStateBarHeight(Activity a) {
		int result = 0;
		int resourceId = a.getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = a.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}


	public  static void setTranslucentStatusBlack(Context context) {
		Activity activity = (Activity) context;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemStatusManager tintManager = new SystemStatusManager(activity);
			tintManager.setStatusBarTintEnabled(true);
			// 设置状态栏的颜色
			tintManager.setStatusBarTintResource(R.color.black);
			activity.getWindow().getDecorView().setFitsSystemWindows(true);
		}
	}


}
