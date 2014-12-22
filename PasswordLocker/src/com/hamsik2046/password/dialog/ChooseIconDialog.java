package com.hamsik2046.password.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.gc.materialdesign.views.ButtonFloatSmall;
import com.hamsik2046.password.Constant;
import com.hamsik2046.password.R;
import com.hamsik2046.password.adapter.MyGridViewAdapter;
import com.hamsik2046.password.bean.Account;
import com.hamsik2046.password.dao.AccountDao;
import com.hamsik2046.password.dao.CategoryDao;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoSession;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;
import com.hamsik2046.password.utils.AndroidUtils;

public class ChooseIconDialog extends Dialog implements OnItemClickListener {

	private Context context;
	private Handler mHandler;
	private List<Account> data;
	// 数据库操作相关类
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private OpenHelper helper;
	private DaoSession daoSession;
	private AccountDao accountDao;

	public ChooseIconDialog(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.mHandler = handler;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_icon_dialog);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		int screenWidth = AndroidUtils.getScreenWidth(context);
		int screenHeight = AndroidUtils.getScreenHeight(context);
		params.width = (int) (screenWidth / 1.2);
		params.height = (int) (screenHeight / 1.5);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		initDB();
		initGridviewData();
	}

	private void initDB() {
		helper = new DaoMaster.DevOpenHelper(context, "account_db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		accountDao = daoSession.getAccountDao();
	}

	private void initGridviewData() {
		GridView iconGridView = (GridView) findViewById(R.id.icon_gridview);
		iconGridView.setOnItemClickListener(this);
		
		data = accountDao.loadAll();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getImg_path().equals("")) {
				data.remove(i);
			}
		}
		MyGridViewAdapter adapter = new MyGridViewAdapter(context, data);
		iconGridView.setAdapter(adapter);

		ButtonFloatSmall addIcon = (ButtonFloatSmall) findViewById(R.id.add_icon_button);
		addIcon.setOnClickListener(new ButtonOnClickListener());
	}

	private class ButtonOnClickListener implements
			android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.add_icon_button:
				dismiss();
				mHandler.sendEmptyMessage(0);
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Account account = data.get(position);
		Message msg = new Message();
		msg.what = Constant.ICON_SELECTED_UPDATE_VIEW;
		msg.obj = account;
		mHandler.sendMessage(msg);
		ChooseIconDialog.this.dismiss();
	}

}
