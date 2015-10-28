/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import net.oschina.app.bean.Post;
import net.oschina.app.bean.Result;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.AppConfig;
import net.oschina.app.core.AppException;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 类名 PublishPostActivity.java</br>
 * 创建日期 2014年5月4日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月4日 下午11:28:58</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 发贴
 */
public class PublishPostActivity extends BaseActionBarActivity implements TextWatcher {
	
	private EditText mTitle;
	private EditText mContent;
	private Spinner mCatalog;
	private CheckBox mEmail;
    
    private Button mPublishButton;
    
	private InputMethodManager imm;
	private OSChinaApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//初始化actionbar
		ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
		
		mPublishButton = (Button) getLayoutInflater().inflate(R.layout.actionbar_button, null);
		mPublishButton.setOnClickListener(publishClickListener);
		mPublishButton.setEnabled(false);
		mPublishButton.setText(R.string.publish_post_button);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT, 
				ActionBar.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		lp.rightMargin = (int)TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 10, 
				getResources().getDisplayMetrics());
		bar.setCustomView(mPublishButton, lp);
		
		mApplication = getOsChinaApplication();
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        
        setContentView(R.layout.activity_publish_post);
        initView();
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
	
	/** 初始化界面*/
	private void initView() {
		mTitle = (EditText)findViewById(R.id.question_pub_title);
    	mContent = (EditText)findViewById(R.id.question_pub_content);
    	mEmail = (CheckBox)findViewById(R.id.question_pub_email);
    	mCatalog = (Spinner)findViewById(R.id.question_pub_catalog);
    	
    	mTitle.addTextChangedListener(this);
    	mContent.addTextChangedListener(this);
    	
    	mCatalog.setOnItemSelectedListener(catalogSelectedListener);
    	//编辑器添加文本监听
    	mTitle.addTextChangedListener(UIHelper.getTextWatcher(this, AppConfig.TEMP_POST_TITLE));
    	mContent.addTextChangedListener(UIHelper.getTextWatcher(this, AppConfig.TEMP_POST_CONTENT));
    	
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(this, mContent, AppConfig.TEMP_POST_CONTENT);
    	//显示临时选择分类
    	String position = mApplication.getProperty(AppConfig.TEMP_POST_CATALOG);
    	mCatalog.setSelection(StringUtils.toInt(position, 0));
	}
	
	private AdapterView.OnItemSelectedListener catalogSelectedListener = new AdapterView.OnItemSelectedListener(){
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			//保存临时选择的分类
			mApplication.setProperty(AppConfig.TEMP_POST_CATALOG, position + "");
		}
		
		public void onNothingSelected(AdapterView<?> parent) {}
    };
    
    /** 点击发布的监听*/
    private View.OnClickListener publishClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			
			String title = mTitle.getText().toString();
			if(StringUtils.isEmpty(title)){
				UIHelper.ToastMessage(getActivity(), 
						R.string.publish_post_error_tips1);
				return;
			}
			String content = mContent.getText().toString();
			if(StringUtils.isEmpty(content)){
				UIHelper.ToastMessage(getActivity(), 
						R.string.publish_post_error_tips2);
				return;
			}
			
			if(!mApplication.isLogin()) {
				Toast.makeText(getActivity(), R.string.msg_login_request, Toast.LENGTH_LONG).show();
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivity(i);
				return;
			}
			
			Post post = new Post();
			post.setAuthorId(mApplication.getLoginUid());
			post.setTitle(title);
			post.setBody(content);
			post.setCatalog(mCatalog.getSelectedItemPosition() + 1);
			if(mEmail.isChecked()) {
				post.setIsNoticeMe(1);
			}
			
			publishPost(post);
		}
    	
    };
    
    private void checkData() {
    	String title = mTitle.getText().toString().trim();
    	String content = mContent.getText().toString().trim();
    	
    	if(title.length() > 0 && content.length() > 0) {
    		mPublishButton.setEnabled(true);
    	} else {
    		mPublishButton.setEnabled(false);
    	}
    }

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		checkData();
	}
	
	private void publishPost(final Post post) {
		final ProgressDialog dialog = ProgressDialog.show(
				getActivity(), null, 
				getString(R.string.publish_post_wait_tips),
				true, true);
		
		new AsyncTask<Void, Void, Message>() {

			@Override
			protected Message doInBackground(Void... params) {
				Message msg = new Message();					
				try {
					Result res = mApplication.pubPost(post);
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
				dialog.dismiss();
				if(msg.what == 1){
					Result res = (Result)msg.obj;
					UIHelper.ToastMessage(getActivity(), res.getErrorMessage());
					if(res.OK()){
						//清除之前保存的编辑内容
						mApplication.removeProperty(AppConfig.TEMP_POST_TITLE, 
								AppConfig.TEMP_POST_CATALOG, AppConfig.TEMP_POST_CONTENT);
						//跳转到文章详情
						finish();
					}
				}
				else {
					((AppException)msg.obj).makeToast(getActivity());
				}
			}
			
		}.execute();
	}
}