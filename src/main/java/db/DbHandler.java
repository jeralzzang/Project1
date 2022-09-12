package db;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import lombok.*;
import util.ApiCallUtil;

public class DbHandler {

	static final String SQLITE_FILE_NAME = "db/seoul-free-wifi.db";
	
	String getCurrentDirectory() {
		return this.getClass().getClassLoader().getResource("").getPath();
	}
	
    public Connection sqliteDbConn() {
		Connection conn = null;
		String path = getCurrentDirectory();
		String dbConnUrl = "jdbc:sqlite:".concat(path.concat(SQLITE_FILE_NAME));
		
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(dbConnUrl);
			return conn;
			
		}catch(Exception E) {
			E.getStackTrace();
			return null;
		}
		
	}
    
    public ResultSet dbGetSelect(Connection conn, String sql) {
    	ResultSet rs = null;
    	try {
    		PreparedStatement ps = conn.prepareStatement(sql);
    		rs = ps.executeQuery();
    		
    	}catch(Exception E) {
    		E.getStackTrace();
    		System.out.println("select error   " + E.getMessage());
    	}
    	
    	return rs;
    }
    
	public ResultSet dbGetSelect(Connection conn, String sql, ArrayList<SqlCondition> cond) {
		ResultSet rs = null;
		
		//쿼리에 조건문 넣기
		if(!cond.isEmpty()) {
			int idx = 1;
			try {
				for(SqlCondition item : cond) {
					   sql = sql.concat(item.getCond());
					}
				PreparedStatement ps = conn.prepareStatement(sql);
				
				for(SqlCondition item : cond) {
					   ps.setString(idx++, item.getValue());
					}
				rs = ps.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return rs;
	}
	
	public int dbSetInsert(Connection conn, String sql) {
		
		if(conn==null || sql == null) {
			return -1;
		}
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			return ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int dbSetInsert(Connection conn, PreparedStatement ps) {
		
		if(conn==null || ps == null) {
			return -1;
		}
		
		try {
			return ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}	

	
}
