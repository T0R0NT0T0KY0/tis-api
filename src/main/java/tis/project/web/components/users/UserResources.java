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
			System.out.println(token);
			goList[1] = new UserDTO(newUser.getId(), newUser.getEmail(), newUser.getSessionId(), token);
		} catch (SQLException e) {
			e.printStackTrace();
			goList[0] = e.getMessage();
		}
		return goList;
	}

	public static Object[] login(String email, String password, String sessionId) {
		Object[] goList = new Object[2];
		try {
			long user_id = verificationUserWithPass(email, password);
			System.out.println("user_id: " + user_id);
			long userId = getUserIdByEmail(email);
			String token = createToken(userId, sessionId);
			goList[1] = new UserDTO(userId, email, sessionId, token);
		} catch (Exception e) {
			e.printStackTrace();
			goList[0] = e.getMessage();
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


	private static long verificationUserWithPass(String email, String password) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						SELECT user_id
						from d_users
						where user_id = (select id from users where email = ?)
						and password = crypt(?, password)""");
		pg.setObject(1, email);
		pg.setObject(2, password);
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return resultSet.getLong("user_id");
	}

	public static long getUserIdByEmail(String email) throws SQLException {
		PreparedStatement pg = PostgresqlConnection.getConnection()
				.prepareStatement("""
						SELECT id as user_id from users
						where email = ?""");
		pg.setObject(1, email);
		ResultSet resultSet = pg.executeQuery();
		resultSet.next();
		return resultSet.getLong("user_id");
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

	public static Object[] getUserPhoto(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select image_link from users_avatars
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("image_link");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] addUserPhoto(long userId, String imageLink) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							insert into users_avatars (user_id, image_link) 
							values (?, ?) returning image_link""");
			pg.setObject(1, userId);
			pg.setObject(2, imageLink);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("image_link");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] updateUserPhoto(long userId, String imageLink) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							update users_avatars set (image_link, updated_at)
							= (?, now()) where user_id = ? returning image_link""");
			pg.setObject(1, imageLink);
			pg.setObject(2, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("image_link");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] deleteUserPhoto(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							delete from users_avatars where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}


	public static Object[] getUsername(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select username from users
							where id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("username");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] getNickname(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select nickname from users
							where id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("nickname");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] getLivingPlace(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select living_place from users_info
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("place");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] getBirthday(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select date from users_birthdays
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("day_month");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] getAbout(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select about from users_info
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("about");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] getUserinfo(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select user_id, nickname, username, living_place, birthday, email, team, team_image_link, about
							 from view_users_information
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet rs = pg.executeQuery();
			rs.next();
			goList[1] = new UserPageDTO(rs.getLong("user_id"), rs.getString("nickname")
					, rs.getString("username"), rs.getString("team"), rs.getString("team_image_link"), rs.getString(
					"living_place"), rs.getString("birthday"), rs.getString("email"), rs.getString("about"));
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] getEmail(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select email from users
							where id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("email");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}

	public static Object[] getTeam(long userId) {
		Object[] goList = new Object[2];
		try {
			PreparedStatement pg = PostgresqlConnection.getConnection()
					.prepareStatement("""
							select team from users_teams_fans
							where user_id = ?""");
			pg.setObject(1, userId);
			ResultSet resultSet = pg.executeQuery();
			resultSet.next();
			goList[1] = resultSet.getString("team");
		} catch (SQLException e) {
			goList[0] = e;
		}
		return goList;
	}
}