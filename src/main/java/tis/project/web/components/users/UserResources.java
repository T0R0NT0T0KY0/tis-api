package tis.project.web.components.users;

import tis.project.web.services.db.PostgresqlConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserResources {
	public static String[] registration(registrDTO newUser, String sessionId) {
		String[] goList = new String[2];
		try {
			newUser.setId(createUser(newUser));

			addUserPass(newUser, newUser.getId());
			addUserEmail(newUser, newUser.getId());
			goList[1] = createToken(newUser.getId(), sessionId);
		} catch (SQLException err) {
			err.printStackTrace();
			goList[0] = err.getLocalizedMessage();
		}
		return goList;
	}

	public static String login(String email, String password, String sessionId) {
		try {
			long userId = getUserIdByEmail(email);
			return verificationUserWithPass(userId, password) ? createToken(userId, sessionId) : "";
		} catch (SQLException throwable) {
			throwable.printStackTrace();
			return "";
		}
	}

	public static boolean isNotUnique(String value, String type) {
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							SELECT count(user_id)!=0 as exist from view_users
							where ? =""" + type);
			pg.setObject(1, value);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			return resultSet.getBoolean("exist");
		} catch (SQLException e) {
			return true;
		}
	}


	private static String addUserPass(registrDTO newUser, long userId) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						INSERT INTO d_users (user_id, password)
						values (?, crypt(?, gen_salt('md5')))
						returning password""");
		pg.setObject(1, userId);
		pg.setObject(2, newUser.getPassword());
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return resultSet.getString("password");
	}


	private static String addUserEmail(registrDTO newUser, long userId) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						INSERT INTO users_info (user_id, email)
						values (?, ?) returning email""");
		pg.setObject(1, userId);
		pg.setObject(2, newUser.getEmail());
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return resultSet.getString("email");
	}


	public static long createUser(registrDTO newUser) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						INSERT INTO users (user_name, nickname, active_type)
						values (?, ?, ?::active_type) returning id""");
		pg.setObject(1, newUser.getUserName());
		pg.setObject(2, newUser.getNickName());
		pg.setObject(3, newUser.getUserActiveTypeDTO().toString());
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return resultSet.getLong("id");
	}

	public static String createToken(long userId, String sessionId) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						insert into users_sessions (user_id, session_id) values (?, ?)
						on conflict (user_id)
						do update set token = default
						returning token""");
		pg.setObject(1, userId);
		pg.setObject(2, sessionId);
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return resultSet.getString("token");
	}


	public static boolean tokenVerification(String token, long userId) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						SELECT concat(user_id) = 1 as verification
						from view_d_users_sessions
						where token = ?
						and user_id = ?""");
		pg.setObject(1, token);
		pg.setObject(2, userId);
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return Objects.nonNull(resultSet.getString("verification"));
	}


	private static boolean verificationUserWithPass(long userId, String password) {
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							SELECT count(id) = 1 as verification
							from d_users
							where user_id = ?
							and password = crypt(?, password)""");
			pg.setObject(1, userId);
			pg.setObject(2, password);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			return resultSet.getBoolean("verification");
		} catch (SQLException e) {
			return false;
		}

	}

	public static long getUserIdByEmail(String email) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						SELECT user_id from users_info
						where email = ?""");
		pg.setObject(1, email);
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return Long.parseLong(resultSet.getString("user_id"));
	}
}
