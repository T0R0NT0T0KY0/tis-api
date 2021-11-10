package tis.project.web.components.users.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserDTO {
	private long id;
	private String email;
	private String session;
	private String token;

	public UserDTO(long id, String email, String session, String token) {
		this.id = id;
		this.email = email;
		this.session = session;
		this.token = token;
	}
}
