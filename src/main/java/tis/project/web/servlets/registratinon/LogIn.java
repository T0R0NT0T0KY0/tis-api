package tis.project.web.servlets.registratinon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.HttpError;
import tis.project.web.JSON_Parser;
import tis.project.web.components.registration.LoginType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.login;

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
		String sessionId = session.getId();

		LoginType loginType = (LoginType) validateData[1];
		Object[] list = login(loginType.getEmail(), loginType.getPassword(), sessionId);
		if (Objects.nonNull(list[0])) {
			resp.sendError(403, JSON_Parser.stringify(new HttpError.ErrorObject("Отказано в доступе",
					"Не павильная комбинация пароль/логин")));
			return;
		}
		session.setMaxInactiveInterval(-1);
		session.setAttribute("user_dto", list[1]);
		resp.setStatus(200);
	}

	private Object[] validateData(Map<String, String> body) {
		Object[] goList = new Object[2];
		try {
			String email = body.get("email");
			String password = body.get("password");
			goList[1] = new LoginType(email, password);
		} catch (ClassCastException | NullPointerException err) {
			goList[0] = new HttpError(400,
					new HttpError.ErrorObject("Проблема с введенными данными", err.getLocalizedMessage()));
			goList[1] = null;
		}
		return goList;
	}
}
