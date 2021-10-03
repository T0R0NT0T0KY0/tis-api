package tis.project.web.components.users;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UsersDTO {
	private String userName;
	private String nickName;
	private String email;
	private UserActiveTypeDTO userActiveTypeDTO;
	private Date createdAt;

	public UsersDTO(String userName, String nickName, String email, UserActiveTypeDTO userActiveTypeDTO) {
		this.userName = userName;
		this.nickName = nickName;
		this.email = email;
		this.userActiveTypeDTO = userActiveTypeDTO;
		this.createdAt = new Date();
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
