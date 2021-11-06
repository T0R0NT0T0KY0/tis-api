package tis.project.web.helpers;

import tis.project.web.components.users.dto.UserDTO;

public class Validator {
	public static Object[] validateInputData(Object data) {
		Object[] goList = new Object[2];
		try {
			UserDTO dto = (UserDTO) data;
			goList[1] = dto;
		} catch (Exception e) {
			goList[0] = e.getMessage();
		}
		return goList;
	}

}
