package tis.project.web.components.users;

import tis.project.web.components.registration.RegisterType;
import tis.project.web.components.users.dto.UserDTO;
import tis.project.web.components.users.dto.UserPageDTO;
import tis.project.web.services.db.PostgresqlConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserResources {
	public static Object[] registration(RegisterType newUser) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("call insert_data_prc(?, ?, ?, ?, ?)");
			pg.setObject(1, newUser.getUsername());
			pg.setObject(2, newUser.getNickname());
			pg.setObject(3, newUser.getPassword());
			pg.setObject(4, newUser.getEmail());
			pg.setObject(5, newUser.getSessionId());
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			newUser.setId(resultSet.getLong("user_id"));
			String token = resultSet.getString("session_token");
			goList[1] = new UserDTO(newUser.getId(), newUser.getEmail(), newUser.getSessionId(), token);
		} catch (SQLException e) {
			e.printStackTrace();
			goList[0] = e.getLocalizedMessage();
		}
		return goList;
	}

	public static Object[] login(String email, String password, String sessionId) {
		Object[] goList = new Object[2];
		try {
			Object[] userId = getUserIdByEmail(email);
			if (Objects.nonNull(userId[0])) throw (Exception) userId[0];
			Long id = (Long) userId[1];
			String token = verificationUserWithPass(id, password) ? createToken(id, sessionId) : "";
			goList[1] = new UserDTO(id, email, sessionId, token);
		} catch (Exception e) {
			e.printStackTrace();
			goList[0] = e.getLocalizedMessage();
		}
		return goList;
	}

	public static Boolean isAvailableSession(String sessionId, Object user) {
		try {
			UserDTO userDTO = (UserDTO) user;
			return sessionId.equals(userDTO.getSessionId()) && tokenVerification(userDTO.getToken(),
					userDTO.getSessionId(), userDTO.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isNotUnique(String value, String type) {
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							SELECT count(id)!=0 as exist from users
							where ? =""" + type);
			pg.setObject(1, value);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			return resultSet.getBoolean("exist");
		} catch (SQLException e) {
			return true;
		}
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


	public static boolean tokenVerification(String token, String sessionId, long userId) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						SELECT count(user_id) = 1 as verification
						from view_d_users_sessions
						where token = ?
						and session_id = ?
						and user_id = ?""");
		pg.setObject(1, token);
		pg.setObject(2, sessionId);
		pg.setObject(3, userId);
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

	public static Object[] getUserIdByEmail(String email) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							SELECT id as user_id from users
							where email = ?""");
			pg.setObject(1, email);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = Long.parseLong(resultSet.getString("user_id"));
		} catch (SQLException e) {
			e.printStackTrace();
			goList[0] = e;
		}
		return goList;
	}

	public static String getUserAvatar(long userId) {
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							SELECT image_link from users_avatars
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			return resultSet.getString("image_link");
		} catch (SQLException e) {
			return "";
		}
	}

	public static Object[] getUserInformationById(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							SELECT user_id, place, birthday, email, predictions_experience, team_name, team_image_link
								from users_information
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = new UserPageDTO(resultSet.getLong("user_id"),
					resultSet.getString("place"),
					resultSet.getString("birthday"),
					resultSet.getString("email"),
					resultSet.getLong("predictions_experience"),
					resultSet.getString("team_name"),
					resultSet.getString("team_image_link"));
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}
}