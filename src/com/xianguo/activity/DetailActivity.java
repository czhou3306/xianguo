package com.xianguo.activity;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xianguo.basic.DatabaseHelper;
import com.xianguo.constant.XianguoConstant;
import com.xianguo.model.Phone;
import com.xianguo.query.PhoneQuery;
import com.xianguo.util.ImageUtil;

public class DetailActivity extends Activity {

	private DatabaseHelper databaseHelper;
	private TextView bt_sc;
	/** ��ת�����activityʱ����id */
	private String phoneId = null;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// �˷�����ui�߳�����
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				Phone phone = (Phone) msg.obj;
				TextView phoneTitle = (TextView) findViewById(R.id.phoneTitle);
				phoneTitle.setText(phone.getTitle());

				TextView phoneVersion = (TextView) findViewById(R.id.phoneVersion);
				phoneVersion.setText("�汾��" + phone.getVersion());

				TextView phonePrice = (TextView) findViewById(R.id.phonePrice);
				phonePrice.setText("�۸񣺣�" + phone.getPrice());

				TextView updateTime = (TextView) findViewById(R.id.updateTime);
				updateTime.setText("����ʱ�䣺"
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(phone.getUpdateTime()));

				TextView phoneDetail = (TextView) findViewById(R.id.phoneDetail);
				phoneDetail.setText(phone.getDetail());

				ImageView imageView = (ImageView) findViewById(R.id.image);
				imageView.setImageBitmap(ImageUtil.getImage(
						phone.getImageName(), phone.getImageUrl()));
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
		setContentView(R.layout.detail);
		databaseHelper = new DatabaseHelper(this);
		showDialog(XianguoConstant.DIALOG_WAITING);

		Intent intent = this.getIntent();
		phoneId = intent.getStringExtra("phone_id");

		bt_sc = (TextView) findViewById(R.id.bt_sc);
		bt_sc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (databaseHelper.isExist(phoneId)) {
					Toast.makeText(DetailActivity.this, "���Ѿ��ղع�����Ʒ",
							Toast.LENGTH_SHORT).show();
				} else {
					databaseHelper.saveFavorite(phoneId);
					Toast.makeText(DetailActivity.this, "��ϲ���Ѿ��ղسɹ�",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		new Thread(runnable).start();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case XianguoConstant.DIALOG_WAITING:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���������У����Ժ�...");
			return progressDialog;
		}
		return null;
	}
}
