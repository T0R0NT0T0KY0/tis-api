package tis.project.web.components.users;

import tis.project.web.services.db.PostgresqlConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResources {
	public static Object registration(UsersDTO newUser) {
		try {
			long userId = createUser(newUser);
			addUserPass(newUser, userId);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return new Object();
	}

	private static Object addUserPass(UsersDTO newUser, long userId) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("INSERT INTO d_users (user_id, password) " +
						"values (?, uuid_generate_v5(uuid_ns_x500(), ?)) returning password");
		pg.setObject(1, newUser.getUserName());
		pg.setObject(2, userId);
		ResultSet resultSet = pg.executeQuery();
		return resultSet.getLong("password");
	}

	public static long createUser(UsersDTO newUser) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("INSERT INTO users (user_name, nickname, active_type) " +
						"values (?, ?, ?) returning id");
		pg.setObject(1, newUser.getUserName());
		pg.setObject(2, newUser.getNickName());
		pg.setObject(3, newUser.getUserActiveTypeDTO());
		ResultSet resultSet = pg.executeQuery();
		return resultSet.getLong("id");
	}
}
