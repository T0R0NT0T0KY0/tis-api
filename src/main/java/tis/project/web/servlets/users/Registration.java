package tis.project.web.servlets.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.JSON_Parser;
import tis.project.web.components.users.UserActiveTypeDTO;
import tis.project.web.components.users.UserResources;
import tis.project.web.components.users.UsersDTO;
import tis.project.web.HttpError;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "registrationServlet", urlPatterns = "/api/registration")
public class Registration extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Registration.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		Object[] validateData = validateData(body);

		HttpError error = (HttpError) validateData[0];
		if (Objects.nonNull(error)) {
			resp.setContentType("json/http");
			resp.sendError(error.getErrorCode(), JSON_Parser.stringify(error.getErrorObject()));
			return;
		}

		UsersDTO user = (UsersDTO) validateData[1];

		HttpSession session = req.getSession();
		String id = session.getId();

		String[] registration = UserResources.registration(user, id);
		if (Objects.nonNull(registration[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Неизвестная ошибка",
					registration[0])));
			return;
		}

		session.setMaxInactiveInterval(-1);
		session.setAttribute("Authorization_session", registration[1]);
		session.setAttribute("userDTO", user);
		resp.addCookie(new Cookie("Authorization", registration[1]));
		resp.addCookie(new Cookie("Login", user.getEmail()));
		PrintWriter writer = resp.getWriter();
		writer.println("{ \"user_id\": " + user.getId() + "\"}");
		resp.setStatus(200);
	}


	private Object[] validateData(Map<String, String> body) {
		Object[] goList = new Object[2];
		try {
			String email = body.get("email");
			String password = body.get("password");
			String username = body.get("username");
			String nickname = body.get("nickname");
			goList[0] = validateData(username, nickname, password, email);
			goList[1] = new UsersDTO(username, nickname, email, UserActiveTypeDTO.NOT_CONFIRMED, password);
		} catch (ClassCastException | NullPointerException err) {
			goList[0] = new HttpError(400,
					new HttpError.ErrorObject("Проблема с введенными данными", err.getLocalizedMessage()));
			goList[1] = null;
		}
		return goList;
	}

	// mb with throw will be better see
	// or on js like this
	// return e1 ?? e2 ?? e3 ?? e4 ?? e5;
	private HttpError validateData(String userName, String nickName, String password, String email) {
		HttpError e1 = validateDataIsEmpty(userName, nickName, password, email);
		if (Objects.nonNull(e1)) {return e1;}

		HttpError e2 = validateEmail(email);
		if (Objects.nonNull(e2)) {return e2;}

		HttpError e3 = validatePassword(password);
		if (Objects.nonNull(e3)) {return e3;}

		HttpError e4 = validateUniqueNickname(nickName);
		if (Objects.nonNull(e4)) {return e4;}

		HttpError e5 = validateUniqueEmail(email);
		if (Objects.nonNull(e5)) {return e5;}

		return null;
	}

	private HttpError validateUniqueEmail(String email) {
		return UserResources.isNotUnique(email, "email") ?
				new HttpError(400, new HttpError.ErrorObject("Не уникальное поле email",
						"email уже занят. Введите другой, или восстановите пароль")) : null;
	}

	private HttpError validateUniqueNickname(String nickname) {
		return UserResources.isNotUnique(nickname, "nickname") ?
				new HttpError(400, new HttpError.ErrorObject("Не уникальное поле nickname",
						"Кто то уже занял такой же nickname")) : null;
	}

	private HttpError validatePassword(String password) {
		return password.length() < 6 || !password.matches(".*\\d.*\\d.*\\d.*") ||
				!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") ?
				new HttpError(400, new HttpError.ErrorObject("Легкий пароль",
						"Пароль должен быть не коротким (длиной минимум в 5 символов).\n" +
								"Состоять из минимум 4 цифр, 1 заглавной и 1 строчной латинских букв")) : null;
	}

	private HttpError validateDataIsEmpty(String userName, String nickName, String password, String email) {
		return Objects.isNull(userName) || Objects.isNull(nickName) || Objects.isNull(email) || Objects.isNull(password) ?
				new HttpError(400, new HttpError.ErrorObject("Нет данных", "Пустые поля")) :
				null;
	}

	private HttpError validateEmail(String email) {
		return !email.matches(".+@.+\\..+") ?
				new HttpError(400, new HttpError.ErrorObject("Некорректный e-mail",
						"введена некорректная электронная почта")) : null;
	}
}
