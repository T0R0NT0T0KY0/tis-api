package tis.project.web.services.db;

import tis.project.web.Envs;

import java.sql.*;

public class PostgresqlConnection {
	private static Connection connection;
	static {
		try {
			connection = DriverManager.getConnection(
					Envs.getENV("db_pg_connection_url"),
					Envs.getENV("db_pg_user"),
					Envs.getENV("db_pg_password"));
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	public static PreparedStatement q (String statement) {
		try {
			return connection.prepareStatement(statement);
		} catch (SQLException troubles) {
			troubles.printStackTrace();
		}
		return null;
	}
}
