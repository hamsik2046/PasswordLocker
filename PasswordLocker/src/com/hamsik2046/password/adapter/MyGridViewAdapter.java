package com.hamsik2046.password.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Account;
import com.hamsik2046.password.utils.AndroidUtils;

public class MyGridViewAdapter extends BaseAdapter {
	
	private List<Account> data;
	private Context context;
	
	public MyGridViewAdapter(Context context, List<Account> data) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data==null?0:data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.icons_grid_item,
				null);
		ImageView icon = (ImageView) convertView.findViewById(R.id.icon_item);
		float desity = AndroidUtils.getDensity(context);
		int w = (int) (60 * desity);
		int h = (int) (60 * desity);
		icon.setLayoutParams(new RelativeLayout.LayoutParams(w, h));
		String iconUrl = data.get(position).getImg_path();
		if(TextUtils.isEmpty(iconUrl)){
			icon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_icon));
		}else {
			icon.setImageBitmap(AndroidUtils.decodeUri(context, Uri.fromFile(new File(iconUrl))));
			
		}
		return convertView;
	}

}
