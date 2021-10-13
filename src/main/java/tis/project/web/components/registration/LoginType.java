package tis.project.web.components.registration;

import lombok.Getter;

@Getter
public class LoginType {
	private final String email;
	private final String password;

	public LoginType(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
