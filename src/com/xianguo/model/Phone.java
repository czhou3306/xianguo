package com.xianguo.model;

import java.util.Date;

/**
 * �ֻ�������Ϣģ��
 * 
 * @author
 * 
 */
public class Phone {

	/** �ֻ���Ʒid */
	private String phoneId;

	/** �ֻ��汾���磺���У��۰棬ŷ�� */
	private String version;

	/** ���� */
	private String title;

	/** �۸� */
	private String price;

	/** �۸����ʱ�� */
	private Date updateTime;

	/** ͼƬ���� */
	private String imageName;

	/** ͼƬ·�� */
	private String imageUrl;

	/** �������� */
	private String taobaoUrl;

	/** ��Ʒ���� */
	private String detail;

	/** ����ָ�� */
	private String buyGuide;

	/** �ֻ��� */
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
