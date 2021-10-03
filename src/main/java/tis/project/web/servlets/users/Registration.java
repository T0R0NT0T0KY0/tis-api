package tis.project.web.servlets.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import kotlin.text.Regex;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.components.users.UserActiveTypeDTO;
import tis.project.web.components.users.UserResources;
import tis.project.web.components.users.UsersDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "loginServlet", urlPatterns = "/registration")
public class Registration extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Registration.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		String userName = body.get("userName");
		String nickName = body.get("nickName");
		String email = body.get("email");
		String pas = body.get("password");
		resp.setContentType("text/json");

		if (Objects.isNull(userName) || Objects.isNull(nickName) || Objects.isNull(email) || Objects.isNull(pas)) {
			resp.sendError(400);
			return;
		}
		// email validate
		if (!email.matches(".+@.+\\..+")) {
			resp.sendError(400, """
					"err": {
					"description": "incorrect email",\s
					"localization": "Некорректная электронная почта"}""");
			return;
		}

		// password validate
		if (pas.length() < 5) {
			resp.sendError(400, """
					"err": {
					"description": "incorrect password",\s
					"description": "incorrect password",\s
					"resolve": "Длина пароля должна быть больше 5 символов"}""");
			return;
		}

		UsersDTO user = new UsersDTO(userName, nickName, email, UserActiveTypeDTO.NOT_CONFIRMED);
		Object registration = UserResources.registration(user);
		PrintWriter writer = resp.getWriter();
		writer.println(JSON_Parser.stringify(user));
		writer.close();
		resp.setStatus(200);
	}
}
