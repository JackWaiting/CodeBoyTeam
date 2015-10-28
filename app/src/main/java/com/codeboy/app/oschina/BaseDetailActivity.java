/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import net.oschina.app.common.UIHelper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.codeboy.app.oschina.adapter.SimpleFragmentPagerAdapter;
import com.codeboy.app.oschina.adapter.TabInfo;
import com.codeboy.app.oschina.widget.CommentDialog;

/**
 * 类名 BaseDetailActivity.java</br>
 * 创建日期 2014年5月2日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月2日 上午11:39:41</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 详细信息的基类
 */
public abstract class BaseDetailActivity extends BaseActionBarActivity 
	implements OnPageChangeListener {

	protected ViewPager mViewPager;
	//评论数显示的按钮
	protected Button mCommentCountButton;
	protected View mEditBoxView;
	
	protected SimpleFragmentPagerAdapter mAdapter;
	protected OSChinaApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
		
        mApplication = getOsChinaApplication();
        mAdapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        
        setContentView(R.layout.activity_base_detail);
        
        initView();
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
	
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.basedetail_viewpager);
		mCommentCountButton = (Button) findViewById(R.id.basedetail_button);
		mEditBoxView = findViewById(R.id.basedetail_editbox);
		
		mCommentCountButton.setOnClickListener(mButtonClickListener);
		mEditBoxView.setOnClickListener(mButtonClickListener);
		
		mEditBoxView.setEnabled(false);
		
		mViewPager.setOnPageChangeListener(this);
		
		mAdapter.addTab(getBodyTabInfo(getBodyFragmentTag()));
		mAdapter.addTab(getCommentTabInfo(getCommentFragmentTag()));
		mViewPager.setAdapter(mAdapter);
		
	}
	
	protected abstract TabInfo getBodyTabInfo(String tag);
	protected abstract TabInfo getCommentTabInfo(String tag);
	/** 获取评论数*/
	protected abstract int getCommentCount();
	/** 评论对话框*/
	protected abstract CommentDialog getCommentDialog();
	/** 数据是否已经加载完毕*/
	protected abstract boolean isDataLoaded();
	
	/** 
	 * 在AndroidSupportV4里，每一个位置的fragment的tag生成规则
	 * 详细看源码
	 * @param viewId ViewPager的id
	 * @param id adapter里的long id,规则以位置作为id
	 * */
	static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
	
	String getBodyFragmentTag() {
		return makeFragmentName(mViewPager.getId(), 0);
	}
	
	String getCommentFragmentTag() {
		return makeFragmentName(mViewPager.getId(), 1);
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int position) {
		updateButton();
	}
	
	private OnClickListener mButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.basedetail_button) {
				onClickCommentCountButton();
			} else if(id == R.id.basedetail_editbox) {
				onClickEditBoxButton();
			}
		}
	};
	
	protected void onClickCommentCountButton() {
		//点击评论与原文
		int pos = mViewPager.getCurrentItem();
		if(pos == 0){
			mViewPager.setCurrentItem(1);
		} else {
			mViewPager.setCurrentItem(0);
		}
	}
	
	protected void onClickEditBoxButton() {
		//先判断是否已经加载数据
		if(!isDataLoaded()) {
			return;
		}
		//判断是否已经登录
		boolean login = mApplication.isLogin();
		if(login) {
			getCommentDialog().show();
		} else {
			Toast.makeText(this, R.string.msg_login_request, Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, LoginActivity.class));
		}
	}
	
	/** 更新按钮状态*/
	protected void updateButton() {
		mEditBoxView.setEnabled(isDataLoaded());
		int pos = mViewPager.getCurrentItem();
		if(pos == 0) {
			updateBodyButton(getCommentCount());
		} else {
			updateCommentButton();
		}
	}
	
	/** 在显示内容时，按钮为显示评论数*/
	protected void updateBodyButton (int count) {
		UIHelper.setupCommentButton(mCommentCountButton, count);
	}
	
	/** 在显示评论时，按钮为显示原文*/
	protected void updateCommentButton() {
		mCommentCountButton.setText(R.string.comment_original_text);
		mCommentCountButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}
}