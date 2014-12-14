package com.hamsik2046.password.adapter;

import java.util.List;

import com.hamsik2046.password.R;
import com.hamsik2046.password.bean.Account;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {

	private List<Account> list;
	
	public MyRecycleViewAdapter(List<Account> list) {  
        this.list = list;  
    } 
	
	public static class ViewHolder extends RecyclerView.ViewHolder {  
        public CardView mCardView;  
        public ImageView mIcon;
        public TextView mUsername;
        public TextView mPassword;
        public TextView mRemark;
        public ViewHolder(View v) {  
            super(v);  
            mCardView = (CardView) v.findViewById(R.id.card_view);  
            mIcon = (ImageView) mCardView.findViewById(R.id.icon);
            mUsername = (TextView) mCardView.findViewById(R.id.username);
            mPassword = (TextView) mCardView.findViewById(R.id.password);
            mRemark = (TextView) mCardView.findViewById(R.id.remark);
        }  
    }

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list==null?0:list.size();  
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		holder.mIcon.setImageBitmap(list.get(position).getIcon());
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
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view  
        View v = LayoutInflater.from(parent.getContext())  
                               .inflate(R.layout.recycle_view_item, null);  
        // set the view's size, margins, paddings and layout parameters  
        ViewHolder vh = new ViewHolder(v);  
        return vh;  
	}
}
