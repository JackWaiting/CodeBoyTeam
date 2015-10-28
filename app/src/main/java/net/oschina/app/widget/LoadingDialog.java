/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package net.oschina.app.widget;

import com.codeboy.app.oschina.R;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

/**
 * 类名 LoadingDialog.java</br>
 * 创建日期 2014年4月26日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月26日 下午12:29:18</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 加载对话框
 */
public class LoadingDialog {

	private Context mContext;
	private TextView mLoadtext;
	private Dialog mDialog;

	public LoadingDialog(Context context) {
		this.mContext = context;
	}
	
	private void setupDialog() {
		mDialog = new Dialog(mContext, R.style.TranslucenceDialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View layout = inflater.inflate(R.layout.loadingdialog, null);
		mLoadtext = (TextView) layout.findViewById(R.id.loading_text);
		mDialog.setContentView(layout);
		
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		// 设置window属性
		LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		//设置对话框的大小
		lp.width = (int) (dm.widthPixels * 0.8);
		lp.height = (int) (dm.heightPixels * 0.4);
		mDialog.getWindow().setAttributes(lp);
	}
	
	public void show() {
		if(mDialog == null){
			setupDialog();
		}
		mDialog.show();
	}
	
	public void dismiss() {
		if(mDialog == null){
			setupDialog();
		}
		mDialog.dismiss();
	}
	
	public void hide() {
		if(mDialog == null){
			setupDialog();
		}
		mDialog.hide();
	}
	
	public void setCancelable(boolean flag) {
		if(mDialog == null){
			setupDialog();
		}
		mDialog.setCancelable(flag);
	}

	/** 设置对话框显示的文字*/
	public void setLoadText(CharSequence text){
		if(mDialog == null) {
			setupDialog();
		}
		mLoadtext.setText(text);
	}
	
	/** 设置对话框显示的文字*/
	public void setLoadText(int resId){
		if(mDialog == null) {
			setupDialog();
		}
		mLoadtext.setText(resId);
	}
}