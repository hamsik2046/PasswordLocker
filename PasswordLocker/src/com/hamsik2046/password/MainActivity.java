package com.hamsik2046.password;



import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.Dialog;
import com.hamsik2046.password.activity.PasswordListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	
	private ButtonFloat buttonFloat;  //DZT的button
	private SharedPreferences data;
	private Boolean isLogined = false;
	private String password;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		
	}

	private void initView(){
		buttonFloat = (ButtonFloat) findViewById(R.id.buttonFloat);
		buttonFloat.setOnClickListener(this);
		dialog = new Dialog(MainActivity.this, "Set Login Password", null);
	}
	
	private void initData(){
		data = getSharedPreferences("passwordlocker.data", Context.MODE_PRIVATE);
		password = data.getString("login_password", null);
		isLogined = password == null ? false : true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.buttonFloat:
			if (isLogined) {
				showLoginDialog();
			}else {
				showSetLoginPasswordDialog();
			}
			
			break;

		default:
			break;
		}
	}
	
	private void showLoginDialog(){
		
	}
	
	private void showSetLoginPasswordDialog(){
		dialog.setOnConfirmButtonClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				password = dialog.checkEditTextContent();
				if (!TextUtils.isEmpty(password)) {
					dialog.dismiss();
					Intent intent = new Intent(MainActivity.this,PasswordListActivity.class);
					//cache password
					data.edit().putString("login_pwd", password).commit();
				}
			}
		});
		dialog.show();
	}
	
}
