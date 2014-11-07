package com.xianguo.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Gallery;
import android.widget.ImageView;

import com.xianguo.adapter.ItemAdapter;
import com.xianguo.constant.XianguoConstant;
import com.xianguo.model.Phone;
import com.xianguo.query.PhoneQuery;

public class HomeActivity extends Activity {

	private Gallery jinGallery, xinGallery, reGallery;

	private Context context;

	private AutoCompleteTextView textView;

	private int itemWidth;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case XianguoConstant.MSG_SUCCESS:
				PhoneQuery query = new PhoneQuery();
				Map<String, List<Phone>> indexMap = query.getIndexData();

				ItemAdapter jinAdapter = new ItemAdapter(context,
						indexMap.get(XianguoConstant.KEY_JIN), itemWidth);
				jinGallery.setAdapter(jinAdapter);

				ItemAdapter reAdapter = new ItemAdapter(context,
						indexMap.get(XianguoConstant.KEY_RE), itemWidth);
				reGallery.setAdapter(reAdapter);

				ItemAdapter xinAdapter = new ItemAdapter(context,
						indexMap.get(XianguoConstant.KEY_XIN), itemWidth);
				xinGallery.setAdapter(xinAdapter);

				removeDialog(XianguoConstant.DIALOG_WAITING);
				break;

			}
		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			PhoneQuery query = new PhoneQuery();
			Map<String, List<Phone>> indexMap = query.getIndexData();
			handler.obtainMessage(XianguoConstant.MSG_SUCCESS, indexMap)
					.sendToTarget();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_home);

		showDialog(XianguoConstant.DIALOG_WAITING);

		jinGallery = (Gallery) findViewById(R.id.jinGallery);
		xinGallery = (Gallery) findViewById(R.id.xinGallery);
		reGallery = (Gallery) findViewById(R.id.reGallery);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int galleryWidth = dm.widthPixels - 2 * XianguoConstant.ARROW_WIDTH;
		int spellWidth = galleryWidth / (3 * 5);
		itemWidth = (galleryWidth * 4) / (3 * 5);

		jinGallery.getLayoutParams().width = galleryWidth;
		jinGallery.setSpacing(spellWidth);
		xinGallery.getLayoutParams().width = galleryWidth;
		xinGallery.setSpacing(spellWidth);
		reGallery.getLayoutParams().width = galleryWidth;
		reGallery.setSpacing(spellWidth);

		GalleryItemClickLister galleryItemClickLister = new GalleryItemClickLister();
		jinGallery.setOnItemClickListener(galleryItemClickLister);
		xinGallery.setOnItemClickListener(galleryItemClickLister);
		reGallery.setOnItemClickListener(galleryItemClickLister);

		GalleryItemSelectedListener galleryItemSelectedListener = new GalleryItemSelectedListener();
		jinGallery.setOnItemSelectedListener(galleryItemSelectedListener);
		xinGallery.setOnItemSelectedListener(galleryItemSelectedListener);
		reGallery.setOnItemSelectedListener(galleryItemSelectedListener);

		new Thread(runnable).start();

		ImageView imageView = (ImageView) findViewById(R.id.index_search_bt);
		textView = (AutoCompleteTextView) findViewById(R.id.index_search_text);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SearchTitleActivity.class);
				intent.putExtra("title", textView.getText().toString());
				startActivity(intent);
			}
		});

	}

	class GalleryItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Gallery temp = (Gallery) arg0;
			int target = arg2 == 0 ? 1 : arg2;
			int size = temp.getAdapter().getCount();
			target = target == size - 1 ? target - 1 : target;
			temp.setSelection(target);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * gallery点击监听器
	 * 
	 * @author 
	 * 
	 */
	class GalleryItemClickLister implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			ItemAdapter adapter = (ItemAdapter) arg0.getAdapter();
			Phone phone = (Phone) adapter.getItem(position);
			Intent intent = new Intent();
			intent.putExtra("phone_id", phone.getPhoneId());
			intent.setClass(HomeActivity.this, DetailModuleActivity.class);
			startActivity(intent);
		}
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
