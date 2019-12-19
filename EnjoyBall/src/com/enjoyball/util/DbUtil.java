package com.enjoyball.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mysql.jdbc.Statement;

public class DbUtil {
	
	private static Properties dbProps = new Properties();
	
	static {
		try {
			InputStream is = DbUtil.class.getResourceAsStream("/dbinfo.properties");
			dbProps.load(is);
			Class.forName(dbProps.getProperty("db.driver"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getCon() {
		try {
			return DriverManager.getConnection(
					dbProps.getProperty("db.connectUrl"), 
					dbProps.getProperty("db.user"),
					dbProps.getProperty("db.pwd"));
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void close(ResultSet rs, PreparedStatement pstm, Connection con) {
		try {
			if(rs!=null)
				rs.close();
			if(pstm!=null)
				pstm.close();
			if(con!=null)
				con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void close(Connection con) {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param sql 要执行查询的sql语句
	 * @param cls List中的实体类
	 * @return 数据库中的全部数据，存放到一个List里面返回
	 */
	
	public static List<Object> findAll(String sql, Class cls){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		try{
			con = getCon();
			pstm = con.prepareStatement(sql);
			ResultSetMetaData rsmd = pstm.getMetaData();
			int size = rsmd.getColumnCount();
			rs = pstm.executeQuery();
			while(rs.next()){
				Object obj = cls.newInstance();
				for(int i = 0 ; i < size ; i++){
					Field f = cls.getDeclaredField(rsmd.getColumnName(i+1));
					f.setAccessible(true);
					f.set(obj, rs.getObject(i+1));
				}
				list.add(obj);
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			close(rs, pstm, con);
		}
	}
	
	public static List<Object> findAllWithWhere(String sql, Class cls,  Object[] params){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		try{
			con = getCon();
			pstm = con.prepareStatement(sql);
			if(params != null && params.length > 0){
				for(int i = 0 ; i < params.length ; i++){
					pstm.setObject(i+1, params[i]);
				}
			}
			ResultSetMetaData rsmd = pstm.getMetaData();
			int size = rsmd.getColumnCount();
			rs = pstm.executeQuery();
			while(rs.next()){
				Object obj = cls.newInstance();
				for(int i = 0 ; i < size ; i++){
					Field f = cls.getDeclaredField(rsmd.getColumnName(i+1));
					f.setAccessible(true);
					f.set(obj, rs.getObject(i+1));
				}
				list.add(obj);
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			close(rs, pstm, con);
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int executeUpdate(String sql, Object[] params){
		Connection con = null;
		PreparedStatement pstm = null;
		try{
			con = getCon();
			pstm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			if(params != null && params.length > 0){
				for(int i = 0 ; i < params.length ; i++){
					pstm.setObject(i+1, params[i]);
				}
			}
			int x = pstm.executeUpdate();
			ResultSet rs = pstm.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}
			return x;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally{
			close(null, pstm, con);
		}
	}
	
	public static boolean executeSQL(String sql){
		Connection con = null;
		PreparedStatement pstm = null;
		try{
			con = getCon();
			Statement sm1 = (Statement) con.createStatement();
			int x = sm1.executeUpdate(sql);
			System.out.println(x);
			if(x != 0)
				return true;
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			close(null, pstm, con);
		}
	}
	
}
