package com.xianguo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

import com.xianguo.constant.XianguoConstant;
import com.xianguo.query.MessageQuery;

/**
 * 关于页面
 * 
 * @author
 * 
 */
public class AboutActivity extends Activity {

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				String aboutMsg = (String) msg.obj;
				TextView aboutus = (TextView) findViewById(R.id.aboutus);
				aboutus.setText(aboutMsg);
				removeDialog(XianguoConstant.DIALOG_WAITING);
				break;
			}
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			MessageQuery query = new MessageQuery();
			String aboutus = query.getAboutUs();
			handler.obtainMessage(XianguoConstant.MSG_SUCCESS, aboutus)
					.sendToTarget();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);

		showDialog(XianguoConstant.DIALOG_WAITING);
		new Thread(runnable).start();
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
