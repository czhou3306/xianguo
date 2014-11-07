/**
 * 
 */
package com.xianguo.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

import com.xianguo.model.Phone;
import com.xianguo.util.ImageUtil;

/**
 * @author 
 * 
 */
public class ItemAdapter extends BaseAdapter {

	private List<Phone> list;
	LayoutInflater inflater;
	private Context mContext;

	private int itemWidth;

	public ItemAdapter(Context context, List<Phone> list, int width) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.itemWidth = width;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView i = new ImageView(mContext);
		Bitmap bit = getRoundedCornerBitmap(list.get(position));

		i.setImageBitmap(bit);

		i.setAdjustViewBounds(true);
		i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		return i;
	}

	private Bitmap getRoundedCornerBitmap(Phone phone) {
		Bitmap bitmap = ImageUtil.getImage(phone.getImageName(),
				phone.getImageUrl());

		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = ((float) itemWidth) / src_w;
		float scale_h = ((float) itemWidth) / src_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);

		Bitmap bit = Bitmap.createBitmap(itemWidth, itemWidth + 100,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bit);
		Paint paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setAlpha(255);

		final Rect rect = new Rect(0, 0, itemWidth, itemWidth);
		final RectF rectF = new RectF(rect);

		canvas.drawRoundRect(rectF, 15, 15, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		paint.setTextSize(25);
		String text = "¡¾" + phone.getVersion() + "¡¿" + phone.getTitle();
		paint.setColor(Color.BLACK);
		canvas.drawText(text.substring(0, 7) + "...", 0, bitmap.getHeight()
				+ paint.getTextSize() + 10, paint);
		paint.setColor(Color.RED);
		canvas.drawText("£¤" + phone.getPrice() + "Ôª", 0, bitmap.getHeight() + 2
				* (paint.getTextSize() + 10), paint);

		return bit;
	}

}
