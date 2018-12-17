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

}
