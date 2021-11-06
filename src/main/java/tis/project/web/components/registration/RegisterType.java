package tis.project.web.components.registration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tis.project.web.components.users.dto.UserActiveTypeDTO;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
public class RegisterType {
	private long id;
	private String username;
	private String nickname;
	private String email;
	private UserActiveTypeDTO userActiveTypeDTO;
	private Timestamp createdAt;
	private String password;
	private String sessionId;

	public RegisterType(String username, String nickname, String email, UserActiveTypeDTO userActiveTypeDTO, String password) {
		this.username = username;
		this.nickname = nickname;
		this.email = email;
		this.userActiveTypeDTO = userActiveTypeDTO;
		this.createdAt = new Timestamp(new Date().getTime());
		this.password = password;
	}
}
