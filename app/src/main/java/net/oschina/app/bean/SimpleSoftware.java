/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package net.oschina.app.bean;

import java.io.Serializable;

/**
 * 类名 SimpleSoftware.java</br>
 * 创建日期 2014年4月23日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月23日 下午9:19:09</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 类的描述
 */
public class SimpleSoftware extends Entity implements Serializable {

	private String name;
	private String description;
	private String url;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
