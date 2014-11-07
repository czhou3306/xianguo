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

		public void handleMessage(Message msg) {// �˷�����ui�߳�����
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				showDialog(XianguoConstant.DIALOG_YES_NO_MESSAGE);
				break;
			case XianguoConstant.MSG_PROGRESSING:
				int index = (Integer) msg.obj;
				progressBar.setProgress(index);
				appstart_view.setText("������,���Ժ�,������ " + index + "%");
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
				connection = (HttpURLConnection) url.openConnection();// ����HTTP����
				connection.setConnectTimeout(XianguoConstant.TIMEOUT_15);// ����15�볬ʱ
				size = connection.getContentLength();// ��ȡ���ݳ���
				inputStream = connection.getInputStream();// �õ�������
				outputStream = new FileOutputStream(downloadFile);// �ļ������

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
			} catch (Exception e) {// �����쳣��������Ϣ
				e.printStackTrace();
				handler.obtainMessage(-1).sendToTarget();
			} finally {
				close();
			}
		}
	};

	private File downloadFile;// APK�ļ�

	private InputStream inputStream = null;

	private FileOutputStream outputStream = null;

	private HttpURLConnection connection = null;

	private String apk_url;

	private int size = 1;// APK���°��Ĵ�С

	private long hasRead = 0;// APK�����Ѷ�ȡ�����ֽ�

	private int index = 0;// APK���½���

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

		if (connection != null) {// ��Ҫ�ر�,�����п��������������
			connection.disconnect();
		}

		if (inputStream != null) {// ������һ��Ҫ�ر�
			try {
				inputStream.close();
			} catch (IOException e) {
				inputStream = null;
				e.printStackTrace();
			}
		}

		if (outputStream != null) {// �����һ��Ҫ�ر�
			try {
				outputStream.close();
			} catch (IOException e) {
				outputStream = null;
				e.printStackTrace();
			}
		}

	}

	/**
	 * �򿪰�װ���ļ�
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
					.setPositiveButton("��װ",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									setup();
								}
							})
					.setNegativeButton("ȡ��",
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
