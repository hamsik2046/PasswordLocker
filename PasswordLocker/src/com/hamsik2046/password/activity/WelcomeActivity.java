package com.hamsik2046.password.activity;

import com.hamsik2046.password.R;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class WelcomeActivity extends Activity {
	
	private SharedPreferences userInfo;
	private Boolean isFirst;
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private OpenHelper helper;

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
        initDataBase();
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
	
	private void initDataBase(){
		helper = new DaoMaster.DevOpenHelper(WelcomeActivity.this, "account_db", null);
		db = helper.getWritableDatabase();
		DaoMaster.createAllTables(db, true);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		helper.close();
    	db.close();
    	daoMaster = null;
	}
	
}
