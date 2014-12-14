package com.hamsik2046.password.activity;

import java.util.ArrayList;
import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
import com.hamsik2046.password.R;
import com.hamsik2046.password.adapter.MyRecycleViewAdapter;
import com.hamsik2046.password.bean.Account;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

@SuppressLint("NewApi")
public class PasswordListActivity extends Activity {
	
	 private Toolbar toolbar;
	 private RecyclerView mRecyclerView;  
	 private RecyclerView.Adapter mAdapter;  
	 private RecyclerView.LayoutManager mLayoutManager;  
	 private List<Account> mData;
	 private int CLICK_STATE; //判断当前事件状态，决定是否执行onclick
	 private final int STATE_DRAG_BUTTON = 0x12;
	 private final int STATE_CLICK_BUTTON = 0x13;
	 private ButtonFloat addAccount;
	 private int screenWidth;
	 private int screenHeight;

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
		} );
    	
    	mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);  
    	mRecyclerView.setHasFixedSize(true); 
    	
    	mLayoutManager = new LinearLayoutManager(this);  
        mRecyclerView.setLayoutManager(mLayoutManager);  
        
        initData();
  
        mAdapter = new MyRecycleViewAdapter(mData);  
        mRecyclerView.setAdapter(mAdapter);
        
        addAccount = (ButtonFloat) findViewById(R.id.add_account);
        addAccount.setOnClickListener(new MyOnClickListener());
        
        DisplayMetrics dm=getResources().getDisplayMetrics();  
        screenWidth=dm.widthPixels;  
        screenHeight=dm.heightPixels-50; 
        
        addAccount.setOnTouchListener(new MyOnTouchListener());  //实现button在屏幕内拖拽效果
    	
    }	
    
    private void initData(){
    	mData = new ArrayList<Account>();
    	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.douban);
    	for (int i = 0; i < 20; i++) {
			Account account = new Account();
			account.setIcon(bitmap);
			account.setUsername("hamsik2046@gmail.com");
			account.setPassword("luoxin19851101");
            account.setRemark("绑定的支付宝账户是nixoul@126.com");
            if (i==2) {
            	account.setUsername("11");
    			account.setPassword("22");
				account.setRemark(null);
			}else if (i==4) {
				account.setRemark("绑定的支付宝账户是nixoul@126.com绑定的支付宝账户是绑定的支付宝账户是绑定的支付宝账户是绑定的支付宝账户是");
			}
			mData.add(account);
		}
    }
    
    private class MyOnTouchListener implements OnTouchListener{
    	int firstDownX, firstDownY;
        int lastX,lastY;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int ea=event.getAction(); 
			switch(ea){  
	          case MotionEvent.ACTION_DOWN:             
	           firstDownX=(int)event.getRawX();
	           firstDownY=(int)event.getRawY();
	           lastX=(int)event.getRawX();//获取触摸事件触摸位置的原始X坐标  
	           lastY=(int)event.getRawY();             
	           break;  
	           
	          case MotionEvent.ACTION_MOVE:  
	           int dx=(int)event.getRawX()-lastX;  
	           int dy=(int)event.getRawY()-lastY;             
	             
	           int l=v.getLeft()+dx;   
	           int b=v.getBottom()+dy;  
	           int r=v.getRight()+dx;  
	           int t=v.getTop()+dy;  
	  
	           if(l<0){  
	            l=0;      
	            r=l+v.getWidth();  
	           }  
	             
	           if(t<0){  
	            t=0;  
	            b=t+v.getHeight();  
	           }  
	             
	           if(r>screenWidth){  
	            r=screenWidth;  
	            l=r-v.getWidth();  
	           }  
	            
	           if(b>screenHeight){  
	            b=screenHeight;  
	            t=b-v.getHeight();  
	           }  
	           v.layout(l, t, r, b);  
	             
	           lastX=(int)event.getRawX();  
	           lastY=(int)event.getRawY();  

	           v.postInvalidate();             
	           break;  
	          case MotionEvent.ACTION_UP:  
	           int newX = (int) event.getRawX();
	           int newY = (int) event.getRawY();
	           if (newX==firstDownX && newY==firstDownY) {
				   CLICK_STATE = STATE_CLICK_BUTTON;
			   }else {
				   CLICK_STATE = STATE_DRAG_BUTTON;
			   }
	           break;            
	          }  
	    return false;
		}
    	
    }
    
    private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (CLICK_STATE == STATE_CLICK_BUTTON) {
				Intent addIntent = new Intent(PasswordListActivity.this,AddAccountActivity.class);
				startActivity(addIntent);
			}
		}
    	
    }

}
