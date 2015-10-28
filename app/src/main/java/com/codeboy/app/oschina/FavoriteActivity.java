/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina;

import com.codeboy.app.oschina.ui.FravoriteMainFragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;


/**
 * 类名 FavoriteActivity.java</br>
 * 创建日期 2014年5月6日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月6日 下午11:38:10</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 收藏夹
 */
public class FavoriteActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar bar = getSupportActionBar();
		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
        bar.setDisplayOptions(change, flags);
        
        setContentView(R.layout.activity_favorite);
        
        if(savedInstanceState == null) {
        	getSupportFragmentManager().beginTransaction()
        	.replace(R.id.container, FravoriteMainFragment.newInstance())
        	.commit();
        }
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}