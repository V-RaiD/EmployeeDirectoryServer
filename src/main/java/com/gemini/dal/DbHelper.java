package com.gemini.dal;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import ch.qos.logback.classic.db.DBHelper;

@Component
public class DbHelper {

	private String mysqlHost = "127.0.0.1";
	private String port = "3306";
	private String db = "test";
	private String user = "mdahra";
	private String password = "Gemini@123";

	private static DbHelper _instance = new DbHelper();

	private DbHelper(){};

	public static DbHelper getInstance(){
		return _instance;
	}
	public JSONArray getEmployeesData(int empId, int startId, int limit) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = (Connection) DriverManager.getConnection(String.format(
					"jdbc:mysql://%s:%s/%s"/*?user=%s&password=%s"*/, mysqlHost, port, db/*,
					user, password*/));
			JSONArray jArr = new JSONArray();
			System.out.println("EID " + empId + " SID " + startId + " LIM " + limit );
			PreparedStatement statement = (PreparedStatement)
					connection.prepareStatement("select * from EMPLOYEE where ((EMPLOYEEID = ?) or (EMPLOYEEID > ?)) order by EMPLOYEEID limit ?");
			statement.setInt(1, empId);
			statement.setInt(2, startId);
			statement.setInt(3, limit);
			// Result set get the result of the SQL query
			ResultSet resultSet = statement
					.executeQuery();
			// ResultSet is initially before the first data set
		    while (resultSet.next()) {
		      int eid = resultSet.getInt("EMPLOYEEID");
		      String name = resultSet.getString("NAME");
		      JSONObject jObj = new JSONObject();
		      jObj.put("eid", eid);
		      jObj.put("name", name);
		      jArr.put(jObj);
		    }
		    return jArr;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
