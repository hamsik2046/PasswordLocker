package com.hamsik2046.password.dialog;

import com.hamsik2046.password.R;
import com.hamsik2046.password.activity.AddAccountActivity;
import com.hamsik2046.password.bean.Account;
import com.hamsik2046.password.utils.AndroidUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ChooseOperationDialog extends Dialog implements android.view.View.OnClickListener {
	
	private Account account;
	private Handler handler;
	private Context context;

	public ChooseOperationDialog(Context context,Handler handler, Account account) {
		super(context);
		this.context = context;
		this.account = account;
		this.handler = handler;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_operation_dialog);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		int screenWidth = AndroidUtils.getScreenWidth(context);
		params.width = (int) (screenWidth / 1.5);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		initView();
	}
	
	private void initView(){
		TextView edit = (TextView) findViewById(R.id.edit_the_item_tv);
		edit.setOnClickListener(this);
		TextView delete = (TextView) findViewById(R.id.delete_the_item_tv);
		delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_the_item_tv:
			Intent intent = new Intent(context,AddAccountActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("account", account);
			intent.putExtras(bundle);
			intent.putExtra("from", "edit");
			context.startActivity(intent);
			ChooseOperationDialog.this.dismiss();
			break;
		case R.id.delete_the_item_tv:
			ChooseOperationDialog.this.dismiss();
			new DeleteAccoutDialog(context, account, handler).show();
			break;
		}
		
	}

}
