package com.hamsik2046.password.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gc.materialdesign.views.ButtonFlat;
import com.hamsik2046.password.R;
import com.hamsik2046.password.dialog.ChooseIconDialog;

public class AddAccountActivity extends Activity implements OnClickListener{
	
	private Toolbar toolbar;
	private ImageView imageView;
	private EditText etUsername;
	private EditText etPassword;
	private EditText etRemarks;
	private ButtonFlat cancel;
	private ButtonFlat confirm;
	private LinearLayout pickImgLayout; //启动相机拍照还是从本地相册选择图片布局
	private ButtonFlat chooseCamera; //启动相机拍照获取图片button
	private ButtonFlat chooseDirectory; //打开本地相册选取图片button
	private RelativeLayout add_account_layout;


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
        
		imageView= (ImageView) findViewById(R.id.choose_icon_img);
		imageView.setOnClickListener(this);
		etUsername = (EditText) findViewById(R.id.username);
		etPassword = (EditText) findViewById(R.id.password);
		etRemarks = (EditText) findViewById(R.id.remark);
		cancel = (ButtonFlat) findViewById(R.id.cancel_add);
		cancel.setOnClickListener(this);
		confirm = (ButtonFlat) findViewById(R.id.confirm_add);
		confirm.setOnClickListener(this);
		
		pickImgLayout = (LinearLayout) findViewById(R.id.pick_img_layout);

		add_account_layout = (RelativeLayout) findViewById(R.id.add_account_layout);
		add_account_layout.setOnClickListener(this);
		
		chooseCamera = (ButtonFlat) findViewById(R.id.pick_from_camera);
		chooseCamera.setOnClickListener(this);
		
		chooseDirectory = (ButtonFlat) findViewById(R.id.pick_from_phone);
		chooseDirectory.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choose_icon_img:
			new ChooseIconDialog(AddAccountActivity.this,iconDialogHandler).show();
			break;
		case R.id.cancel_add:
			finish();
			break;
		case R.id.confirm_add:
			
			break;
		case R.id.add_account_layout:
			if (pickImgLayout.getVisibility()==View.VISIBLE) {
				pickImgLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.pick_from_camera:
			
			break;
		case R.id.pick_from_phone:
			
			break;
		}
		
	}
	
	public Handler iconDialogHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0:
				if (pickImgLayout.getVisibility()==View.GONE) {
					pickImgLayout.setVisibility(View.VISIBLE);
				}
				break;

			}
		}
	};
	
	
	public void onBackPressed() {
		if (pickImgLayout.getVisibility() == View.VISIBLE) {
			pickImgLayout.setVisibility(View.GONE);
			return;
		}else {
			finish();
		}
	};
	
}
