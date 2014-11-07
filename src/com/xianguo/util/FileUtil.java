package com.xianguo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.os.Environment;
import android.util.Log;

import com.xianguo.constant.XianguoConstant;

public class FileUtil {

	/** sd卡的路径 */
	private static String DIR_PATH;

	private FileUtil() {

	}

	static {
		// 得到当前外部存储设备的目录( /SDCARD )
		File sdCard = Environment.getExternalStorageDirectory();
		DIR_PATH = sdCard.getAbsolutePath() + File.separator
				+ XianguoConstant.DIR + File.separator;
		createSDDir(DIR_PATH);
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirPath
	 * @return
	 */
	private static void createSDDir(String dirPath) {
		File dir = null;
		if (dirPath == null) {
			dirPath = "";
		}

		dir = new File(dirPath);
		boolean result = dir.mkdir();
		Log.i(XianguoConstant.LOG_TAG, String.valueOf(result));

	}

	/**
	 * 判断SD卡上的文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExist(String fileName) {

		File file = new File(DIR_PATH + fileName);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取SD上的文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static File getFile(String fileName) {
		File file = new File(DIR_PATH + fileName);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 删除文件
	 */
	public static void delFile(String fileName) {
		File file = new File(DIR_PATH + fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void delFile(File file) {
		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		}
	}

	/**
	 * 创建一个文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static File createFile(String fileName) {
		try {
			File file = new File(DIR_PATH + fileName);
			file.delete();
			file.createNewFile();
			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 *            创建目录
	 * @param fileName
	 *            创建的文件名
	 * @param input
	 *            输入流
	 * @return
	 */
	public static File saveFile2SDCARD(String fileName, InputStream input) {
		try {
			File outFile = new File(DIR_PATH + fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(outFile);
			IOUtils.copy(input, fileOutputStream);
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(fileOutputStream);
			return outFile;
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
			return null;
		}
	}

}
