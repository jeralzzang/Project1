package db;

import java.sql.*;
import java.util.ArrayList;
import lombok.*;

public class DbHandler {

	static final String SQLITE_FILE_PATH = "/src/main/java/db/seoul-free-wifi.db";

    public Connection sqliteDbConn() {
		Connection conn = null;
		String dirPath = System.getProperty("user.dir");
		
	    String dbConnUrl = "jdbc:sqlite:".concat(dirPath.concat(SQLITE_FILE_PATH));
		
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
	
//	public static void main(String[] args) {
//		DbHandler db = new DbHandler();
//		String dirPath = System.getProperty("user.dir");
//		
//		Connection conn = db.sqliteDbConn();
//		String sql = "select id, no, dt, key from test";
//		
//		try {
////			PreparedStatement ps = conn.prepareStatement(sql);
//			SqlCondition cond = new SqlCondition(" WHERE ID = ? ", "이");
//			ArrayList<SqlCondition> condList = new ArrayList();
//			condList.add(cond);
//			
//			ResultSet rs = db.dbGetSelect(conn, sql, condList);
//			
//			String id = rs.getString("id");
//			int no = rs.getInt("no");
//			String dt = rs.getString("dt");
//			double key = rs.getDouble("key");
//			
//			System.out.println(id);
//			System.out.println(no);
//			System.out.println(dt);
//			System.out.println(key);
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//		}
//		
//	}
	
}
