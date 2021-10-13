package tis.project.web.servlets.registratinon;

import tis.project.web.JSON_Parser;
import tis.project.web.components.users.dto.UserActiveTypeDTO;
import tis.project.web.components.registration.RegisterType;
import tis.project.web.HttpError;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.isNotUnique;
import static tis.project.web.components.users.UserResources.registration;

@WebServlet(name = "registrationServlet", urlPatterns = "/api/registration")
public class Registration extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		body.forEach((s, s2) -> System.out.println(s +": "+ s2));
		Object[] validateData = validateData(body);
		HttpError error = (HttpError) validateData[0];
		if (Objects.nonNull(error)) {
			resp.setContentType("json/http");
			resp.sendError(error.getErrorCode(), JSON_Parser.stringify(error.getErrorObject()));
			return;
		}

		RegisterType user = (RegisterType) validateData[1];
		HttpSession session = req.getSession();
		String sessionId = session.getId();
		user.setSessionId(sessionId);

		Object[] list = registration(user);
		if (Objects.nonNull(list[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Неизвестная ошибка",
					((Exception) list[0]).getLocalizedMessage())));
			return;
		}

		session.setMaxInactiveInterval(-1);
		session.setAttribute("user_dto", list[1]);
		PrintWriter writer = resp.getWriter();
		writer.println("{ \"user_id\": " + user.getId() + "}");
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
			goList[1] = new RegisterType(username, nickname, email, UserActiveTypeDTO.NOT_CONFIRMED, password);
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
		return isNotUnique(email, "email") ?
				new HttpError(400, new HttpError.ErrorObject("Не уникальное поле email",
						"email уже занят. Введите другой, или восстановите пароль")) : null;
	}

	private HttpError validateUniqueNickname(String nickname) {
		return isNotUnique(nickname, "nickname") ?
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
		return Objects.isNull(userName) || userName.length() == 0 || Objects.isNull(nickName) || nickName.length() == 0 ||
				Objects.isNull(email) || email.length() == 0 || Objects.isNull(password) || password.length() == 0 ?
				new HttpError(400, new HttpError.ErrorObject("Нет данных", "Пустые поля")) :
				null;
	}

	private HttpError validateEmail(String email) {
		return !email.matches(".+@.+\\..+") ?
				new HttpError(400, new HttpError.ErrorObject("Некорректный e-mail",
						"введена некорректная электронная почта")) : null;
	}
}
