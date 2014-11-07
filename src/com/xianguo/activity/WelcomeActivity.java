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
 * 欢迎界面
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

		// 网络不可用的情况
		if (!NetUtil.isNetworkAvailable(this)) {
			Dialog alertDialog = new AlertDialog.Builder(WelcomeActivity.this)
					.setTitle("当前网络不可用，请检查后使用！")
					// 设置对话框的标题
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {// 设置按下表示确定按钮时按钮的text，和按钮的事件监听器
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
								+ "检测到新版本", System.currentTimeMillis());

				Intent intent = new Intent(WelcomeActivity.this,
						DownloadActivity.class);
				intent.putExtra("apk_url", url);

				PendingIntent contentIntent = PendingIntent.getActivity(
						WelcomeActivity.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				notification.defaults = Notification.DEFAULT_SOUND;

				notification.setLatestEventInfo(WelcomeActivity.this,
						getText(R.string.app_name), getText(R.string.app_name)
								+ "检测到新版本", contentIntent);
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
			progressDialog.setMessage("数据加载中，请稍后...");
			return progressDialog;
		}
		return null;
	}

	/**
	 * 获取程序的版本
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getVersionName() {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
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
