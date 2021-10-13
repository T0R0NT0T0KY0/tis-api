package tis.project.web.components.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPageDTO {
	private  long user_id;
	private String place;
	private String birthday;
	private String email;
	private long predictions_experience;
	private String team_name;
	private String team_image_link;

	public UserPageDTO(long user_id, String place, String birthday, String email, long predictions_experience,
	                   String team_name, String team_image_link) {
		this.user_id = user_id;
		this.place = place;
		this.birthday = birthday;
		this.email = email;
		this.predictions_experience = predictions_experience;
		this.team_name = team_name;
		this.team_image_link = team_image_link;
	}
}
