package com.hamsik2046.password.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Account;
import com.hamsik2046.password.dao.AccountDao;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoSession;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;
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
	private final int TAKE_PHOTO_WITH_DATA = 0x12;
	private final int GET_PHOTO_FROM_DIRECTORY = 0x13;
	private String mUsername = null;
	private String mPassword = null;
	private String mRemarks = null;
	private String imgPath = null;
	//数据库操作相关类
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private OpenHelper helper;
	private DaoSession daoSession;
	private AccountDao accountDao;
	private Boolean isFromEdit = false;
	private Account editAccount;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_account);
		initView();
		initDataIfFromEdit();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initDB();
	}
	
	private void initDB(){
		//init database
		if (helper == null) {
			helper = new DaoMaster.DevOpenHelper(AddAccountActivity.this, "account_db", null);
	    	db = helper.getWritableDatabase();
	    	daoMaster = new DaoMaster(db);
	    	daoSession = daoMaster.newSession();
	    	accountDao = daoSession.getAccountDao();
		}
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
		etUsername = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRemarks = (EditText) findViewById(R.id.et_remark);
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
	
	private void initDataIfFromEdit(){
		Intent intent = getIntent();
		if (intent.hasExtra("from")) {
			isFromEdit = true;
			editAccount = (Account) intent.getExtras().get("account");
			if (editAccount != null) {
				etUsername.setText(editAccount.getUsername());
				etPassword.setText(editAccount.getPassword());
				etRemarks.setText(editAccount.getRemark());
			}
			
		}
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
			mUsername = etUsername.getText().toString().trim();
			mPassword = etPassword.getText().toString().trim();
			mRemarks = etRemarks.getText().toString().trim();
			if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
				Toast.makeText(AddAccountActivity.this, "账号和密码为必填项哦~！",
						Toast.LENGTH_SHORT).show();
			}else {
				if (isFromEdit) {
					Account account = new Account();
					account.setPassword(mPassword);
					account.setUsername(mUsername);
					account.setRemark(mRemarks);
					account.setId(editAccount.getId());
					account.setImg_path(imgPath==null?"":imgPath);
					account.setCategory("app");
			    	accountDao.update(account);
				}else {
					Account account = new Account();
					account.setPassword(mPassword);
					account.setUsername(mUsername);
					account.setRemark(mRemarks);
					account.setId(System.currentTimeMillis());
					account.setImg_path(imgPath==null?"":imgPath);
					account.setCategory("app");
			    	accountDao.insert(account);
				}
		    	finish();
			}
			break;
		case R.id.add_account_layout:
			if (pickImgLayout.getVisibility()==View.VISIBLE) {
				pickImgLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.pick_from_camera:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 startActivityForResult(intent, TAKE_PHOTO_WITH_DATA);
			break;
		case R.id.pick_from_phone:
			Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT,null);
			intent2.setType("image/*");
			intent2.putExtra("crop", "true");
			//WIDTH 和 HEIGHT指的是截取框的宽高比例，如设WIDTH = 1，HEIGHT = 1，截取框就为正方形
			intent2.putExtra("aspectX", 1);
			intent2.putExtra("aspectY", 1);
			//OUTPUT_X和OUTPUT_Y指的是图片的宽高，可根据实际需要设值
			intent2.putExtra("outputX", 60);
			intent2.putExtra("outputY", 60);
			intent2.putExtra("scale", true);
			//return-data 指是否在onActivityResult方法中返回数据
			intent2.putExtra("return-data", true);
			intent2.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent2.putExtra("noFaceDetection", true);
			startActivityForResult(intent2, GET_PHOTO_FROM_DIRECTORY);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAKE_PHOTO_WITH_DATA && resultCode == Activity.RESULT_OK) {
			String sdCardState = Environment.getExternalStorageState();
			if (sdCardState.equals(Environment.MEDIA_MOUNTED)) {
				Bitmap picture = data.getParcelableExtra("data");
				imageView.setImageBitmap(picture);
				if (pickImgLayout.getVisibility() == View.VISIBLE) {
					pickImgLayout.setVisibility(View.GONE);
				}
				
			}else {
				Toast.makeText(AddAccountActivity.this, "请确认已插入SD卡", Toast.LENGTH_SHORT).show();
			}
			
		}else if (requestCode == GET_PHOTO_FROM_DIRECTORY && resultCode == Activity.RESULT_OK) {
			Bitmap bitmap = data.getParcelableExtra("data");
			imageView.setImageBitmap(bitmap);
			if (pickImgLayout.getVisibility() == View.VISIBLE) {
				pickImgLayout.setVisibility(View.GONE);
			}
		}
	}

	public void onBackPressed() {
		if (pickImgLayout.getVisibility() == View.VISIBLE) {
			pickImgLayout.setVisibility(View.GONE);
			return;
		}else {
			finish();
		}
	};
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		helper.close();
    	db.close();
    	daoSession = null;
    	daoMaster = null;
    	accountDao = null;
	}
	
}
