package tis.project.web.components.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private long id;
	private String email;
	private String sessionId;
	private String token;

	public UserDTO(long id, String email, String sessionId, String token) {
		this.id = id;
		this.email = email;
		this.sessionId = sessionId;
		this.token = token;
	}
}
