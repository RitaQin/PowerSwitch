package com.ncr.powerswitch.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.DriverManager;
import java.io.PrintWriter;

public class BaseDataSource implements DataSource {

	private final static Log log = LogFactory.getLog(BaseDataSource.class);
	private static String driverClassName;
	private static String url;
	private static String username;
	private static String password;

	public static String getDriverClassName() {
		return driverClassName;
	}

	// 连接池
	private static LinkedList<Connection> pool = (LinkedList<Connection>) Collections
			.synchronizedList(new LinkedList<Connection>());
	private static BaseDataSource instance = new BaseDataSource();

	static {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			log.error("找不到驱动类！", e);
		}
	}

	private BaseDataSource() {
	}

	/**
	 * 获取数据源单例
	 * 
	 * @return 数据源单例
	 */
	public BaseDataSource instance() {
		if (instance == null)
			instance = new BaseDataSource();
		return instance;
	}

	/**
	 * 获取一个数据库连接
	 * 
	 * @return 一个数据库连接
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		synchronized (pool) {
			if (pool.size() > 0)
				return pool.removeFirst();
			else
				return makeConnection();
		}
	}

	/**
	 * 连接归池
	 * 
	 * @param conn
	 */
	public static void freeConnection(Connection conn) {
		pool.addLast(conn);
	}

	private Connection makeConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	public void setLoginTimeout(int seconds) throws SQLException {

	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String getUrl() {
		return url;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setDriverClassName(String driverClassName) {
		BaseDataSource.driverClassName = driverClassName;
	}

	public static void setUrl(String url) {
		BaseDataSource.url = url;
	}

	public static void setUsername(String username) {
		BaseDataSource.username = username;
	}

	public static void setPassword(String password) {
		BaseDataSource.password = password;
	}
}