package com.xianguo.model;

import java.util.Date;

/**
 * 手机基本信息模型
 * 
 * @author
 * 
 */
public class Phone {

	/** 手机产品id */
	private String phoneId;

	/** 手机版本，如：国行，港版，欧版 */
	private String version;

	/** 标题 */
	private String title;

	/** 价格 */
	private String price;

	/** 价格更新时间 */
	private Date updateTime;

	/** 图片名字 */
	private String imageName;

	/** 图片路径 */
	private String imageUrl;

	/** 购买链接 */
	private String taobaoUrl;

	/** 商品详情 */
	private String detail;

	/** 购买指南 */
	private String buyGuide;

	/** 手机号 */
	private String mobileNo;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuyGuide() {
		return buyGuide;
	}

	public void setBuyGuide(String buyGuide) {
		this.buyGuide = buyGuide;
	}

	public String getTaobaoUrl() {
		return taobaoUrl;
	}

	public void setTaobaoUrl(String taobaoUrl) {
		this.taobaoUrl = taobaoUrl;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

}
