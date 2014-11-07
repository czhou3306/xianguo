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
 * ����ҳ��
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

		// �ύ��ť
		Button button = (Button) findViewById(R.id.submit);

		// ��������
		editText = (EditText) findViewById(R.id.suggest_edit);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MessageManager manager = new MessageManager();
				if (manager.submitFeedBack(editText.getText().toString())) {
					Toast.makeText(FeedBackActivity.this, "�������ύ����л����֧�֡�",
							Toast.LENGTH_LONG);
				} else {
					Toast.makeText(FeedBackActivity.this,
							"�����ύʧ�ܣ��뵽���������ǡ����ҵ���ϵ��ʽֱ����ϵ���ǡ�", Toast.LENGTH_LONG);
				}
			}
		});

	}

}
