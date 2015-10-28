/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;


import net.oschina.app.common.UIHelper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.codeboy.app.library.common.DoubleClickExitHelper;
import com.codeboy.app.library.util.L;
import com.codeboy.app.oauth.weibo.WBAuthActivity;
import com.codeboy.app.oschina.modul.DrawerMenuCallBack;
import com.codeboy.app.oschina.ui.ActiveMainFragment;
import com.codeboy.app.oschina.ui.DrawerMenuFragment;
import com.codeboy.app.oschina.ui.NewsMainFragment;
import com.codeboy.app.oschina.ui.QAMainFragment;
import com.codeboy.app.oschina.ui.SoftwareMainFragment;
import com.codeboy.app.oschina.ui.TweetMainFragment;
import com.umeng.update.UmengUpdateAgent;


/**
 * 类名 MainActivity.java</br>
 * 创建日期 2014年4月26日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月26日 下午12:15:10</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 主界面 
 */
public class MainActivity extends BaseActionBarActivity implements DrawerMenuCallBack{
    
	static final String DRAWER_MENU_TAG = "drawer_menu";
	static final String DRAWER_CONTENT_TAG = "drawer_content";
	
	static final String CONTENT_TAG_NEWS = "content_news";
	static final String CONTENT_TAG_QA = "content_questionask";
	static final String CONTENT_TAG_TWEET = "content_tweet";
	static final String CONTENT_TAG_SOFTWARE = "content_software";
	static final String CONTENT_TAG_ACTIVE = "content_active";
	
	static final String CONTENTS[] = {
		CONTENT_TAG_NEWS,
		CONTENT_TAG_QA,
		CONTENT_TAG_TWEET,
		CONTENT_TAG_SOFTWARE,
		CONTENT_TAG_ACTIVE
	};
	
	static final String FRAGMENTS[] = {
		NewsMainFragment.class.getName(),
		QAMainFragment.class.getName(),
		TweetMainFragment.class.getName(),
		SoftwareMainFragment.class.getName(),
		ActiveMainFragment.class.getName()
	};
	
	static final int TITLES[] = {
		R.string.frame_title_news,
		R.string.frame_title_question_ask,
		R.string.frame_title_tweet,
		R.string.frame_title_software,
		R.string.frame_title_active
	};
	
	private ActionBarDrawerToggle mDrawerToggle;
	
	private FragmentManager mFragmentManager;
    private DrawerLayout mDrawerLayout;
    
    //当前显示的界面标识
    private String mCurrentContentTag;
    
    private ActionBar mActionBar;
    private DoubleClickExitHelper mDoubleClickExitHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestActionBarMenu();
		
		//是否自动检查更新
		if(getOsChinaApplication().isCheckUp()) {
			//友盟检查更新
			UmengUpdateAgent.update(this);
		}
		
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		
		setContentView(R.layout.activity_main);
		
		mDoubleClickExitHelper = new DoubleClickExitHelper(this);
		
		mFragmentManager = getSupportFragmentManager();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(new DrawerMenuListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, 0, 0);
		
		if(savedInstanceState == null) {
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			ft.replace(R.id.drawer_menu, DrawerMenuFragment.newInstance(), DRAWER_MENU_TAG)
			.replace(R.id.drawer_content, NewsMainFragment.newInstance(), CONTENT_TAG_NEWS)
			.commit();
			
			mActionBar.setTitle(TITLES[0]);
			mCurrentContentTag = CONTENT_TAG_NEWS;
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if(id == R.id.action_publish_post) {
        	showPublishPostUI();
        	return true;
        } else if(id == R.id.action_publish_tweet) {
        	showTweetUI();
        	return true;
        } else if(id == R.id.action_settings) {
        	UIHelper.showSetting(this);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private void showPublishPostUI() {
		Intent intent = new Intent(this, PublishPostActivity.class);
		startActivity(intent);
	}
	
	private void showTweetUI() {
		Intent intent = new Intent(this, PublishTweetActivity.class);
		startActivity(intent);
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			//判断菜单是否打开
			if(mDrawerLayout.isDrawerOpen(Gravity.START)) {
				mDrawerLayout.closeDrawers();
				return true;
			}
			return mDoubleClickExitHelper.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/** 显示内容*/
	private void showContent(int pos) {
		mDrawerLayout.closeDrawers();
		String tag = CONTENTS[pos];
		if(tag.equals(mCurrentContentTag)) {
			if(L.Debug) {
				L.d("show content:" + tag);
			}
			return;
		}
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		if(mCurrentContentTag != null) {
			Fragment fragment = mFragmentManager.findFragmentByTag(mCurrentContentTag);
			if(fragment != null) {
				ft.remove(fragment);
			}
		}
		ft.replace(R.id.drawer_content, Fragment.instantiate(this, FRAGMENTS[pos]), tag);
		ft.commit();
		
		mActionBar.setTitle(TITLES[pos]);
		mCurrentContentTag = tag;
	}

	@Override
	public void onClickNews() {
		showContent(0);
	}

	@Override
	public void onClickQuestionAsk() {
		showContent(1);
	}

	@Override
	public void onClickTweet() {
		showContent(2);
	}

	@Override
	public void onClickSoftware() {
		showContent(3);
	}
	
	@Override
	public void onClickAvtive() {
		showContent(4);
	}
	
	private class DrawerMenuListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerToggle.onDrawerOpened(drawerView);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerToggle.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            mDrawerToggle.onDrawerStateChanged(newState);
        }
    }
}