/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package net.oschina.app.bean;

import java.io.Serializable;

/**
 * 类名 Search.java</br>
 * 创建日期 2014年4月23日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月23日 下午9:22:48</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 搜索结果实体类
 */
public class Search extends Entity implements Serializable {

	private int objid;
	private int type;
	private String title;
	private String url;
	private String pubDate;
	private String author;

	public int getObjid() {
		return objid;
	}

	public void setObjid(int objid) {
		this.objid = objid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
