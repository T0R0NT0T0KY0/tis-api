package tis.project.web.servlets.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.HttpError;
import tis.project.web.JSON_Parser;
import tis.project.web.components.users.LoginDTO;
import tis.project.web.components.users.UserResources;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "loginServlet", urlPatterns = "/api/login")
public class LogIn extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Registration.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		Object[] validateData = validateData(body);


		HttpError error = (HttpError) validateData[0];
		if (Objects.nonNull(error)) {
			resp.sendError(400);
			resp.sendError(error.getErrorCode(), JSON_Parser.stringify(error.getErrorObject()));
			return;
		}

		HttpSession session = req.getSession();
		String session_id = session.getId();

		LoginDTO loginDTO = (LoginDTO) validateData[1];
		String token = UserResources.login(loginDTO.getEmail(), loginDTO.getPassword(), session_id);
		if (token.length() < 1) {
			resp.sendError(403, JSON_Parser.stringify(new HttpError.ErrorObject("Отказано в доступе",
					"Не павильная комбинация пароль/логин")));
			return;
		}
		resp.addCookie(new Cookie("Authorization", token));
		resp.addCookie(new Cookie("Login", loginDTO.getEmail()));
		resp.setStatus(200);
	}

	private Object[] validateData(Map<String, String> body) {
		Object[] goList = new Object[2];
		try {
			String email = body.get("email");
			String password = body.get("password");
			goList[1] = new LoginDTO(email, password);
		} catch (ClassCastException | NullPointerException err) {
			goList[0] = new HttpError(400,
					new HttpError.ErrorObject("Проблема с введенными данными", err.getLocalizedMessage()));
			goList[1] = null;
		}
		return goList;
	}
}
