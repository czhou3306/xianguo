package com.xianguo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.xianguo.constant.XianguoConstant;
import com.xianguo.query.MessageQuery;
import com.xianguo.util.NetUtil;

/**
 * ��ӭ����
 * 
 * @author
 * 
 */
public class WelcomeActivity extends Activity {

	private static int NOTIFICATIONS_ID = 1;
	private NotificationManager notificationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);

		XianguoConstant.VERSION = getVersionName();

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// ���粻���õ����
		if (!NetUtil.isNetworkAvailable(this)) {
			Dialog alertDialog = new AlertDialog.Builder(WelcomeActivity.this)
					.setTitle("��ǰ���粻���ã������ʹ�ã�")
					// ���öԻ���ı���
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {// ���ð��±�ʾȷ����ťʱ��ť��text���Ͱ�ť���¼�������
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									WelcomeActivity.this.finish();
								}
							}).create();
			alertDialog.show();
		} else {
			new Thread(runnable).start();
		}
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			MessageQuery query = new MessageQuery();
			String url = query.checkVersion();
			if (url != null && url.trim() != "") {

				Notification notification = new Notification(
						R.drawable.ic_launcher, getText(R.string.app_name)
								+ "��⵽�°汾", System.currentTimeMillis());

				Intent intent = new Intent(WelcomeActivity.this,
						DownloadActivity.class);
				intent.putExtra("apk_url", url);

				PendingIntent contentIntent = PendingIntent.getActivity(
						WelcomeActivity.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				notification.defaults = Notification.DEFAULT_SOUND;

				notification.setLatestEventInfo(WelcomeActivity.this,
						getText(R.string.app_name), getText(R.string.app_name)
								+ "��⵽�°汾", contentIntent);
				notificationManager.notify(NOTIFICATIONS_ID, notification);
			}
			handler.obtainMessage(2).sendToTarget();
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				Intent intent = new Intent(WelcomeActivity.this,
						XianguoActivity.class);
				startActivity(intent);
				// removeDialog(XianguoConstant.DIALOG_WAITING);
				WelcomeActivity.this.finish();
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case XianguoConstant.DIALOG_WAITING:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ݼ����У����Ժ�...");
			return progressDialog;
		}
		return null;
	}

	/**
	 * ��ȡ����İ汾
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getVersionName() {
		try {
			// ��ȡpackagemanager��ʵ��
			PackageManager packageManager = getPackageManager();
			// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
			return null;
		}
	}
}
