package com.xianguo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.xianguo.constant.XianguoConstant;

public class ImageUtil {

	/**
	 * 先从sd卡上找文件，找不到再从网络上下载
	 * 
	 * @param imageName
	 * @param imageUrl
	 * @return
	 */
	public static Bitmap getImage(String imageName, String imageUrl) {

		File file = FileUtil.getFile(imageName);
		// sd卡上文件存在
		if (file != null) {
			return getLoacalBitmap(file);
		} else {
			Bitmap bitmap = getNetBitmap(imageUrl);
			saveToSDCard(bitmap, imageName);
			return bitmap;
		}
	}

	/**
	 * 将Bitmap对象存储为本地图片 返回SD卡存储路径
	 */
	public static String saveToSDCard(Bitmap coverBitmap, String filename) {

		String localPicPath = null;
		if (coverBitmap != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 得到输出流
			coverBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			InputStream ins = new ByteArrayInputStream(baos.toByteArray());
			File file = FileUtil.saveFile2SDCARD(filename, ins);
			localPicPath = file.getPath();
			Log.d(XianguoConstant.LOG_TAG, "ImageUtil.getLocalUrl|filename="
					+ filename + ",localPicPath=" + localPicPath);
		}
		return localPicPath;

	}

	/**
	 * 获取一个圆角图片
	 * 
	 * @param phone
	 * @return
	 */
	public static Bitmap drawRoundedCornerBitmap(Bitmap bitmap) {

		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = ((float) 150) / src_w;
		float scale_h = ((float) 150) / src_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);

		Bitmap bit = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bit);
		Paint paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setAlpha(255);

		final Rect rect = new Rect(0, 0, 150, 150);
		final RectF rectF = new RectF(rect);

		canvas.drawRoundRect(rectF, 15, 15, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return bit;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	private static Bitmap getLoacalBitmap(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 获取网络图片-通用
	 * 
	 * @param urlpath
	 * @return
	 */
	private static Bitmap getNetBitmap(String urlpath) {
		Bitmap res_bitMap = null;
		if (urlpath != null && !"".equals(urlpath)) {
			try {
				byte[] _b = getUrlBytes(urlpath);
				res_bitMap = BitmapFactory.decodeByteArray(_b, 0, _b.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res_bitMap;
	}

	/**
	 * 获取指定路径的Byte[]数据-通用
	 * 
	 * @param urlpath
	 * @return byte[]
	 * @throws Exception
	 */
	private static byte[] getUrlBytes(String urlpath) throws Exception {
		InputStream in_s = getUrlInputStream(urlpath);

		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[128];
		int len = -1;
		while ((len = in_s.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		in_s.close();
		return outstream.toByteArray();
	}

	/**
	 * 获取指定路径的InputStream数据-通用
	 * 
	 * @param urlpath
	 * @return byte[]
	 * @throws Exception
	 */
	private static InputStream getUrlInputStream(String urlpath)
			throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestMethod("GET");
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(XianguoConstant.TIMEOUT_15);// 15秒超时
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {// 返回码200等于返回成功
			InputStream inputStream = conn.getInputStream();
			return inputStream;
		}
		return null;
	}
}
