package com.hamsik2046.password.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.hamsik2046.password.Constant;
import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Account;
import com.hamsik2046.password.bean.Category;
import com.hamsik2046.password.dao.AccountDao;
import com.hamsik2046.password.dao.CategoryDao;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoSession;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;
import com.hamsik2046.password.dialog.AddCategoryDialog;
import com.hamsik2046.password.dialog.ChooseCategoryDialog;
import com.hamsik2046.password.dialog.ChooseIconDialog;
import com.hamsik2046.password.utils.AndroidUtils;

public class AddAccountActivity extends Activity implements OnClickListener {

	private ButtonFlat chooseCategory;
	private Toolbar toolbar;
	private ImageView imageView;
	private EditText etUsername;
	private EditText etPassword;
	private EditText etRemarks;
	private ButtonFlat cancel;
	private ButtonFlat confirm;
	private LinearLayout pickImgLayout; // 启动相机拍照还是从本地相册选择图片布局
	private ButtonFlat chooseCamera; // 启动相机拍照获取图片button
	private ButtonFlat chooseDirectory; // 打开本地相册选取图片button
	private RelativeLayout add_account_layout;
	private final int TAKE_PHOTO_WITH_DATA = 0x12;
	private final int GET_PHOTO_FROM_DIRECTORY = 0x13;
	private String mUsername = null;
	private String mPassword = null;
	private String mRemarks = null;
	private String imgPath = null;
	// 数据库操作相关类
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private OpenHelper helper;
	private DaoSession daoSession;
	private AccountDao accountDao;
	private CategoryDao categoryDao;
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

	private void initDB() {
		// init database
		if (helper == null) {
			helper = new DaoMaster.DevOpenHelper(AddAccountActivity.this,
					"account_db", null);
			db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			accountDao = daoSession.getAccountDao();
			categoryDao = daoSession.getCategoryDao();
		}
	}

	private void initView() {
		toolbar = (Toolbar) findViewById(R.id.add_account_toobar);
		toolbar.setNavigationIcon(R.drawable.back_arrow);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		toolbar.setTitle("New Account");

		imageView = (ImageView) findViewById(R.id.choose_icon_img);
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

		chooseCategory = (ButtonFlat) findViewById(R.id.choose_category_btn);
		chooseCategory.setOnClickListener(this);
	}

	private void initDataIfFromEdit() {
		Intent intent = getIntent();
		if (intent.hasExtra("from")) {
			isFromEdit = true;
			editAccount = (Account) intent.getExtras().get("account");
			if (editAccount != null) {
				etUsername.setText(editAccount.getUsername());
				etPassword.setText(editAccount.getPassword());
				etRemarks.setText(editAccount.getRemark());
				chooseCategory.setText(editAccount.getCategory());
			}

		}
	}
	
	private File imageFile;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choose_icon_img:
			new ChooseIconDialog(AddAccountActivity.this, iconDialogHandler)
					.show();
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
			} else {
				if (isFromEdit) {
					Account account = new Account();
					account.setPassword(mPassword);
					account.setUsername(mUsername);
					account.setRemark(mRemarks);
					account.setId(editAccount.getId());
					account.setImg_path(imgPath == null ? "" : imgPath);
					account.setCategory(chooseCategory.getTextView().getText()
							+ "");
					accountDao.update(account);
				} else {
					Account account = new Account();
					account.setPassword(mPassword);
					account.setUsername(mUsername);
					account.setRemark(mRemarks);
					account.setId(System.currentTimeMillis());
					account.setImg_path(imgPath == null ? "" : imgPath);
					account.setCategory(chooseCategory.getTextView().getText()
							+ "");
					if (accountDao == null) {
						initDB();
					}
					accountDao.insert(account);
				}
				finish();
			}
			break;
		case R.id.add_account_layout:
			if (pickImgLayout.getVisibility() == View.VISIBLE) {
				pickImgLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.pick_from_camera:
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, TAKE_PHOTO_WITH_DATA);
			}else {
				Toast.makeText(AddAccountActivity.this, "Please check your SDcard first~!",
						Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.pick_from_phone:
			Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, null); 
			pickIntent.setDataAndType(  
	                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
	                "image/*"); 
			startActivityForResult(pickIntent, GET_PHOTO_FROM_DIRECTORY);
			break;
		case R.id.choose_category_btn:
			new ChooseCategoryDialog(AddAccountActivity.this,
					chooseCategoryDialogHandler).show();
			break;
		}

	}

	public Handler chooseCategoryDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.UPDATE_CHOOSE_CATEGORY_BUTTON_TEXT:
				String category = (String) msg.obj;
				chooseCategory.setText(category);
				break;

			}
		}
	};

	public Handler iconDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (pickImgLayout.getVisibility() == View.GONE) {
					pickImgLayout.setVisibility(View.VISIBLE);
				}
				break;

			}
		}
	};

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case TAKE_PHOTO_WITH_DATA:

			break;
		case GET_PHOTO_FROM_DIRECTORY:
			Intent zoomIntent= new Intent("com.android.camera.action.CROP"); 
			zoomIntent.setDataAndType(data.getData(), "image/*");
			zoomIntent.putExtra("crop", "true");  
			zoomIntent.putExtra("aspectX", 1);// 裁剪框比例  
			zoomIntent.putExtra("aspectY", 1);  
			zoomIntent.putExtra("outputX", 150);// 输出图片大小  
			zoomIntent.putExtra("outputY", 150);  
			zoomIntent.putExtra("return-data", true);  
            startActivityForResult(zoomIntent, 1111);  
			break;
		case Constant.CROP_FROM_CAMERA:

			break;
		case 1111:
			Bitmap get = data.getParcelableExtra("data");
			imageView.setImageBitmap(get);
			FileOutputStream foutput = null;  
			imgPath = getFilesDir().getAbsolutePath()+"/pwdlocker/"+
					Calendar.getInstance().getTimeInMillis()+".jpg";
			File fileDir = new File(getFilesDir().getAbsoluteFile()+"/pwdlocker/");
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
			imageFile = new File(imgPath);
			try {
				foutput = new FileOutputStream(imageFile);  
				get.compress(Bitmap.CompressFormat.JPEG, 100, foutput); 
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				if (foutput!=null) {
					try {
						foutput.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
			break;
		}
	}

	public void onBackPressed() {
		if (pickImgLayout.getVisibility() == View.VISIBLE) {
			pickImgLayout.setVisibility(View.GONE);
			return;
		} else {
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
		categoryDao = null;
	}

	public Handler addCategoryDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SET_NEW_CATEGORY_ON_PROMPT:

				break;

			}
		}
	};

}
