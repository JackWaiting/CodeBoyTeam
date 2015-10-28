/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import java.util.regex.Pattern;

import net.oschina.app.bean.Result;
import net.oschina.app.bean.User;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.core.ApiClient;
import net.oschina.app.core.AppContext;
import net.oschina.app.core.AppException;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 类名 LoginActivity.java</br>
 * 创建日期 2014年4月26日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月26日 上午12:08:38</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 登录
 */
public class LoginActivity extends BaseActionBarActivity 
	implements OnClickListener, OnEditorActionListener{

	private EditText mAccountEditText;
	private EditText mPasswordEditText;
	
	private CheckBox mRememberCheckBox;
	private ProgressDialog mLoginProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		ActionBar bar = getSupportActionBar();
		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
        bar.setDisplayOptions(change, flags);
        
        //初始化界面
        mAccountEditText = (EditText) findViewById(R.id.login_account_edittext);
        mPasswordEditText = (EditText) findViewById(R.id.login_passwd_edittext);
        mRememberCheckBox = (CheckBox) findViewById(R.id.login_remember_checkbox);
        
        findViewById(R.id.login_button).setOnClickListener(this);
        
        //是否显示登录信息
	    AppContext ac = (AppContext)getApplication();
	    User user = ac.getLoginInfo();
	    if(user==null || !user.isRememberMe()) { 
	    	return;
	    }
	    if(!StringUtils.isEmpty(user.getAccount())){
	    	mAccountEditText.setText(user.getAccount());
	    	mAccountEditText.selectAll();
	    	mRememberCheckBox.setChecked(user.isRememberMe());
	    }
	    if(!StringUtils.isEmpty(user.getPwd())){
	    	mPasswordEditText.setText(user.getPwd());
	    }
	    mPasswordEditText.setOnEditorActionListener(this);
	    mAccountEditText.addTextChangedListener(new TextWatcher() {
			//账号只能输入特定的字符
	    	String regEx = "[^-._@0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]";
	    	Pattern p = Pattern.compile(regEx);
	    	
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
		        int editEnd = mAccountEditText.getSelectionEnd();
				String text = s.toString();
				String resultText = p.matcher(text).replaceAll("");
				//如果处理后的文本是一样的，则不再需要执行以下
				if(text.equals(resultText)) {
					return;
				}
				
				if(editEnd > resultText.length()) {
					editEnd = resultText.length();
				} else {
					editEnd = editEnd - 1;
				}
				editEnd = editEnd < 0 ? 0 : editEnd;
				mAccountEditText.removeTextChangedListener(this);
				mAccountEditText.setText(resultText);
				mAccountEditText.setSelection(editEnd);
				mAccountEditText.addTextChangedListener(this);
			}
		});
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	@Override
	public void onClick(View v) {
		checkLogin();
	}
	
	/** 登录信息检查与登录*/
	private void checkLogin() {
		String account = mAccountEditText.getText().toString();
		String passwd = mPasswordEditText.getText().toString();
		boolean remember = mRememberCheckBox.isChecked();
		
		////检查用户输入的参数
		if(StringUtils.isEmpty(account)){
			UIHelper.ToastMessage(this, getString(R.string.msg_login_email_null));
			return;
		}
		if(StringUtils.isEmpty(passwd)){
			UIHelper.ToastMessage(this, getString(R.string.msg_login_pwd_null));
			return;
		}
		
		login(account, passwd, remember);
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		//在输入法里点击了“完成”，则去登录
		if(actionId == EditorInfo.IME_ACTION_DONE) {
			checkLogin();
			//将输入法隐藏
			InputMethodManager imm = (InputMethodManager)getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
			return true;
		}
		return false;
	}
	
	//登录验证
    private void login(final String account, final String pwd, final boolean isRememberMe) {
    	if(mLoginProgressDialog == null) {
    		mLoginProgressDialog = new ProgressDialog(this);
    		mLoginProgressDialog.setCancelable(false);
    		mLoginProgressDialog.setMessage(getString(R.string.login_dialog_tips));
    	}
    	
    	//异步登录
    	new AsyncTask<Void, Void, Message>() {

			@Override
			protected Message doInBackground(Void... params) {
				Message msg =new Message();
				try {
					AppContext ac = getOsChinaApplication(); 
	                User user = ac.loginVerify(account, pwd, isRememberMe);
	                Result res = user.getValidate();
	                if(res.OK()){
	                	msg.what = 1;//成功
	                	msg.obj = user;
	                }else{
	                	msg.what = 0;//失败
	                	msg.obj = res.getErrorMessage();
	                }
	            } catch (AppException e) {
	            	e.printStackTrace();
			    	msg.what = -1;
			    	msg.obj = e;
	            }
				return msg;
			}
			
			@Override
			protected void onPreExecute() {
				if(mLoginProgressDialog != null) {
					mLoginProgressDialog.show();
				}
			}
			
			@Override
			protected void onPostExecute(Message msg) {
				//如果程序已经关闭，则不再执行以下处理
				if(isFinishing()) {
					return;
				}
				if(mLoginProgressDialog != null) {
					mLoginProgressDialog.dismiss();
				}
				Context context = LoginActivity.this;
				if(msg.what == 1){
					User user = (User)msg.obj;
					if(user != null){
						//提示登陆成功
						UIHelper.ToastMessage(context, R.string.msg_login_success);
						//返回标识，成功登录
						setResult(RESULT_OK);
						finish();
					}
				} else if(msg.what == 0){
					UIHelper.ToastMessage(context, getString(
							R.string.msg_login_fail) + msg.obj);
				} else if(msg.what == -1){
					((AppException)msg.obj).makeToast(context);
				}
			}
		}.execute();
    }
}