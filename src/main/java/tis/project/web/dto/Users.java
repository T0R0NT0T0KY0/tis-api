package tis.project.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Users {
	private final long id;
	private String userName;
	private String nickName;
	private String email;
	private UserActiveType userActiveType;
	private Date createdAt;

	public Users(long id, String userName, String nickName, String email, UserActiveType userActiveType) {
		this.id = id;
		this.userName = userName;
		this.nickName = nickName;
		this.email = email;
		this.userActiveType = userActiveType;
		this.createdAt = new Date();
	}
}
