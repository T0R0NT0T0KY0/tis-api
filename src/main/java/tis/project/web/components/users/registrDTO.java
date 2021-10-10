package tis.project.web.components.users;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
public class registrDTO {
	private long id;
	private String userName;
	private String nickName;
	private String email;
	private UserActiveTypeDTO userActiveTypeDTO;
	private Timestamp createdAt;
	private String password;

	public registrDTO(String userName, String nickName, String email, UserActiveTypeDTO userActiveTypeDTO, String password) {
		this.userName = userName;
		this.nickName = nickName;
		this.email = email;
		this.userActiveTypeDTO = userActiveTypeDTO;
		this.createdAt = new Timestamp(new Date().getTime());
		this.password = password;
	}
}
