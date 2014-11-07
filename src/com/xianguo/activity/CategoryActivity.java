package com.xianguo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xianguo.constant.XianguoConstant;
import com.xianguo.query.BrandQuery;

public class CategoryActivity extends ListActivity {

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				List<Map<String, Object>> dataList = (List<Map<String, Object>>) msg.obj;
				SimpleAdapter adapter = new SimpleAdapter(
						CategoryActivity.this, dataList,
						R.layout.list_category,
						new String[] { "title", "img" }, new int[] { R.id.text,
								R.id.img });
				setListAdapter(adapter);
				removeDialog(XianguoConstant.DIALOG_WAITING);
				break;

			}
		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			List<Map<String, Object>> dataList = getData();
			handler.obtainMessage(XianguoConstant.MSG_SUCCESS, dataList)
					.sendToTarget();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_category);
		showDialog(XianguoConstant.DIALOG_WAITING);

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SimpleAdapter simpleAdapter = (SimpleAdapter) arg0.getAdapter();
				Map<String, Object> map = (Map<String, Object>) simpleAdapter
						.getItem(arg2);

				Intent intent = new Intent();
				intent.putExtra("brand", map.get("title").toString());
				intent.setClass(CategoryActivity.this, BrandActivity.class);
				startActivity(intent);
			}
		});
		new Thread(runnable).start();
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		BrandQuery query = new BrandQuery();
		List<String> brandList = query.getAllBrand();

		for (String brand : brandList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", brand);
			map.put("img", R.drawable.ic_arrow);
			list.add(map);
		}

		return list;
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
