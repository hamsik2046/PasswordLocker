package com.hamsik2046.password.adapter;

import java.util.List;

import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Account;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

	private List<Account> list;
	private Context context;
	private MyItemLongClickListener mItemLongClickListener;
	
	public MyRecycleViewAdapter(Context context, List<Account> list) {  
        this.list = list;  
        this.context = context;
    } 

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list==null?0:list.size();  
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (list.get(position).getImg_path().equals("")) {
			holder.mIcon.setImageBitmap(BitmapFactory.
					decodeResource(context.getResources(), R.drawable.default_icon));
		}else {
			holder.mIcon.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getImg_path()));
		}
		holder.mUsername.setText(list.get(position).getUsername());
		holder.mPassword.setText(list.get(position).getPassword());
		if (list.get(position).getRemark() == null || list.get(position).getRemark().equals("")) {
			holder.mRemark.setVisibility(View.GONE);
		}else {
			holder.mRemark.setText(list.get(position).getRemark());
		}
		
	}

	// Create new views (invoked by the layout manager) 
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view  
        View v = LayoutInflater.from(parent.getContext())  
                               .inflate(R.layout.recycle_view_item, null);  
        // set the view's size, margins, paddings and layout parameters  
        MyViewHolder vh = new MyViewHolder(v , mItemLongClickListener);  
        return vh;  
	}
	
	public void setList(List<Account> list) {
		this.list = list;
	}
	
	public void setOnItemLongClickListener(MyItemLongClickListener listener){  
        this.mItemLongClickListener = listener;  
    } 
}
