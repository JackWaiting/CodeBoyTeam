/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.adapter.TabsFragmentPagerAdapter;

/**
 * 类名 SoftwareMainFragment.java</br>
 * 创建日期 2014年4月27日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月27日 下午7:50:22</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 开源软件库主界面
 */
public class SoftwareMainFragment extends BaseMainFragment{
	
	public static SoftwareMainFragment newInstance() {
		return new SoftwareMainFragment();
	}

	@Override
	protected void onSetupTabAdapter(TabsFragmentPagerAdapter adapter) {
		adapter.addTab(getString(R.string.frame_title_software_recommon),
				"software_recommon", SoftwareRecommonFragment.class, null);
		
        adapter.addTab(getString(R.string.frame_title_software_lastest),
        		"software_lastest", SoftwareLastestFragment.class, null);
        
        adapter.addTab(getString(R.string.frame_title_software_hot),
        		"software_hot", SoftwareHotFragment.class, null);
        
        adapter.addTab(getString(R.string.frame_title_software_china),
        		"software_china", SoftwareChinaFragment.class, null);
	}

}
