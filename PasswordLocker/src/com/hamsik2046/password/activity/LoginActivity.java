package com.hamsik2046.password.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import com.hamsik2046.password.MainActivity;
import com.hamsik2046.password.R;
import com.hamsik2046.password.view.SingleInputFormActivity;
import com.hamsik2046.password.view.Step;
import com.hamsik2046.password.view.TextStep;

public class LoginActivity extends SingleInputFormActivity {

	@Override
	protected List<Step> getSteps(Context context) {
		List<Step> steps2 = new ArrayList<Step>();
		steps2.add(new TextStep(context, "login_pwd",
				InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD, R.string.password_login,
				R.string.password_error, R.string.password_details));
		return steps2;
	}

	@Override
	protected void onFormFinished(Bundle data) {
		String password = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE).getString("password", null);
		String password_login = TextStep.text(data, "login_pwd");
		if (password_login.equals(password)) {
			Intent intent = new Intent(LoginActivity.this,
					PasswordListActivity.class);
			startActivity(intent);
			finish();
		}else {
			Toast.makeText(LoginActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
			finish();
			Intent intent = new Intent(LoginActivity.this,
					LoginActivity.class);
			startActivity(intent);
		}

	}

}
