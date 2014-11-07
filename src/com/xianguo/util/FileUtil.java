package com.xianguo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.os.Environment;
import android.util.Log;

import com.xianguo.constant.XianguoConstant;

public class FileUtil {

	/** sd����·�� */
	private static String DIR_PATH;

	private FileUtil() {

	}

	static {
		// �õ���ǰ�ⲿ�洢�豸��Ŀ¼( /SDCARD )
		File sdCard = Environment.getExternalStorageDirectory();
		DIR_PATH = sdCard.getAbsolutePath() + File.separator
				+ XianguoConstant.DIR + File.separator;
		createSDDir(DIR_PATH);
	}

	/**
	 * ��SD���ϴ���Ŀ¼
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
	 * �ж�SD���ϵ��ļ��Ƿ����
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
	 * ��ȡSD�ϵ��ļ�
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
	 * ɾ���ļ�
	 */
	public static void delFile(String fileName) {
		File file = new File(DIR_PATH + fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * ɾ���ļ�
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
	 * ����һ���ļ�
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
	 * ��һ��InputStream���������д�뵽SD����
	 * 
	 * @param path
	 *            ����Ŀ¼
	 * @param fileName
	 *            �������ļ���
	 * @param input
	 *            ������
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
