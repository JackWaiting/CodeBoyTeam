/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.ui;

import com.codeboy.app.oschina.R;
import com.codeboy.app.oschina.adapter.TabsFragmentPagerAdapter;

/**
 * 类名 QAMainFragment.java</br>
 * 创建日期 2014年4月27日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月27日 下午7:42:51</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 问答主界面
 */
public class QAMainFragment extends BaseMainFragment{
	
	public static QAMainFragment newInstance() {
		return new QAMainFragment();
	}

	@Override
	protected void onSetupTabAdapter(TabsFragmentPagerAdapter adapter) {
		adapter.addTab(getString(R.string.frame_title_question_ask),
				"question_ask", QAQuestionAskFragment.class, null);
        
        adapter.addTab(getString(R.string.frame_title_question_share),
        		"share", QAShareFragment.class, null);
        
        adapter.addTab(getString(R.string.frame_title_question_other),
        		"other", QAOtherFragment.class, null);
        
        adapter.addTab(getString(R.string.frame_title_question_job),
        		"job", QAJobFragment.class, null);
        
        adapter.addTab(getString(R.string.frame_title_question_site),
        		"site", QASiteFragment.class, null);
	}
}