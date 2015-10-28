/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.adapter.TabsFragmentPagerAdapter;

/**
 * 类名 FravoriteMainFragment.java</br>
 * 创建日期 2014年5月9日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年5月9日 下午10:06:49</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 收藏夹
 */
public class FravoriteMainFragment extends BaseMainFragment {
	
	public static FravoriteMainFragment newInstance() {
		return new FravoriteMainFragment();
	}

	@Override
	protected void onSetupTabAdapter(TabsFragmentPagerAdapter adapter) {
		adapter.addTab(getString(R.string.favorite_software_title), "software", FavoriteSoftwareFragment.class, null);
		adapter.addTab(getString(R.string.favorite_post_title), "post", FavoritePostFragment.class, null);
		adapter.addTab(getString(R.string.favorite_code_title), "code", FavoriteCodeFragment.class, null);
		adapter.addTab(getString(R.string.favorite_blog_title), "blog", FavoriteBlogFragment.class, null);
		adapter.addTab(getString(R.string.favorite_news_title), "news", FavoriteNewsFragment.class, null);
	}

}
