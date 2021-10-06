package tis.project.web.servlets.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.JSON_Parser;
import tis.project.web.components.users.UserResources;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LogIn extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Registration.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		String email = body.get("email");
		String password = body.get("password");
		logger.info("email: {}, pass: {}", email, password);
		if (Objects.isNull(email) || Objects.isNull(password)) {
			resp.sendError(400);
			return;
		}
		String token = UserResources.login(email, password);
		if (token.length()<1) {
			resp.sendError(403, """
					{"err": {
					"description": "invalid email/password combination",\s
					"localization": "не павильная комбинация пароль/логин"}}""");
			return;
		}
		resp.addCookie(new Cookie("Authorization", token));
		resp.addCookie(new Cookie("Login", email));
		resp.setStatus(200);
	}
}
