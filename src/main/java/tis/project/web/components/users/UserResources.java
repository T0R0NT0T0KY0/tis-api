package tis.project.web.components.users;

import tis.project.web.services.db.PostgresqlConnection;

import java.util.Objects;

public class UserResources {
	public static boolean registration (UsersDTO newUser) {
//		PostgresqlConnection.q(
//				String.format("INSERT INTO USERS %s", newUser.getNickName()));
		return Objects.nonNull(newUser.getNickName());
	}
}
