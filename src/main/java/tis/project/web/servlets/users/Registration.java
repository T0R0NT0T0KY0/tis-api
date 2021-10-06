package tis.project.web.servlets.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.JSON_Parser;
import tis.project.web.components.users.UserActiveTypeDTO;
import tis.project.web.components.users.UserResources;
import tis.project.web.components.users.UsersDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "registrationServlet", urlPatterns = "/registration")
public class Registration extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Registration.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		String userName = body.get("userName");
		String nickName = body.get("nickName");
		String password = body.get("password");
		String email = body.get("email");
		resp.setContentType("text/json");
		body.forEach((s, s2) -> logger.info("{} {}", s, s2));
		if (Objects.isNull(userName) || Objects.isNull(nickName) || Objects.isNull(email) || Objects.isNull(password)) {
			resp.sendError(400, """
					{"err": {
					"error": "no data",\s
					"localization": "не хватает данных"}}""");
			return;
		}

		// email validate
		if (!email.matches(".+@.+\\..+")) {
			resp.sendError(400, """
					{"err": {
					"error": "incorrect email",\s
					"localization": "Некорректная электронная почта"}}""");
			return;
		}

		// password validate
		if (password.length() < 6) {
			resp.sendError(400, """
					{"err": {
					"error": "incorrect password",\s
					"description": "password length mast be more then 5",\s
					"localization": "Некорректный пароль"}
					"resolve": "Длина пароля должна быть больше 5 символов"}}""");
			return;
		}

		UsersDTO user = new UsersDTO(userName, nickName, email, UserActiveTypeDTO.NOT_CONFIRMED, password);

		if (!UserResources.isUniqueEmail(email)) {
			resp.sendError(400, """
					{"err": {
					"description": "not unique email",\s
					"localization": "почта уже используется"}}""");
			return;
		}

		String token = UserResources.registration(user);
		resp.addCookie(new Cookie("Authorization", token));
		resp.addCookie(new Cookie("Login", email));
		PrintWriter writer = resp.getWriter();
		writer.println("""
	{
        data: {
            username: 
        }
	}
			""");
		writer.close();
		resp.setStatus(200);
	}
}
