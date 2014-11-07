package com.xianguo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xianguo.manager.MessageManager;

/**
 * 关于页面
 * 
 * @author 
 * 
 */
public class FeedBackActivity extends Activity {

	private EditText editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedback);

		// 提交按钮
		Button button = (Button) findViewById(R.id.submit);

		// 建议内容
		editText = (EditText) findViewById(R.id.suggest_edit);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MessageManager manager = new MessageManager();
				if (manager.submitFeedBack(editText.getText().toString())) {
					Toast.makeText(FeedBackActivity.this, "内容已提交，感谢您的支持。",
							Toast.LENGTH_LONG);
				} else {
					Toast.makeText(FeedBackActivity.this,
							"内容提交失败，请到“关于我们”中找到联系方式直接联系我们。", Toast.LENGTH_LONG);
				}
			}
		});

	}

}
