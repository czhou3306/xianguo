package com.xianguo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TableRow;
import android.widget.Toast;

import com.xianguo.constant.XianguoConstant;
import com.xianguo.query.MessageQuery;

public class MoreActivity extends Activity {

	private static int NOTIFICATIONS_ID = 1;
	private NotificationManager notificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_more);

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// ��������
		TableRow aboutUs = (TableRow) findViewById(R.id.more_page_row1);
		aboutUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});

		// �������
		TableRow feedBack = (TableRow) findViewById(R.id.more_page_row2);
		feedBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, FeedBackActivity.class);
				startActivity(intent);
			}
		});

		// ������
		TableRow checkUpdate = (TableRow) findViewById(R.id.more_page_row3);
		checkUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(XianguoConstant.DIALOG_WAITING);

				MessageQuery query = new MessageQuery();
				String url = query.checkVersion();
				if (url != null && url.trim() != "") {

					Notification notification = new Notification(
							R.drawable.ic_launcher, getText(R.string.app_name)
									+ "��⵽�°汾", System.currentTimeMillis());

					Intent intent = new Intent(MoreActivity.this,
							DownloadActivity.class);
					intent.putExtra("apk_url", url);

					PendingIntent contentIntent = PendingIntent.getActivity(
							MoreActivity.this, 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					notification.defaults = Notification.DEFAULT_SOUND;

					notification.setLatestEventInfo(MoreActivity.this,
							getText(R.string.app_name),
							getText(R.string.app_name) + "��⵽�°汾",
							contentIntent);
					notificationManager.notify(NOTIFICATIONS_ID, notification);
				} else {
					Toast.makeText(MoreActivity.this, "�Ѿ������°汾",
							Toast.LENGTH_LONG).show();
				}
				removeDialog(XianguoConstant.DIALOG_WAITING);
			}
		});

		// ʹ���f��
		TableRow useGuide = (TableRow) findViewById(R.id.more_page_row4);
		useGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, UseGuideActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case XianguoConstant.DIALOG_WAITING:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("����У����Ժ�...");
			return progressDialog;
		}
		return null;
	}
}
