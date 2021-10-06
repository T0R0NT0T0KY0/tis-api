package tis.project.web.services.db;

import tis.project.web.Envs;

import java.sql.*;

public class PostgresqlConnection {
	private static Connection connection;
	static {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(
					Envs.getENV("db_pg_connection_url"),
					Envs.getENV("db_pg_user"),
					Envs.getENV("db_pg_password"));
		} catch (SQLException | ClassNotFoundException throwable) {
			throwable.printStackTrace();
			System.exit(1);
		}
	}

	public static Connection getConnection () {
		return connection;
	}
}
