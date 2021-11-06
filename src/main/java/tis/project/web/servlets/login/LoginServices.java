package tis.project.web.servlets.login;

import tis.project.web.HttpError;
import tis.project.web.components.registration.LoginType;

import java.util.Map;

public class LoginServices {

	protected static Object[] validateLoginData(Map<String, String> body) {
		Object[] goList = new Object[2];
		try {
			String email = body.get("email");
			String password = body.get("password");
			goList[1] = new LoginType(email, password);
		} catch (ClassCastException | NullPointerException err) {
			goList[0] = new HttpError(400,
					new HttpError.ErrorObject("Bed request", err.getMessage()));
			goList[1] = null;
		}
		return goList;
	}
}
