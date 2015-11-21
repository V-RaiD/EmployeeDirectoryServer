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

@Component
public class DbHelper {

	private @Value("${mysql.host}") String mysqlHost;
	private @Value("${mysql.port}") String port;
	private @Value("${mysql.db}") String db;
	private @Value("${mysql.user}") String user;
	private @Value("${mysql.password}") String password;

	public JSONArray getEmployeesData(int empId, int startId, int limit) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = (Connection) DriverManager.getConnection(String.format(
					"jdbc:mysql://%s/%s?user=%s&password=%s", mysqlHost, port, db,
					user, password));
			JSONArray jArr = new JSONArray();
			PreparedStatement statement = (PreparedStatement)
					connection.prepareStatement("select * from Employee where ((eid = ?) or (eid > ?)) order by eid limit ?"
							, empId, startId, limit);
			// Result set get the result of the SQL query
			ResultSet resultSet = statement
					.executeQuery("select * from Employee where");
			// ResultSet is initially before the first data set
		    while (resultSet.next()) {
		      int eid = resultSet.getInt("eid");
		      String name = resultSet.getString("name");
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