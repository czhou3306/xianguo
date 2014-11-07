package com.xianguo.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.xianguo.basic.HttpClientTemplate;
import com.xianguo.constant.XianguoConstant;

public class MessageQuery {

	private static String aboutus = null;

	private static String useGuide = null;

	/**
	 * 检查程序版本，如果有新版本则返回下载的地址
	 * 
	 * @return
	 */
	public String checkVersion() {

		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod",
					"xianguo.checkversion"));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				String version = jsonObject.getString("version");
				if (!version.equals(XianguoConstant.VERSION)) {
					return jsonObject.getString("appurl");
				}
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取关于我们信息
	 * 
	 * @return
	 */
	public String getAboutUs() {
		if (aboutus != null && aboutus.trim() != "") {
			return aboutus;
		}

		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod", "xianguo.aboutus"));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				String msg = jsonObject.getString("msg");
				aboutus = msg;
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return aboutus;
	}

	/**
	 * 获取关于我们信息
	 * 
	 * @return
	 */
	public String getUseGuide() {
		if (useGuide != null && useGuide.trim() != "") {
			return useGuide;
		}

		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod",
					"xianguo.use.guide"));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				String msg = jsonObject.getString("msg");
				useGuide = msg;
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return useGuide;
	}
}
