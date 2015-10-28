/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.codeboy.app.library.util.L;
import com.codeboy.app.library.util.Util;
import com.codeboy.app.oschina.R;

/**
 * 类名 CommentDialog.java</br>
 * 创建日期 2014年4月30日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月30日 下午11:37:52</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 回复评论的对话框
 */
public class CommentDialog {
	
	private Context mContext;

	private Dialog mDialog;
	private EditText mEditText;
	private Button mButton;
	
	private OnCommentCallListener mOnCommentCallListener;
	private InputMethodManager imm;
	
	public CommentDialog(Context context) {
		mContext = context;
		imm = (InputMethodManager) context.getSystemService(
				Context.INPUT_METHOD_SERVICE);
		
		mDialog = new Dialog(context, R.style.CommentDialog);
		SoftKeyboardEventLayout container = 
				new SoftKeyboardEventLayout(mDialog.getContext());
		
		container.setOnSoftKeyboardShowListener(new OnSoftKeyboardShowListener() {
			
			@Override
			public void onSoftKeyboardShow(boolean isShowing) {
				if(L.Debug) {
					L.d("键盘:" + isShowing);
				}
				if(!isShowing) {
					dismiss();
				}
			}
		});
		
		LayoutInflater inflater = mDialog.getLayoutInflater();
		View view = inflater.inflate(R.layout.comment_dialog_layout, null);
		
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
				Gravity.BOTTOM);
		container.addView(view, params);
		
		mDialog.setContentView(container);
		
		mEditText = (EditText)mDialog.findViewById(R.id.comment_edittext);
		mButton = (Button) mDialog.findViewById(R.id.comment_button);
		
		//发表按钮监听
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickComment();
			}
		});
		
		mEditText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK) {
					imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
					dismiss();
					return true;
				}
				return false;
			}
		});
		
		//文本输入框监听
		mEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//判断是否为空
				if(s.length() == 0) {
					mButton.setEnabled(false);
				} else {
					mButton.setEnabled(true);
				}
			}
		});
	}
	
	private void onClickComment() {
		String text = mEditText.getText().toString();
		if(Util.isEmpty(text)) {
			Toast.makeText(mContext, R.string.comment_notnull_tips, 
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(mOnCommentCallListener != null) {
			mOnCommentCallListener.onCommentCall(text);
		}
	}
	
	/** 显示评论的对话框*/
	public void show() {
		mDialog.show();
		mEditText.setText("");
	}
	
	/** 隐藏评论的对话框*/
	public void dismiss() {
		mDialog.dismiss();
	}
	
	/** 设置点击发表的监听*/
	public void setOnCommentCallListener(OnCommentCallListener listener) {
		mOnCommentCallListener = listener;
	}
	
	public static interface OnCommentCallListener {
		public void onCommentCall(String text);
	}
	
	static interface OnSoftKeyboardShowListener {
		public void onSoftKeyboardShow(boolean isShowing);
	}
	
	public static class SoftKeyboardEventLayout extends FrameLayout {
		
		int screenHeight;
		public SoftKeyboardEventLayout(Context context) {
			super(context);
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			screenHeight = dm.heightPixels;
		}

		private OnSoftKeyboardShowListener listener;

		public void setOnSoftKeyboardShowListener(OnSoftKeyboardShowListener listener) {
			this.listener = listener;
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			//判断软件键盘是否显示
			int location[] = new int[2];
			getLocationOnScreen(location);
			int height = getHeight();
			if(L.Debug) {
				L.d("screenHeight:" + screenHeight + "  location: " + location[0] 
						+ "  " + location[1] + "  height:" + height 
						+ "  size:" + (location[1] + height));
			}
			//因为将Dialog的重心设置为底部，则高度就是从底部为0，往上加
			if(listener != null && location[1] != 0 && height != 0) {
				//当前view的y坐标+自身高度等于屏幕高度，则表明正在显示软键盘
				boolean show = location[1] + height == screenHeight;
				listener.onSoftKeyboardShow(show);
			}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}