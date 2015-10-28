/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package net.oschina.app.bean;

import java.io.Serializable;

/**
 * 类名 Friend.java</br>
 * 创建日期 2014年4月23日</br>
 * @author LeonLee (http://my.oschina.net/lendylongli)</br>
 * Email lendylongli@gmail.com</br>
 * 更新时间 2014年4月23日 下午9:11:41</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 好友实体类
 */
public class Friend extends Entity implements Serializable {

	private int userid;
	private String name;
	private String face;
	private String expertise;
	private int gender;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
}
