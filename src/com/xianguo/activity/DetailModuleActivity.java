package com.xianguo.activity;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailModuleActivity extends ActivityGroup {

	private LinearLayout container;
	private TextView tab_detail, tab_buy;
	private Context context;

	private ImageView goback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_module);
		context = this;
		Intent intent = this.getIntent();
		final String id = intent.getStringExtra("phone_id");

		container = (LinearLayout) findViewById(R.id.detailBody);
		tab_detail = (TextView) findViewById(R.id.tab_detail);
		tab_buy = (TextView) findViewById(R.id.tab_buy);
		goback = (ImageView) findViewById(R.id.detail_goback);

		goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DetailModuleActivity.this.finish();
			}
		});

		tab_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity forward_activity = getLocalActivityManager()
						.getActivity("DetailActivity");
				container.removeAllViews();
				if (forward_activity != null) {
					View activityView = forward_activity.getWindow()
							.getDecorView();
					container.addView(activityView);
				} else {
					Intent intent = new Intent(context, DetailActivity.class);
					intent.putExtra("phone_id", id);

					container.addView(
							getLocalActivityManager()
									.startActivity(
											"DetailActivity",
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
									.getDecorView(), LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT);
				}

				tab_detail.setBackgroundResource(R.drawable.detail_tab_select);
				tab_buy.setBackgroundResource(R.drawable.detail_tab_def);

			}
		});

		tab_buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity forward_activity = getLocalActivityManager()
						.getActivity("BuyActivity");
				container.removeAllViews();
				if (forward_activity != null) {
					View activityView = forward_activity.getWindow()
							.getDecorView();
					container.addView(activityView);
				} else {
					Intent intent = new Intent(context, BuyActivity.class);
					intent.putExtra("phone_id", id);

					container.addView(
							getLocalActivityManager()
									.startActivity(
											"BuyActivity",
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
									.getDecorView(), LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT);

				}

				tab_buy.setBackgroundResource(R.drawable.detail_tab_select);
				tab_detail.setBackgroundResource(R.drawable.detail_tab_def);

			}
		});

		tab_detail.performClick();
	}

	private void resumeActivity(Activity activity, Class<?> cls,
			String activityId) {

		boolean status = false;

		if (activity != null) {
			status = activity.getIntent().getBooleanExtra("status", false);
		}

		if (activity != null && status) {
			View activityView = activity.getWindow().getDecorView();
			if (container != null) {
				container.removeAllViews();
				container.addView(activityView);
			}
		} else {
			Intent it = new Intent(this, cls);
			container.addView(getLocalActivityManager().startActivity(
					activityId, it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
		}

	}

}
