package tis.project.web.components.users;

import tis.project.web.services.db.PostgresqlConnection;

import java.util.Objects;

public class UserResources {
	public static boolean registration (UsersDTO newUser) {
		return Objects.nonNull(PostgresqlConnection.q(
				String.format("INSERT INTO users (user_name, nickname, active_type) " +
						"values (%s, %s, %s)", newUser.getUserName(),newUser.getNickName(), newUser.getUserActiveTypeDTO())));
	}
}
