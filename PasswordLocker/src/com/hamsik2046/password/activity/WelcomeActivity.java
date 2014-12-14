package com.hamsik2046.password.activity;

import com.hamsik2046.password.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class WelcomeActivity extends Activity {
	
	private SharedPreferences userInfo;
	private Boolean isFirst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		if (userInfo.getString("password", null) != null) {
			isFirst = false;
		}else {
			isFirst = true;
		}
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		if (isFirst) {
			Intent setPwd = new Intent(WelcomeActivity.this,SetPasswordActivity.class);
			startActivity(setPwd);
			finish();
		}else{
			Intent login = new Intent(WelcomeActivity.this, LoginActivity.class);
			startActivity(login);
			finish();
		}
			
	}
	
}
