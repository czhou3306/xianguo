package com.xianguo.basic;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.xianguo.constant.XianguoConstant;

public class HttpClientTemplate {

	/**
	 * ִ������
	 * 
	 * @param params
	 * @return
	 */
	public String execute(List<NameValuePair> params) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT,
					XianguoConstant.TIMEOUT_15);// ����ʱ
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.SO_TIMEOUT,
							XianguoConstant.TIMEOUT_15); // ��ȡ��ʱ
			HttpPost httpPost = new HttpPost(XianguoConstant.URL);

			String res_str = null;

			if (httpclient != null && httpPost != null && !httpPost.isAborted()) {

				// ����httpPost�������������
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

				// �ڶ�����ʹ��execute��������HTTP POST���󣬲�����HttpResponse����
				HttpResponse httpResponse = httpclient.execute(httpPost);

				if (httpResponse != null
						&& (httpResponse.getStatusLine().getStatusCode() == 200)) {

					// ��������ʹ��getEntity������÷��ؽ��
					res_str = EntityUtils.toString(httpResponse.getEntity(),
							HTTP.UTF_8);
				}

				httpPost.abort();
				httpclient.getConnectionManager().closeExpiredConnections();
			}
			return res_str;
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, "�������Ӳ�����!",e);
			return null;
		}
	}
}
