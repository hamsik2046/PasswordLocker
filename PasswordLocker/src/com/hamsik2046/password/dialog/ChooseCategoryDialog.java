package com.hamsik2046.password.dialog;

import java.util.ArrayList;
import java.util.List;

import com.gc.materialdesign.views.ButtonFloatSmall;
import com.hamsik2046.password.Constant;
import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Category;
import com.hamsik2046.password.dao.CategoryDao;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoSession;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;
import com.hamsik2046.password.utils.AndroidUtils;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseCategoryDialog extends Dialog implements android.view.View.OnClickListener, OnItemClickListener {

	private Handler handler;
	private Context context;
	private ListView categoryList;
	private ButtonFloatSmall addCategory;
	// 数据库操作相关类
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private OpenHelper helper;
	private DaoSession daoSession;
	private CategoryDao categoryDao;
	private List<Category> categoreis;
	private ArrayAdapter<String> adapter;

	public ChooseCategoryDialog(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_category_dialog);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		int screenWidth = AndroidUtils.getScreenWidth(context);
		int screenHeight = AndroidUtils.getScreenHeight(context);
		params.width = (int) (screenWidth / 1.2);
		params.height = (int) (screenHeight / 1.5);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		initDB();
		initData();
		initView();
	}

	private void initDB(){
		helper = new DaoMaster.DevOpenHelper(context, "account_db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		categoryDao = daoSession.getCategoryDao();
	}
	
	private void initData(){
		categoreis = categoryDao.loadAll();
		if (categoreis != null && categoreis.size() > 0) {
			String[] data = new String[categoreis.size()];
			for(int i = 0; i < categoreis.size(); i++){
				data[i] = categoreis.get(i).getCategory();
				adapter = new ArrayAdapter<String>(context, 
						android.R.layout.simple_list_item_1, data);
			}
		}
	}
	
	private void initView() {
        categoryList = (ListView) findViewById(R.id.category_list);
        if (adapter!=null) {
        	categoryList.setAdapter(adapter);
		}
        categoryList.setOnItemClickListener(this);
        addCategory = (ButtonFloatSmall) findViewById(R.id.add_category_buttonfolat);
        addCategory.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_category_buttonfolat:
			new AddCategoryDialog(context, handler).show();
			ChooseCategoryDialog.this.dismiss();
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String category = categoreis.get(position).getCategory();
		Message msg = new Message();
		msg.obj = category;
		msg.what = Constant.UPDATE_CHOOSE_CATEGORY_BUTTON_TEXT;
		handler.sendMessage(msg);
		ChooseCategoryDialog.this.dismiss();
	}

}
