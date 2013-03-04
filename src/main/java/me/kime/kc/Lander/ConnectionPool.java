package me.Kime.KC.Lander;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

public class ConnectionPool {

	private int maxConnectNum;

	private String connString;

	private String username;

	private String password;

	private LinkedList<Connection> connectPool;

	public ConnectionPool(String connString, String username, String password, int max) {
		connectPool = new LinkedList<Connection>();
		
		this.connString = connString;
		this.username = username;
		this.password = password;
		this.maxConnectNum = max;
		
		init();

	}

	private Connection getConnectionFromDatabase() {
		Connection trueConn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			trueConn = DriverManager.getConnection(connString, username, password);
		} catch (Exception ex) {
			System.out.println("数据连接出错了:" + ex.toString());
		}
		return trueConn;
	}

	// 这里建立所有的连接;
	private void init() {
		for (int i = 0; i < maxConnectNum; i++) {
			connectPool.addLast(getConnectionFromDatabase());
		}
	}

	// 从连接池中取得一个可用的连接
	public synchronized Connection getConnection() {
		while(connectPool.size() == 0){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				System.out.println("连接全部用光,这里sleep出错了.");
			}			
		}
		Connection conn = connectPool.removeFirst();
		/*try {
			if(!conn.isValid(1)){
				return getConnectionFromDatabase();
			}
		} catch (SQLException e) {			
			return getConnectionFromDatabase();
		}*/
		return conn;
	}

	// 提供给外部程序调用,不用的连接放回连接池当中...
	public synchronized void release(Connection conn) {
		if(conn != null){
			connectPool.addLast(conn);
		}
		
	}
}
