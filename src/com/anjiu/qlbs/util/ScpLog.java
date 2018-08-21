package com.anjiu.qlbs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日志工具类
 */
public class ScpLog {

	private final static Logger stdLog = LoggerFactory.getLogger(ScpLog.class);
	private static boolean needLog4jLog = true;
	
	public static void info(String infoMsg, Object... params) {
		if (needLog4jLog) {
			stdLog.info(infoMsg, params);
		}
	}

	public static void debug(String debugMsg, Object... params) {
		if (needLog4jLog) {
			stdLog.debug(debugMsg, params);
		}
	}

	public static void error(String errMsg, Object... params) {
		if (needLog4jLog) {
			stdLog.error(errMsg, params);
		}
	}

	public static void error(String errMsg, Throwable cause) {
		if (needLog4jLog) {
			stdLog.error(errMsg, cause);
		}
	}
	
}
