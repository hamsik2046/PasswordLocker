package com.hamsik2046.password.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import com.hamsik2046.password.MainActivity;
import com.hamsik2046.password.R;
import com.hamsik2046.password.view.SingleInputFormActivity;
import com.hamsik2046.password.view.Step;
import com.hamsik2046.password.view.TextStep;

public class SetPasswordActivity extends SingleInputFormActivity {

	@Override
	protected List<Step> getSteps(Context context) {
		List<Step> steps1 = new ArrayList<Step>();
		steps1.add(new TextStep(context, "input_pwd",
				InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD, R.string.password_set,
				R.string.password_error, R.string.password_details));
		steps1.add(new TextStep(context, "confirm_pwd",
				InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD,
				R.string.password_confirm, R.string.password_error,
				R.string.password_details));
		return steps1;
	}

	@Override
	protected void onFormFinished(Bundle data) {
		String password_set = TextStep.text(data, "input_pwd");
		String password_cofirm = TextStep.text(data, "confirm_pwd");
		if (password_set.equals(password_cofirm)) {
			Intent intent = new Intent(SetPasswordActivity.this,
					PasswordListActivity.class);
			startActivity(intent);
			finish();
			SharedPreferences userInfo = getSharedPreferences("userInfo",
					Context.MODE_PRIVATE);
			userInfo.edit().putString("password", password_cofirm).commit();
		} else {
			finish();
			Intent intent = new Intent(SetPasswordActivity.this,
					MainActivity.class);
			startActivity(intent);
			Toast.makeText(SetPasswordActivity.this, "twice input not equal",
					Toast.LENGTH_SHORT).show();
		}

	}

}
