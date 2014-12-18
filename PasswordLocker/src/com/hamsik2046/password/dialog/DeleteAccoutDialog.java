package com.hamsik2046.password.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gc.materialdesign.views.ButtonFlat;
import com.hamsik2046.password.Constant;
import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Account;
import com.hamsik2046.password.dao.AccountDao;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;
import com.hamsik2046.password.dao.DaoSession;
import com.hamsik2046.password.utils.AndroidUtils;

public class DeleteAccoutDialog extends Dialog  {

	private Context context;
	private Account account;
	// 数据库操作相关类
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private OpenHelper helper;
	private DaoSession daoSession;
	private AccountDao accountDao;
	private Handler handler;

	public DeleteAccoutDialog(Context context, Account account , Handler handler) {
		super(context);
		this.context = context;
		this.account = account;
		this.handler = handler;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.delete_icon_dialog);

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		int screenWidth = AndroidUtils.getScreenWidth(context);
		// int screenHeight = AndroidUtils.getScreenHeight(context);
		params.width = (int) (screenWidth / 1.5);
		// params.height = (int)(screenHeight/1.5);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		initView();
		initDB();
	}

	private void initView() {
		ButtonFlat cancel = (ButtonFlat) findViewById(R.id.cancel_delete);
		cancel.setOnClickListener(new OnCancelClickListener());
		ButtonFlat confirm = (ButtonFlat) findViewById(R.id.confirm_delete);
		confirm.setOnClickListener(new OnConfirmClickListener());
	}

	private void initDB() {
		helper = new DaoMaster.DevOpenHelper(context, "account_db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		accountDao = daoSession.getAccountDao();
	}

	private class OnCancelClickListener implements android.view.View.OnClickListener{
		
		@Override
		public void onClick(View v) {
			DeleteAccoutDialog.this.dismiss();
		}
		
	}
	
	private class OnConfirmClickListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			accountDao.deleteByKey(account.getId());
			closeDB();
			DeleteAccoutDialog.this.dismiss();
			handler.sendEmptyMessage(Constant.PASSWORD_ACTIVITY_UPDATE_LIST_DATA);
		}
		
	}
	
	private void closeDB() {
		helper.close();
		db.close();
		daoSession = null;
		daoMaster = null;
		accountDao = null;
	}

}
