package com.xianguo.activity;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.xianguo.util.ExitManager;

public class XianguoActivity extends ActivityGroup {

	private LinearLayout container = null;
	private LinearLayout menuHomelinearLayout, menuMorelinearLayout,
			menuCategorylinearLayout, menuFavoritelinearLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		container = (LinearLayout) findViewById(R.id.container);

		menuHomelinearLayout = (LinearLayout) findViewById(R.id.menu_home);
		menuHomelinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Activity forward_activity = getLocalActivityManager()
						.getActivity("HomeModule");
				container.removeAllViews();
				if (forward_activity != null) {
					View activityView = forward_activity.getWindow()
							.getDecorView();
					container.addView(activityView);
				} else {
					container
							.addView(getLocalActivityManager()
									.startActivity(
											"HomeModule",
											new Intent(XianguoActivity.this,
													HomeActivity.class)
													.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
									.getDecorView());
				}
				v.setBackgroundResource(R.drawable.home_btn_bg_s);
				menuCategorylinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuMorelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuFavoritelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
			}
		});

		menuMorelinearLayout = (LinearLayout) findViewById(R.id.menu_more);
		menuMorelinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Activity forward_activity = getLocalActivityManager()
						.getActivity("MoreModule");
				container.removeAllViews();
				if (forward_activity != null) {
					View activityView = forward_activity.getWindow()
							.getDecorView();
					container.addView(activityView);
				} else {
					container
							.addView(getLocalActivityManager()
									.startActivity(
											"MoreModule",
											new Intent(XianguoActivity.this,
													MoreActivity.class)
													.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
									.getDecorView());
				}
				v.setBackgroundResource(R.drawable.home_btn_bg_s);
				menuCategorylinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuHomelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuFavoritelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
			}
		});

		menuCategorylinearLayout = (LinearLayout) findViewById(R.id.menu_category);
		menuCategorylinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity forward_activity = getLocalActivityManager()
						.getActivity("CategoryModule");
				container.removeAllViews();
				if (forward_activity != null) {
					View activityView = forward_activity.getWindow()
							.getDecorView();
					container.addView(activityView);
				} else {
					container.addView(getLocalActivityManager().startActivity(
							"CategoryModule",
							new Intent(XianguoActivity.this,
									CategoryActivity.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView());
				}
				v.setBackgroundResource(R.drawable.home_btn_bg_s);
				menuMorelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuHomelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuFavoritelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
			}
		});

		menuFavoritelinearLayout = (LinearLayout) findViewById(R.id.menu_favorite);
		menuFavoritelinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				container.removeAllViews();
				container
						.addView(getLocalActivityManager()
								.startActivity(
										"FavoriteModule",
										new Intent(XianguoActivity.this,
												FavoriteActivity.class)
												.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
								.getDecorView());

				v.setBackgroundResource(R.drawable.home_btn_bg_s);
				menuCategorylinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuMorelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
				menuHomelinearLayout
						.setBackgroundResource(R.drawable.home_btn_bg);
			}
		});

		menuHomelinearLayout.performClick();

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (event.getRepeatCount() == 0) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						XianguoActivity.this);
				alertDialog.setTitle(XianguoActivity.this
						.getString(R.string.app_close));
				alertDialog.setPositiveButton(
						XianguoActivity.this.getString(R.string.btn_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ExitManager.getInstance().exit();
							}
						});
				alertDialog.setNegativeButton(
						XianguoActivity.this.getString(R.string.btn_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialog.show();
			}
		}
		return super.dispatchKeyEvent(event);
	}

}