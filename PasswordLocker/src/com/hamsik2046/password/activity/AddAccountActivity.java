package com.hamsik2046.password.activity;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonIcon;
import com.hamsik2046.password.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AddAccountActivity extends Activity implements OnClickListener{
	
	private Toolbar toolbar;
	private ImageView imageView;
	private EditText etUsername;
	private EditText etPassword;
	private EditText etRemarks;
	private ButtonFlat cancel;
	private ButtonFlat confirm;
	private TextView tv_choose_category;
	private LinearLayout ll_show_category;
	private TextView tv_show_category;
	private LinearLayout ll_choose_caterogy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_account);
		initView();
	}
	
	private void initView(){
		toolbar = (Toolbar) findViewById(R.id.add_account_toobar);
		toolbar.setNavigationIcon(R.drawable.back_arrow);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		toolbar.setTitle("New Account");
        
		imageView= (ImageView) findViewById(R.id.choose_icon);
		imageView.setOnClickListener(this);
		etUsername = (EditText) findViewById(R.id.username);
		etPassword = (EditText) findViewById(R.id.password);
		etRemarks = (EditText) findViewById(R.id.remark);
		cancel = (ButtonFlat) findViewById(R.id.cancel_add);
		cancel.setOnClickListener(this);
		confirm = (ButtonFlat) findViewById(R.id.confirm_add);
		confirm.setOnClickListener(this);
		tv_choose_category = (TextView) findViewById(R.id.tv_choose_category);
		ll_show_category = (LinearLayout) findViewById(R.id.ll_show_category);
		ll_choose_caterogy = (LinearLayout) findViewById(R.id.ll_choose_caterogy);
		ll_choose_caterogy.setOnClickListener(this);
		tv_show_category = (TextView) findViewById(R.id.tv_show_category);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choose_icon:
			
			break;
		case R.id.cancel_add:
			finish();
			break;
		case R.id.confirm_add:
			
			break;
		case R.id.ll_choose_caterogy:
			
			break;
		}
		
	}
	
}
