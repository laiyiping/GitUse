package com.strong.googleplay.utils;

import android.util.Log;

public class LogUtils {

	public static final int LEVEL_NONE = 0;


	public static final int LEVEL_VERBOSE = 1;


	public static final int LEVEL_DEBUG = 2;


	public static final int LEVEL_INFO = 3;


	public static final int LEVEL_WARN = 4;


	public static final int LEVEL_ERROR = 5;




	private static String mTag = "googleplay";


	private static int mDebuggable = LEVEL_ERROR;




	private static long mTimestamp = 0;






	public static void v(String msg) {

		if (mDebuggable >= LEVEL_VERBOSE) {

			Log.v(mTag, msg);

		}

	}



	public static void d(String msg) {

		if (mDebuggable >= LEVEL_DEBUG) {

			Log.d(mTag, msg);

		}

	}




	public static void i(String msg) {

		if (mDebuggable >= LEVEL_INFO) {

			Log.i(mTag, msg);

		}

	}




	public static void w(String msg) {

		if (mDebuggable >= LEVEL_WARN) {

			Log.w(mTag, msg);

		}

	}




	public static void w(Throwable tr) {

		if (mDebuggable >= LEVEL_WARN) {

			Log.w(mTag, "", tr);

		}

	}




	public static void w(String msg, Throwable tr) {

		if (mDebuggable >= LEVEL_WARN && null != msg) {

			Log.w(mTag, msg, tr);

		}

	}




	public static void e(String msg) {

		if (mDebuggable >= LEVEL_ERROR) {

			Log.e(mTag, msg);

		}

	}




	public static void e(Throwable tr) {

		if (mDebuggable >= LEVEL_ERROR) {

			Log.e(mTag, "", tr);

		}

	}



	public static void e(String msg, Throwable tr) {

		if (mDebuggable >= LEVEL_ERROR && null != msg) {

			Log.e(mTag, msg, tr);

		}

	}



	public static void elapsed(String msg) {

		long currentTime = System.currentTimeMillis();

		long elapsedTime = currentTime - mTimestamp;

		mTimestamp = currentTime;

		e("[Elapsed��" + elapsedTime + "]" + msg);

	}




}
