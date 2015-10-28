/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import com.codeboy.app.oschina.core.Contanst;
import com.codeboy.app.oschina.ui.FriendsMainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

/**
 * 类名 FriendsActivity.java</br>
 * 创建日期 2014年5月9日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月9日 下午11:36:58</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 用户关注、粉丝界面
 */
public class FriendsActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar bar = getSupportActionBar();
		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
        bar.setDisplayOptions(change, flags);
        
        setContentView(R.layout.activity_favorite);
        
        Intent intent = getIntent();
        int followers = intent.getIntExtra(Contanst.FRIENDS_FOLLOWERS, 0);
        int fans = intent.getIntExtra(Contanst.FRIENDS_FANS, 0);
        int type = intent.getIntExtra(Contanst.FRIENDS_TYPE, 0);
        
        if(savedInstanceState == null) {
        	getSupportFragmentManager().beginTransaction()
        	.replace(R.id.container, FriendsMainFragment.newInstance(followers, fans, type))
        	.commit();
        }
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
