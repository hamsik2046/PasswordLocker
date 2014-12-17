package com.hamsik2046.password.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.gc.materialdesign.views.ButtonFloatSmall;
import com.hamsik2046.password.R;
import com.hamsik2046.password.adapter.MyGridViewAdapter;
import com.hamsik2046.password.utils.AndroidUtils;

public class ChooseIconDialog extends Dialog {
	
	private Context context;
	private Handler mHandler;

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
		params.height = (int)(screenHeight/1.5);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		
		
		
		initGridviewData();
	}
	
    private void initGridviewData(){
    	GridView iconGridView = (GridView) findViewById(R.id.icon_gridview);
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
			data.add("11");
		}
        MyGridViewAdapter adapter = new MyGridViewAdapter(context, data);
    	iconGridView.setAdapter(adapter);
    	
    	ButtonFloatSmall addIcon = (ButtonFloatSmall) findViewById(R.id.add_icon_button);
    	addIcon.setOnClickListener(new ButtonOnClickListener());
    }
    
    private class ButtonOnClickListener implements android.view.View.OnClickListener{
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

    
}
