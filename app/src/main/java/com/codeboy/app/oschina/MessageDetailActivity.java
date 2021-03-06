/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import net.oschina.app.bean.Result;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppException;

import com.codeboy.app.oschina.core.Contanst;
import com.codeboy.app.oschina.modul.OnNotifyUpdateListener;
import com.codeboy.app.oschina.modul.OnStatusListener;
import com.codeboy.app.oschina.ui.MessageDetailFragment;
import com.codeboy.app.oschina.widget.CommentDialog;
import com.codeboy.app.oschina.widget.CommentDialog.OnCommentCallListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 类名 MessageDetailActivity.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 下午11:33:34</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 空间消息详细界面
 */
public class MessageDetailActivity extends BaseActionBarActivity
	implements OnStatusListener, OnClickListener, OnCommentCallListener {

		private final static int REFLASH_ITEM_ID = 100;
		
		private Menu optionsMenu;
		private int friendId;
		
		private View mEditBoxView;
		
		private CommentDialog mCommentDialog;
		
		private OSChinaApplication mApplication;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			ActionBar bar = getSupportActionBar();
			int flags = ActionBar.DISPLAY_HOME_AS_UP;
	        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, flags);
	        
	        mApplication = getOsChinaApplication();
	        
	        Intent intent = getIntent();
	        friendId = intent.getIntExtra(Contanst.MESSAGE_FRIEND_ID_KEY, 0);
	        String friendName = intent.getStringExtra(Contanst.MESSAGE_FRIEND_NAME_KEY);
	        
	        bar.setTitle(getString(R.string.message_detail_head_title, friendName));
	        
	        setContentView(R.layout.activity_base_detail2);
	        
	        if(savedInstanceState == null) {
	        	getSupportFragmentManager().beginTransaction()
	        	.replace(R.id.container, MessageDetailFragment.newInstance(friendId))
	        	.commit();
	        }
	        
	        initView();
		}
		
		@Override
		public boolean onSupportNavigateUp() {
			finish();
			return true;
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			optionsMenu = menu;
			//刷新按钮
			MenuItem reflashItem = menu.add(0, REFLASH_ITEM_ID, 
					100, R.string.footbar_refresh);
	        reflashItem.setIcon(R.drawable.ic_menu_refresh);
	        MenuItemCompat.setShowAsAction(reflashItem, 
	        		MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			//刷新
			if(item.getItemId() == REFLASH_ITEM_ID) {
				notifyUpdate();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		
		@Override
		public void onStatus(int status) {
			if(optionsMenu == null) {
				return;
			}
			// 更新菜单的状态
			final MenuItem refreshItem = optionsMenu.findItem(REFLASH_ITEM_ID);
	        if (refreshItem == null) {
	            return;
	        }
			if(status == STATUS_LOADED) {
				MenuItemCompat.setActionView(refreshItem, null);
			} else {
				MenuItemCompat.setActionView(refreshItem, 
						R.layout.actionbar_indeterminate_progress);
			}
		}
		
		private void initView() {
			mEditBoxView = findViewById(R.id.basedetail_editbox);
			Button mCommentButton = (Button)findViewById(R.id.basedetail_button);
			
			mEditBoxView.setOnClickListener(this);
			mCommentButton.setVisibility(View.GONE);
		}
		
		/** 通知更新*/
		private void notifyUpdate() {
			Fragment fragment = getSupportFragmentManager()
					.findFragmentById(R.id.container);
			if(fragment == null || !(fragment instanceof OnNotifyUpdateListener)) {
				return;
			}
			OnNotifyUpdateListener listener = (OnNotifyUpdateListener) fragment;
			//通知刷新
			listener.onNotifyUpdate();
		}
		
		@Override
		public void onClick(View v) {
			if(!mApplication.isLogin()) {
				Toast.makeText(this, R.string.msg_login_request, Toast.LENGTH_LONG).show();
				Intent i = new Intent(this, LoginActivity.class);
				startActivity(i);
				return;
			}
			if(mCommentDialog == null) {
				mCommentDialog = new CommentDialog(this);
				mCommentDialog.setOnCommentCallListener(this);
			}
			mCommentDialog.show();
		}

		@Override
		public void onCommentCall(final String text) {
			if(mCommentDialog != null) {
				mCommentDialog.dismiss();
			}
			if(!mApplication.isLogin()) {
				Toast.makeText(this, R.string.msg_login_request, Toast.LENGTH_LONG).show();
				Intent i = new Intent(this, LoginActivity.class);
				startActivity(i);
				return;
			}
			
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(getString(R.string.comment_publish_loading));
			
			new AsyncTask<Void, Void, Message>() {

				@Override
				protected Message doInBackground(Void... params) {
					Message msg = new Message();
					int uid = mApplication.getLoginUid();
					try {
						Result res = mApplication.pubMessage(uid, friendId, text);
						msg.what = 1;
						msg.obj = res;
					} catch (AppException e) {
						e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
					}
					return msg;
				}
				
				@Override
				protected void onPreExecute() {
					dialog.show();
				}
				
				@Override
				protected void onPostExecute(Message msg) {
					if(isFinishing()) {
						return;
					}
					final Context context = getActivity();
					dialog.dismiss();
					if (msg.what == 1) {
						Result res = (Result) msg.obj;
						if (res.OK()) {
							UIHelper.ToastMessage(context, R.string.comment_publish_success);
							notifyUpdate();
						} else {
							UIHelper.ToastMessage(context, res.getErrorMessage());
						}
					} else {
						((AppException) msg.obj).makeToast(context);
					}
				}
				
			}.execute();
		}
}
