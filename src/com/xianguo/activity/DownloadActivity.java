package com.xianguo.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xianguo.constant.XianguoConstant;
import com.xianguo.util.FileUtil;

public class DownloadActivity extends Activity {

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				showDialog(XianguoConstant.DIALOG_YES_NO_MESSAGE);
				break;
			case XianguoConstant.MSG_PROGRESSING:
				int index = (Integer) msg.obj;
				progressBar.setProgress(index);
				appstart_view.setText("下载中,请稍候,已下载 " + index + "%");
				break;
			case XianguoConstant.MSG_FAILED:
				FileUtil.delFile(downloadFile);
				Toast.makeText(_context, R.string.download_retry,
						Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED, intent_appstart);
				// finish();
				break;
			default:
				break;
			}
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				downloadFile = FileUtil.createFile("tmp.apk");

				URL url = new URL(apk_url);
				connection = (HttpURLConnection) url.openConnection();// 开启HTTP连接
				connection.setConnectTimeout(XianguoConstant.TIMEOUT_15);// 设置15秒超时
				size = connection.getContentLength();// 获取内容长度
				inputStream = connection.getInputStream();// 得到输入流
				outputStream = new FileOutputStream(downloadFile);// 文件输出流

				byte[] buffer = new byte[1024];

				while (true) {
					int numread = inputStream.read(buffer);
					if (numread <= 0) {
						break;
					}
					outputStream.write(buffer, 0, numread);
					hasRead += numread;
					index = (int) (hasRead * 100) / size;
					handler.obtainMessage(XianguoConstant.MSG_PROGRESSING,
							index).sendToTarget();
				}
				handler.obtainMessage(XianguoConstant.MSG_SUCCESS, index)
						.sendToTarget();

				close();
			} catch (Exception e) {// 下载异常，发送消息
				e.printStackTrace();
				handler.obtainMessage(-1).sendToTarget();
			} finally {
				close();
			}
		}
	};

	private File downloadFile;// APK文件

	private InputStream inputStream = null;

	private FileOutputStream outputStream = null;

	private HttpURLConnection connection = null;

	private String apk_url;

	private int size = 1;// APK更新包的大小

	private long hasRead = 0;// APK更新已读取多少字节

	private int index = 0;// APK更新进度

	private ProgressBar progressBar = null;

	public TextView appstart_view;

	private Context _context;

	private Intent intent_appstart;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);

		Intent intent = this.getIntent();

		apk_url = intent.getStringExtra("apk_url");

		_context = this;

		appstart_view = (TextView) findViewById(R.id.appstart_text);

		progressBar = (ProgressBar) findViewById(R.id.downloadBar);

		intent_appstart = new Intent(this, WelcomeActivity.class);

		new Thread(runnable).start();
	}

	private void close() {

		if (connection != null) {// 需要关闭,否则有可能造成网络阻塞
			connection.disconnect();
		}

		if (inputStream != null) {// 输入流一定要关闭
			try {
				inputStream.close();
			} catch (IOException e) {
				inputStream = null;
				e.printStackTrace();
			}
		}

		if (outputStream != null) {// 输出流一定要关闭
			try {
				outputStream.close();
			} catch (IOException e) {
				outputStream = null;
				e.printStackTrace();
			}
		}

	}

	/**
	 * 打开安装包文件
	 * 
	 * @param f
	 */
	private void setup() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(downloadFile), type);
		startActivity(intent);
		finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case XianguoConstant.DIALOG_YES_NO_MESSAGE:
			return new AlertDialog.Builder(_context)
					.setTitle(R.string.download_finish)
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("安装",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									setup();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									finish();
								}
							}).create();

		}

		return null;
	}
}
