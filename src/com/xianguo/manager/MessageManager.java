package com.xianguo.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.xianguo.basic.HttpClientTemplate;
import com.xianguo.constant.XianguoConstant;

public class MessageManager {

	/**
	 * ��ȡ����������Ϣ
	 * 
	 * @return
	 */
	public boolean submitFeedBack(String content) {

		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod", "xianguo.feedback"));
			pairList.add(new BasicNameValuePair("content", content));

			// ���ص��Ǹ�json�ַ���
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				return true;
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return false;
	}
}
