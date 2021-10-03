package tis.project.web.components.users;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class UsersDTO {
	private String userName;
	private String nickName;
	private String email;
	private UserActiveTypeDTO userActiveTypeDTO;
	private Timestamp createdAt;

	public UsersDTO(String userName, String nickName, String email, UserActiveTypeDTO userActiveTypeDTO) {
		this.userName = userName;
		this.nickName = nickName;
		this.email = email;
		this.userActiveTypeDTO = userActiveTypeDTO;
		this.createdAt = new Timestamp(new Date().getTime());
	}

	@Override
	public String toString() {

		return "UsersDTO{" +
				"userName='" + userName + '\'' +
				", nickName='" + nickName + '\'' +
				", email='" + email + '\'' +
				", userActiveTypeDTO=" + userActiveTypeDTO +
				", createdAt=" + createdAt +
				'}';
	}
}
