package com.xianguo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xianguo.constant.XianguoConstant;
import com.xianguo.model.Phone;
import com.xianguo.query.PhoneQuery;

public class BuyActivity extends Activity {

	private String phoneId;

	Phone phone = null;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				phone = (Phone) msg.obj;
				TextView textView = (TextView) findViewById(R.id.guideText);
				textView.setText(phone.getBuyGuide());
				removeDialog(XianguoConstant.DIALOG_WAITING);
				break;
			}
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {

			PhoneQuery query = new PhoneQuery();
			Phone phone = query.getPhoneDetail(phoneId);
			handler.obtainMessage(XianguoConstant.MSG_SUCCESS, phone)
					.sendToTarget();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy);
		Intent intent = this.getIntent();
		phoneId = intent.getStringExtra("phone_id");

		showDialog(XianguoConstant.DIALOG_WAITING);
		new Thread(runnable).start();

		Button bt_buy = (Button) findViewById(R.id.bt_buy);
		bt_buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// taobao_url不为空
				if (phone.getTaobaoUrl() != null
						&& phone.getTaobaoUrl().trim().length() > 0) {
					Intent intent = new Intent();
					intent.putExtra("url", phone.getTaobaoUrl());
					intent.setClass(BuyActivity.this, TaobaoActivity.class);
					startActivity(intent);
				} else if (phone.getMobileNo() != null
						&& phone.getMobileNo().trim().length() > 0) {
					// 手机号不为空
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + phone.getMobileNo()));
					startActivity(intent);
				} else {
					Toast.makeText(BuyActivity.this, "联系方式在“关于我们”中。",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case XianguoConstant.DIALOG_WAITING:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("下载数据中，请稍后...");
			return progressDialog;
		}
		return null;
	}
}
