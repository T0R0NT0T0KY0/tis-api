package tis.project.web.components.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPageDTO {
	private  long user_id;
	private String nickname;
	private String username;
	private String team;
	private String team_image_link;
	private String living_place;
	private String birthday;
	private String email;
	private String about;

	public UserPageDTO(long user_id, String nickname, String username, String team, String team_image_link,
	                   String living_place, String birthday, String email, String about) {
		this.user_id = user_id;
		this.nickname = nickname;
		this.username = username;
		this.team = team;
		this.team_image_link = team_image_link;
		this.living_place = living_place;
		this.birthday = birthday;
		this.email = email;
		this.about = about;
	}
}
