package com.xianguo.activity;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.xianguo.adapter.FavoriteAdapter;
import com.xianguo.basic.DatabaseHelper;
import com.xianguo.constant.XianguoConstant;
import com.xianguo.model.Phone;
import com.xianguo.query.PhoneQuery;

public class FavoriteActivity extends ListActivity {

	private DatabaseHelper databaseHelper;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				List<Phone> phoneList = (List<Phone>) msg.obj;

				FavoriteAdapter adapter = new FavoriteAdapter(
						FavoriteActivity.this, phoneList);

				setListAdapter(adapter);
				removeDialog(XianguoConstant.DIALOG_WAITING);
				break;
			}
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			List<String> ids = databaseHelper.getAllFavorite();
			PhoneQuery query = new PhoneQuery();
			List<Phone> phoneList = query.getPhoneByIds(ids);
			handler.obtainMessage(XianguoConstant.MSG_SUCCESS, phoneList)
					.sendToTarget();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_favorite);
		databaseHelper = new DatabaseHelper(this);
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
