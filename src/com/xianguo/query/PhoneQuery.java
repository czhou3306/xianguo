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

	/** ��ҳ��ʹ�õ��ֻ���Ϣ */
	private static Map<String, List<Phone>> indexMap = new HashMap<String, List<Phone>>();

	/** �ֻ���ϸ��Ϣ����ѯ�������ݶ���洢����idΪkey */
	private static Map<String, Phone> detailMap = new HashMap<String, Phone>();

	/** ����Ʒ�ƴ���ֻ���Ϣ */
	private static Map<String, List<Phone>> brandMap = new HashMap<String, List<Phone>>();

	/** ����title��ѯ�ֻ��б�Ϊ�˿��ƴ�С��ֻ����1����� */
	private static Map<String, List<Phone>> titleMap = new HashMap<String, List<Phone>>(
			1);

	/**
	 * �����ֻ���Ʒ�Ʋ�ѯ���е��ֻ��б�
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
			// ���ص��Ǹ�json�ַ���
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
	 * �����ֻ���Ʒ�Ʋ�ѯ���е��ֻ��б�
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
			// ���ص��Ǹ�json�ַ���
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
	 * �����ֻ���id��ȡ�����ֻ���Ϣ
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
			// ���ص��Ǹ�json�ַ���
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
	 * ��ȡ��ҳ��Ҫ��ʾ�����ݣ�����������Ѿ�������ֱ�ӷ���<br/>
	 * ������վȥ��ȡ
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
			// ���ص��Ǹ�json�ַ���
			String response = httpClient.execute(pairList);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean("success")) {
				JSONArray jinArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_JIN);
				// ��ȡ��Ʒ�Ƽ�����
				List<Phone> jinList = new ArrayList<Phone>();
				for (int i = 0; i < jinArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jinArray.opt(i);
					jinList.add(parse(jsonObject2));
				}
				indexMap.put(XianguoConstant.KEY_JIN, jinList);

				JSONArray reArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_RE);
				// ��ȡ������Ʒ����
				List<Phone> reList = new ArrayList<Phone>();
				for (int i = 0; i < reArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) reArray.opt(i);
					reList.add(parse(jsonObject2));
				}
				indexMap.put(XianguoConstant.KEY_RE, reList);

				JSONArray xinArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_XIN);
				// ��ȡ��Ʒ��Ʒ����
				List<Phone> xinList = new ArrayList<Phone>();
				for (int i = 0; i < xinArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) xinArray.opt(i);
					xinList.add(parse(jsonObject2));
				}
				indexMap.put(XianguoConstant.KEY_XIN, xinList);

				JSONArray peiArray = jsonObject
						.getJSONArray(XianguoConstant.KEY_XIN);
				// ��ȡ��Ʒ��Ʒ����
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
	 * ����phoneId��ȡ�ֻ�������
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
			// ���ص��Ǹ�json�ַ���
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
	 * ��json�ַ���ת���ɶ���
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
