package com.xianguo.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import com.xianguo.constant.XianguoConstant;
import com.xianguo.model.Phone;
import com.xianguo.query.PhoneQuery;
import com.xianguo.util.ImageUtil;

/**
 * 根据手机品牌展示手机列表
 * 
 * @author 
 * 
 */
public class BrandActivity extends ListActivity {

	private String searchBrand = null;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				List<Phone> phoneList = (List<Phone>) msg.obj;

				SimpleAdapter adapter = new SimpleAdapter(BrandActivity.this,
						getData(phoneList), R.layout.list_brand, new String[] {
								"logo", "title", "price", "updateTime" },
						new int[] { R.id.logo, R.id.text, R.id.price,
								R.id.updateTime });
				adapter.setViewBinder(new ViewBinder() {

					@Override
					public boolean setViewValue(View view, Object data,
							String textRepresentation) {
						if ((view instanceof ImageView)
								&& (data instanceof Bitmap)) {
							ImageView imageView = (ImageView) view;
							Bitmap bmp = (Bitmap) data;
							imageView.setImageBitmap(bmp);
							return true;
						}
						return false;
					}
				});

				setListAdapter(adapter);
				removeDialog(XianguoConstant.DIALOG_WAITING);
				break;
			}
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			PhoneQuery query = new PhoneQuery();
			List<Phone> phoneList = query.getPhoneByBrand(searchBrand);

			handler.obtainMessage(XianguoConstant.MSG_SUCCESS, phoneList)
					.sendToTarget();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.brand);

		Intent intent = this.getIntent();
		searchBrand = intent.getStringExtra("brand");

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SimpleAdapter simpleAdapter = (SimpleAdapter) arg0.getAdapter();
				Map<String, Object> map = (Map<String, Object>) simpleAdapter
						.getItem(arg2);

				Intent intent = new Intent();
				intent.putExtra("phone_id", map.get("id").toString());
				intent.setClass(BrandActivity.this, DetailModuleActivity.class);
				startActivity(intent);
			}
		});

		showDialog(XianguoConstant.DIALOG_WAITING);
		new Thread(runnable).start();
	}

	private List<Map<String, Object>> getData(List<Phone> phoneList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Phone phone : phoneList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", phone.getPhoneId());
			map.put("title", "【" + phone.getVersion() + "】" + phone.getTitle());
			map.put("price", "￥" + phone.getPrice());
			map.put("updateTime",
					"更新时间："
							+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.format(phone.getUpdateTime()));
			map.put("logo",
					ImageUtil.drawRoundedCornerBitmap(ImageUtil.getImage(
							phone.getImageName(), phone.getImageUrl())));
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
