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
 * ����title��ѯ�ֻ��б�
 * 
 * @author 
 * 
 */
public class SearchTitleActivity extends ListActivity {

	private String searchTitle;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// �˷�����ui�߳�����
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				List<Phone> phoneList = (List<Phone>) msg.obj;

				SimpleAdapter adapter = new SimpleAdapter(
						SearchTitleActivity.this, getData(phoneList),
						R.layout.list_brand, new String[] { "logo", "title",
								"price", "updateTime" }, new int[] { R.id.logo,
								R.id.text, R.id.price, R.id.updateTime });
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
			List<Phone> phoneList = query.getPhoneByTitle(searchTitle);

			handler.obtainMessage(XianguoConstant.MSG_SUCCESS, phoneList)
					.sendToTarget();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);

		Intent intent = this.getIntent();
		searchTitle = intent.getStringExtra("title");

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map<String, Object> map = (Map<String, Object>) arg0
						.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.putExtra("phone_id", map.get("phoneId").toString());
				intent.setClass(SearchTitleActivity.this,
						DetailModuleActivity.class);
				startActivity(intent);
			}
		});
		showDialog(XianguoConstant.DIALOG_WAITING);
		new Thread(runnable).start();
	}

	/**
	 * ��ȡҪ��listview����ʾ������
	 * 
	 * @param phoneList
	 * @return
	 */
	private List<Map<String, Object>> getData(List<Phone> phoneList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Phone phone : phoneList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phoneId", phone.getPhoneId());
			map.put("title", "��" + phone.getVersion() + "��" + phone.getTitle());
			map.put("price", "��ǰ�۸񣺣�" + phone.getPrice());
			map.put("updateTime",
					"������ʱ��:"
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
			progressDialog.setMessage("���������У����Ժ�...");
			return progressDialog;
		}
		return null;
	}
}
