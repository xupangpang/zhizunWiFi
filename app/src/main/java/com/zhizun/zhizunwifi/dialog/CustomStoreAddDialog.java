package com.zhizun.zhizunwifi.dialog;

import com.zhizun.zhizunwifi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 */
public class CustomStoreAddDialog extends Activity implements OnClickListener{

	private Button confirm, abolish;
	private String result;
	private EditText storeName,storeSite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_store_add);

		confirm = (Button) findViewById(R.id.dialog_confirm);
		abolish = (Button) findViewById(R.id.dialog_abolish);
		storeName = (EditText) findViewById(R.id.dialog_item_store_name);
		storeSite = (EditText) findViewById(R.id.dialog_item_store_site);
		
		
		
		confirm.setOnClickListener(this);
		
		abolish.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_confirm:
			Intent intent = new Intent();
			intent.putExtra("StoreNameResult", storeName.getText().toString().trim());
			intent.putExtra("StoreSiteResult", storeSite.getText().toString().trim());
			setResult(RESULT_OK, intent);
			finish();
		case R.id.dialog_abolish:
			finish();
			
			break;

		default:
			break;
		}
	}
	
}
