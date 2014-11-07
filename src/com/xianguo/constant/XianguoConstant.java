package com.xianguo.constant;

/**
 * 使用的常量
 * 
 * @author 
 * 
 */
public class XianguoConstant {

	/** 15s超时时间 */
	public static final int TIMEOUT_15 = 15000;

	/** 当前版本，从manifest中获取 */
	public static String VERSION ;

	/** url */
	public static final String URL = "http://wowlipin.com/mobile";

	/** log tag */
	public static final String LOG_TAG = "call.error";

	/** app使用的目录 */
	public static final String DIR = "xianguo";

	/** 精品推荐使用的key */
	public static final String KEY_JIN = "jin";

	/** 热销机型使用的key */
	public static final String KEY_RE = "re";

	/** 新品上市使用的key */
	public static final String KEY_XIN = "xin";

	/** 精美配件使用的key */
	public static final String KEY_PEI = "pei";

	/** 线程异步执行成功时的消息 */
	public static final int MSG_SUCCESS = 0;

	/** 处理中的标示 */
	public static final int MSG_PROGRESSING = 1;

	/** 处理失败的标示 */
	public static final int MSG_FAILED = -1;

	/** 退出应用时的对话框 */
	public static final int DIALOG_YES_NO_MESSAGE = 1;

	/** 没有网络应用时的对话框 */
	public static final int DIALOG_NO_NETWORK = 2;

	/** 等待对话框 */
	public static final int DIALOG_WAITING = 3;

	/** apk有更新 */
	public static final int DIALOG_APK_UPDATE = 4;

	/** 箭头的宽度 */
	public static final int ARROW_WIDTH = 40;

}
