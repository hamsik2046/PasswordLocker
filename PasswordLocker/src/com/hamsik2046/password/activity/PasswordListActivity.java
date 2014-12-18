package com.hamsik2046.password.activity;

import java.util.ArrayList;
import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
import com.hamsik2046.password.Constant;
import com.hamsik2046.password.R;
import com.hamsik2046.password.adapter.MyItemLongClickListener;
import com.hamsik2046.password.adapter.MyRecycleViewAdapter;
import com.hamsik2046.password.bean.Account;
import com.hamsik2046.password.dao.AccountDao;
import com.hamsik2046.password.dao.DaoMaster;
import com.hamsik2046.password.dao.DaoSession;
import com.hamsik2046.password.dao.DaoMaster.OpenHelper;
import com.hamsik2046.password.dialog.DeleteAccoutDialog;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PasswordListActivity extends Activity {

	private Toolbar toolbar;
	private RecyclerView mRecyclerView;
	private MyRecycleViewAdapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private List<Account> mData = new ArrayList<Account>();
	private int CLICK_STATE; // 判断当前事件状态，决定是否执行onclick
	private final int STATE_DRAG_BUTTON = 0x12;
	private final int STATE_CLICK_BUTTON = 0x13;
	private ButtonFloat addAccount;
	private int screenWidth;
	private int screenHeight;
	// 数据库操作相关类
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private OpenHelper helper;
	private DaoSession daoSession;
	private AccountDao accountDao;
	private DeleteAccoutDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_list);
		toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_launcher);
		toolbar.setTitle("PasswordLocker");
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		addAccount = (ButtonFloat) findViewById(R.id.add_account);
		addAccount.setOnClickListener(new MyOnClickListener());

		

		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels - 50;

		addAccount.setOnTouchListener(new MyOnTouchListener()); // 实现button在屏幕内拖拽效果

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		if (mAdapter == null) {
			mAdapter = new MyRecycleViewAdapter(PasswordListActivity.this,
					mData);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.setList(mData);
			mAdapter.notifyDataSetChanged();
		}
		mAdapter.setOnItemLongClickListener(new MyItemLongClickListener() {
			@Override
			public void onItemLongClick(View view, int position) {
				Account account = mData.get(position);
				if (account != null) {
					new DeleteAccoutDialog(PasswordListActivity.this,account,deleteDialogHandler).show();
				}
			}
		});

	}

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

	private void initData() {
		// init database
		helper = new DaoMaster.DevOpenHelper(PasswordListActivity.this,
				"account_db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		accountDao = daoSession.getAccountDao();
		mData = accountDao.loadAll();
	}

	private class MyOnTouchListener implements OnTouchListener {
		int firstDownX, firstDownY;
		int lastX, lastY;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int ea = event.getAction();
			switch (ea) {
			case MotionEvent.ACTION_DOWN:
				firstDownX = (int) event.getRawX();
				firstDownY = (int) event.getRawY();
				lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
				lastY = (int) event.getRawY();
				break;

			case MotionEvent.ACTION_MOVE:
				int dx = (int) event.getRawX() - lastX;
				int dy = (int) event.getRawY() - lastY;

				int l = v.getLeft() + dx;
				int b = v.getBottom() + dy;
				int r = v.getRight() + dx;
				int t = v.getTop() + dy;

				if (l < 0) {
					l = 0;
					r = l + v.getWidth();
				}

				if (t < 0) {
					t = 0;
					b = t + v.getHeight();
				}

				if (r > screenWidth) {
					r = screenWidth;
					l = r - v.getWidth();
				}

				if (b > screenHeight) {
					b = screenHeight;
					t = b - v.getHeight();
				}
				v.layout(l, t, r, b);

				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();

				v.postInvalidate();
				break;
			case MotionEvent.ACTION_UP:
				int newX = (int) event.getRawX();
				int newY = (int) event.getRawY();
				if (Math.abs(newX - firstDownX) < 10
						&& Math.abs(newY - firstDownY) < 10) {
					CLICK_STATE = STATE_CLICK_BUTTON;
				} else {
					CLICK_STATE = STATE_DRAG_BUTTON;
				}
				break;
			}
			return false;
		}

	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (CLICK_STATE == STATE_CLICK_BUTTON) {
				Intent addIntent = new Intent(PasswordListActivity.this,
						AddAccountActivity.class);
				startActivity(addIntent);
			}
		}

	}

	public Handler deleteDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.PASSWORD_ACTIVITY_UPDATE_LIST_DATA:
				//刷新数据
				mData = accountDao.loadAll();
				mAdapter.setList(mData);
				mAdapter.notifyDataSetChanged();
				break;

			}
		}
	};

}
