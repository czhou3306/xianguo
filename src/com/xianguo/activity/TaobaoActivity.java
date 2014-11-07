package com.xianguo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

/**
 * taobao的购买页面
 * 
 * @author
 * 
 */
public class TaobaoActivity extends Activity {

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.taobaoview);

		Intent intent = this.getIntent();
		String url = intent.getStringExtra("url");

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webHtml(url);
	}

	/**
	 * 
	 * 直接网页显示
	 */
	private void webHtml(String url) {
		try {
			webView.loadUrl(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
