package com.xianguo.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianguo.activity.DetailModuleActivity;
import com.xianguo.activity.R;
import com.xianguo.basic.DatabaseHelper;
import com.xianguo.model.Phone;
import com.xianguo.util.ImageUtil;

public class FavoriteAdapter extends BaseAdapter {

	private List<Phone> phoneList;

	private Context context;

	private DatabaseHelper databaseHelper;

	public FavoriteAdapter(Context context, List<Phone> phoneList) {
		this.context = context;
		this.phoneList = phoneList;
		this.databaseHelper = new DatabaseHelper(context);
	}

	@Override
	public int getCount() {
		if (phoneList == null) {
			return 0;
		}
		return phoneList.size();
	}

	@Override
	public Object getItem(int position) {
		if (phoneList == null) {
			return null;
		}
		return phoneList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View template = inflater.inflate(R.layout.list_favorite, null);
		if (phoneList != null) {
			ImageView logo = (ImageView) template.findViewById(R.id.logo);
			TextView text = (TextView) template.findViewById(R.id.text);
			TextView price = (TextView) template.findViewById(R.id.price);
			TextView updateTime = (TextView) template
					.findViewById(R.id.updateTime);

			Phone phone = phoneList.get(position);
			logo.setImageBitmap(ImageUtil.drawRoundedCornerBitmap(ImageUtil
					.getImage(phone.getImageName(), phone.getImageUrl())));
			text.setText("【" + phone.getVersion() + "】" + phone.getTitle());
			price.setText("￥" + phone.getPrice());
			updateTime.setText("更新时间："
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(phone
							.getUpdateTime()));
		}

		template.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("phone_id", phoneList.get(position)
						.getPhoneId());
				intent.setClass(context, DetailModuleActivity.class);
				context.startActivity(intent);
			}
		});

		template.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(context)
						.setTitle("确定要删除吗？")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 删除收藏
										databaseHelper.deleteFavorite(phoneList
												.get(position).getPhoneId());
										phoneList.remove(position);
										FavoriteAdapter.this
												.notifyDataSetChanged();
										dialog.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								}).create();
				dialog.show();
				return false;
			}
		});

		return template;
	}
}
