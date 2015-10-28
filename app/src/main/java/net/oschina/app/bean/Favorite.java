/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package net.oschina.app.bean;

import java.io.Serializable;

/**
 * 类名 Favorite.java</br>
 * 创建日期 2014年4月23日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月23日 下午9:06:49</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 收藏实体类
 */
public class Favorite extends Entity implements Serializable {

	public int objid;
	public int type;
	public String title;
	public String url;
}
