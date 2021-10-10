package tis.project.web.components.users;

import lombok.Getter;

@Getter
public class LoginDTO {
	private final String email;
	private final String password;

	public LoginDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
