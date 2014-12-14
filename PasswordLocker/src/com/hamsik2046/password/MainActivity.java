package com.hamsik2046.password;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.hamsik2046.password.activity.PasswordListActivity;
import com.hamsik2046.password.view.SingleInputFormActivity;
import com.hamsik2046.password.view.Step;
import com.hamsik2046.password.view.TextStep;

public class MainActivity extends SingleInputFormActivity {

	private SharedPreferences userInfo;
	private Boolean isFirst = true; // 是否第一次使用：是则设置密码，否则直接登录(默认为)
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		password = userInfo.getString("password", null);
		if (password != null && !password.equals("")) {
			isFirst = false;
		}
	}

	@Override
	protected List<Step> getSteps(Context context) {
		
		if (isFirst) {
			List<Step> steps1 = new ArrayList<Step>();
			steps1.add(new TextStep(context, "input_pwd",
					InputType.TYPE_TEXT_VARIATION_PASSWORD, R.string.password_set,
					R.string.password_error, R.string.password_details));
			steps1.add(new TextStep(context, "confirm_pwd",
					InputType.TYPE_TEXT_VARIATION_PASSWORD, R.string.password_confirm,
					R.string.password_error, R.string.password_details));
			return steps1;
		} else {
			List<Step> steps2 = new ArrayList<Step>();
			steps2.add(new TextStep(context, "login_pwd",
					InputType.TYPE_TEXT_VARIATION_PASSWORD, R.string.password_login,
					R.string.password_error, R.string.password_details));
			return steps2;
		}

	}

	@Override
	protected void onFormFinished(Bundle data) {
		if (isFirst) {
			String password_set = TextStep.text(data, "input_pwd");
			String password_cofirm = TextStep.text(data, "confirm_pwd");
			if (password_set.equals(password_cofirm)) {
				Intent intent = new Intent(MainActivity.this,
						PasswordListActivity.class);
				startActivity(intent);
				finish();
				 userInfo.edit().putString("password",
				 password_cofirm).commit();
			} else {
				finish();
				Intent intent = new Intent(MainActivity.this,
						MainActivity.class);
				startActivity(intent);
				Toast.makeText(MainActivity.this, "twice input not equal", Toast.LENGTH_SHORT).show();
			}
		} else {
			String password_login = TextStep.text(data, "login_pwd");
			if (password_login.equals(password)) {
				Intent intent = new Intent(MainActivity.this,
						PasswordListActivity.class);
				startActivity(intent);
				finish();
			}else {
				Toast.makeText(MainActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent(MainActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		}

		
	}
	
}
