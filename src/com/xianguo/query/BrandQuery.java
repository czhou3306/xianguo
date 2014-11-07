package com.xianguo.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.xianguo.basic.HttpClientTemplate;
import com.xianguo.constant.XianguoConstant;

public class BrandQuery {

	private static List<String> brandList = new ArrayList<String>();

	/**
	 * 获取所有的品牌<br/>
	 * 否则到网站去获取
	 * 
	 * @return
	 */
	public List<String> getAllBrand() {
		if (brandList.size() > 0) {
			return brandList;
		}

		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod", "xianguo.brands"));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				JSONArray array = jsonObject.getJSONArray("brands");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) array.opt(i);
					brandList.add(jsonObject2.getString("brand"));
				}
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return brandList;
	}

}
