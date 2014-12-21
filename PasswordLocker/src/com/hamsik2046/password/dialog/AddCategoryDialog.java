package com.hamsik2046.password.dialog;

import java.util.List;

import com.gc.materialdesign.views.ButtonFlat;
import com.hamsik2046.password.Constant;
import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Category;
import com.hamsik2046.password.dao.CategoryDao;
import com.hamsik2046.password.dao.CategoryDao.Properties;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoSession;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;
import com.hamsik2046.password.utils.AndroidUtils;

import de.greenrobot.dao.query.QueryBuilder;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategoryDialog extends Dialog implements android.view.View.OnClickListener {
	
	private EditText category_name;
	private ButtonFlat commit;
	private Context context;
	private Handler handler;
	// 数据库操作相关类
		private SQLiteDatabase db;
		private DaoMaster daoMaster;
		private OpenHelper helper;
		private DaoSession daoSession;
		private CategoryDao categoryDao;

	public AddCategoryDialog(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_category_dialog);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		int screenWidth = AndroidUtils.getScreenWidth(context);
		params.width = (int) (screenWidth / 1.5);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		initView();
		initDB();
	}
	
	private void initView(){
		category_name = (EditText) findViewById(R.id.input_category_name);
		commit = (ButtonFlat) findViewById(R.id.add_category_commit);
		commit.setOnClickListener(this);
	}
	
	private void initDB(){
		helper = new DaoMaster.DevOpenHelper(context, "account_db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		categoryDao = daoSession.getCategoryDao();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_category_commit:
			String category = category_name.getText().toString().trim();
			if (TextUtils.isEmpty(category)) {
				Toast.makeText(context, "Input category name first~!", Toast.LENGTH_SHORT).show();
			}else {
				QueryBuilder qBuilder = categoryDao.queryBuilder();
				qBuilder.where(Properties.Category.eq(category));
				List categories = qBuilder.list();
				if (categories == null || categories.size() == 0) {
					Category category2 = new Category();
					category2.setCategory(category);
					categoryDao.insert(category2);
					Message msg = new Message();
					msg.what = Constant.UPDATE_CHOOSE_CATEGORY_BUTTON_TEXT;
					msg.obj = category;
					handler.sendMessage(msg);
					AddCategoryDialog.this.dismiss();
				}else {
					Toast.makeText(context, "Category already exists~!", Toast.LENGTH_SHORT).show();
				}
				
			}
			break;

		}
		
	}

}
