package com.hamsik2046.password.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamsik2046.password.R;

public class MyViewHolder extends RecyclerView.ViewHolder implements
		OnLongClickListener {
	public CardView mCardView;
	public ImageView mIcon;
	public TextView mUsername;
	public TextView mPassword;
	public TextView mRemark;
	private MyItemLongClickListener mLongClickListener;

	public MyViewHolder(View v, MyItemLongClickListener longClickListener) {
		super(v);
		mCardView = (CardView) v.findViewById(R.id.card_view);
		mIcon = (ImageView) mCardView.findViewById(R.id.icon);
		mUsername = (TextView) mCardView.findViewById(R.id.username);
		mPassword = (TextView) mCardView.findViewById(R.id.password);
		mRemark = (TextView) mCardView.findViewById(R.id.remark);
		this.mLongClickListener = longClickListener;
		v.setOnLongClickListener(this);
	}

	@Override
	public boolean onLongClick(View v) {
		if (mLongClickListener != null) {
			mLongClickListener.onItemLongClick(v, getPosition());
		}
		return true;
	}
}
