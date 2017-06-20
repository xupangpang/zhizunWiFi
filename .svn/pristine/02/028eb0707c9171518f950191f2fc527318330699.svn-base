package com.zhizun.zhizunwifi.dialog;

import com.zhizun.zhizunwifi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @项目名: ZhizunWIFI
 * @包名: com.zhizun.zhizunwif.dialog
 * @类名: StoreSelect
 * @创建者: 梁辉
 * @创建时间: 2016年3月17日 下午3:37:57
 * @描述: TODO
 *
 * @SVN版本: $Rev: 682 $
 * @更新人: $Author: liangzi $
 * @更新时间: $Date: 2016-03-30 18:13:11 +0800 (Wed, 30 Mar 2016) $
 * @更新描述: TODO
 */
public class StoreSelectDialog extends Activity implements OnClickListener{

	private RadioButton recreation, travel, hotel, Restaurant, service,
			hairdressing, movie, other;
	private RadioGroup radioGroup;
	private Button confirm, abolish;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_sort);

		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		confirm = (Button) findViewById(R.id.confirm);
		abolish = (Button) findViewById(R.id.abolish);
		recreation = (RadioButton) findViewById(R.id.recreation); // 娱乐
		travel = (RadioButton) findViewById(R.id.travel); // 旅游
		hotel = (RadioButton) findViewById(R.id.hotel); // 酒店
		Restaurant = (RadioButton) findViewById(R.id.Restaurant); // 美食
		hairdressing = (RadioButton) findViewById(R.id.hairdressing); // 美容保健
		service = (RadioButton) findViewById(R.id.service); // 生活服务
		movie = (RadioButton) findViewById(R.id.movie); // 电影
		other = (RadioButton) findViewById(R.id.other); // 其他

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
					case R.id.recreation:
						result = "娱乐";
						break;
					case R.id.travel:
						result = "旅游";
						break;
					case R.id.hotel:
						result = "酒店";
						break;
					case R.id.Restaurant:
						result = "美食";
						break;
					case R.id.hairdressing:
						result = "美容保健";
						break;
					case R.id.service:
						result = "生活服务";
						break;
					case R.id.movie:
						result = "电影";
						break;
					case R.id.other:
						result = "其他";
						break;

					default:
						break;
				}

			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("result", result);
				setResult(RESULT_OK, intent);
				finish();

			}
		});

		abolish.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.confirm:
				Intent intent = new Intent();

				intent.putExtra("result", result);
				setResult(RESULT_OK, intent);
				finish();

				break;
			case R.id.abolish:
				finish();

				break;

			default:
				break;
		}
	}

}
