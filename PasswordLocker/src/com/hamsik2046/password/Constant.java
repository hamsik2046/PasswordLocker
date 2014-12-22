package com.hamsik2046.password;

import android.R.integer;

public class Constant {
	
	public static final int PASSWORD_ACTIVITY_UPDATE_LIST_DATA = 0x14;
	
	//添加分类后，刷新分类spinner并将spinner首选项置为新添加的category
	public static final int SET_NEW_CATEGORY_ON_PROMPT = 0x15;
	
	//选择分类后，点击已有的分类后，刷新添加账户的分类信息
	public static final int UPDATE_CHOOSE_CATEGORY_BUTTON_TEXT = 0x16;
	
	//使用相机拍照后截取图片
	public static final int CROP_FROM_CAMERA = 0x17;
	
	//选择icon对话框，点选icon后，返回addAccount页面，刷新icon
	public static final int ICON_SELECTED_UPDATE_VIEW = 0x18;

}
