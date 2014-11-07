package com.xianguo.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.xianguo.basic.HttpClientTemplate;
import com.xianguo.constant.XianguoConstant;
import com.xianguo.model.Phone;

public class PhoneQuery {

	/** 首页上使用的手机信息 */
	private static Map<String, List<Phone>> indexMap = new HashMap<String, List<Phone>>();

	/** 手机详细信息，查询过的数据都会存储，以id为key */
	private static Map<String, Phone> detailMap = new HashMap<String, Phone>();

	/** 根据品牌存放手机信息 */
	private static Map<String, List<Phone>> brandMap = new HashMap<String, List<Phone>>();

	/** 根据title查询手机列表，为了控制大小，只缓存1个结果 */
	private static Map<String, List<Phone>> titleMap = new HashMap<String, List<Phone>>(
			1);

	/**
	 * 根据手机的品牌查询所有的手机列表
	 * 
	 * @param title
	 * @return
	 */
	public List<Phone> getPhoneByTitle(String title) {
		if (titleMap.get(title) != null) {
			return titleMap.get(title);
		}
		titleMap.clear();
		List<Phone> phoneList = new ArrayList<Phone>();
		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod",
					"xianguo.title.search"));
			pairList.add(new BasicNameValuePair("title", title));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				JSONArray items = jsonObject.getJSONArray("items");

				for (int i = 0; i < items.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) items.opt(i);
					phoneList.add(parse(jsonObject2));
				}
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		titleMap.put(title, phoneList);
		return phoneList;
	}

	/**
	 * 根据手机的品牌查询所有的手机列表
	 * 
	 * @param brand
	 * @return
	 */
	public List<Phone> getPhoneByBrand(String brand) {
		if (brandMap.get(brand) != null) {
			return brandMap.get(brand);
		}
		List<Phone> phoneList = new ArrayList<Phone>();
		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod",
					"xianguo.brand.search"));
			pairList.add(new BasicNameValuePair("brand", brand));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				JSONArray items = jsonObject.getJSONArray("items");

				for (int i = 0; i < items.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) items.opt(i);
					phoneList.add(parse(jsonObject2));
				}
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return phoneList;
	}

	/**
	 * 根据手机的id获取所有手机信息
	 * 
	 * @param ids
	 * @return
	 */
	public List<Phone> getPhoneByIds(List<String> ids) {

		List<Phone> phoneList = new ArrayList<Phone>();
		String unknownIds = "";
		for (String id : ids) {
			if (detailMap.get(id) != null) {
				phoneList.add(detailMap.get(id));
			} else {
				unknownIds += id + ",";
			}
		}
		if (unknownIds == "") {
			return phoneList;
		}

		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod", "xianguo.ids"));
			pairList.add(new BasicNameValuePair("ids", unknownIds));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				JSONArray items = jsonObject.getJSONArray("items");

				for (int i = 0; i < items.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) items.opt(i);
					phoneList.add(parse(jsonObject2));
				}
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return phoneList;
	}

	/**
	 * 获取首页需要显示的数据，如果缓存中已经存在则直接返回<br/>
	 * 否则到网站去获取
	 * 
	 * @return
	 */
	public Map<String, List<Phone>> getIndexData() {
		if (indexMap.size() > 0) {
			return indexMap;
		}

		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod", "xianguo.index"));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				JSONArray jinArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_JIN);
				// 获取精品推荐数据
				List<Phone> jinList = new ArrayList<Phone>();
				for (int i = 0; i < jinArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jinArray.opt(i);
					jinList.add(parse(jsonObject2));
				}
				indexMap.put(XianguoConstant.KEY_JIN, jinList);

				JSONArray reArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_RE);
				// 获取热销商品数据
				List<Phone> reList = new ArrayList<Phone>();
				for (int i = 0; i < reArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) reArray.opt(i);
					reList.add(parse(jsonObject2));
				}
				indexMap.put(XianguoConstant.KEY_RE, reList);

				JSONArray xinArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_XIN);
				// 获取新品商品数据
				List<Phone> xinList = new ArrayList<Phone>();
				for (int i = 0; i < xinArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) xinArray.opt(i);
					xinList.add(parse(jsonObject2));
				}
				indexMap.put(XianguoConstant.KEY_XIN, xinList);

				JSONArray peiArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_XIN);
				// 获取新品商品数据
				List<Phone> peiList = new ArrayList<Phone>();
				for (int i = 0; i < peiArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) peiArray.opt(i);
					peiList.add(parse(jsonObject2));
				}
				indexMap.put(XianguoConstant.KEY_PEI, peiList);
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return indexMap;
	}

	/**
	 * 根据phoneId获取手机的详情
	 * 
	 * @param phoneId
	 * @return
	 */
	public Phone getPhoneDetail(String phoneId) {
		if (detailMap.get(phoneId) != null) {
			return detailMap.get(phoneId);
		}
		try {
			HttpClientTemplate httpClient = new HttpClientTemplate();
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(new BasicNameValuePair("apiMethod", "xianguo.detail"));
			pairList.add(new BasicNameValuePair("id", phoneId));
			// 返回的是个json字符串
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.getBoolean("success")) {
				JSONObject detail = jsonObject.getJSONObject("detail");
				Phone phone = new Phone();
				phone.setPhoneId(detail.getString("phone_id"));
				phone.setDetail(detail.getString("detail"));
				phone.setVersion(detail.getString("version"));
				phone.setTitle(detail.getString("title"));
				phone.setImageName(detail.getString("image_name"));
				phone.setImageUrl(detail.getString("image_url"));
				phone.setPrice(detail.getString("price"));
				phone.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(detail.getString("update_time")));
				phone.setBuyGuide(detail.getString("buy_guide"));
				phone.setTaobaoUrl(detail.getString("taobao_url"));
				phone.setMobileNo(detail.getString("mobile_no"));
				detailMap.put(phoneId, phone);
				return phone;
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 把json字符串转换成对象
	 * 
	 * @param jsonObject
	 * @return
	 */
	private Phone parse(JSONObject jsonObject) throws Exception {
		Phone item = new Phone();
		item.setPhoneId(jsonObject.getString("phone_id"));
		item.setVersion(jsonObject.getString("version"));
		item.setImageName(jsonObject.getString("image_name"));
		item.setImageUrl(jsonObject.getString("image_url"));
		item.setPrice(jsonObject.getString("price"));

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		item.setTitle(jsonObject.getString("title"));
		item.setUpdateTime(dateFormat.parse(jsonObject.getString("update_time")));
		return item;
	}
}
