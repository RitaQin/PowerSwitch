package com.ncr.powerswitch.utils;



/***
 * 日志级别枚举
 * @author rq185015
 *
 */
public class LogUtil {

	enum LogLevel {

		INFO("info"), DEBUG("debug"), ERROR("error");

		private LogLevel(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}
	}
	
	public static String getMethodName() {
    	StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();
        return methodName;
    }
	public static String getClassName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String className = e.getClassName();
        return className;
	}
	public static String getClassMethodName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[3];
        String className = e.getClassName();
        String methodName = e.getMethodName();
        return className + "-" + methodName;
	}

}
