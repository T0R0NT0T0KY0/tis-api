package tis.project.web.servlets.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.HttpError;
import tis.project.web.JSON_Parser;
import tis.project.web.components.registration.LoginType;
import tis.project.web.components.users.dto.UserDTO;
import tis.project.web.servlets.registratinon.Registration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.login;
import static tis.project.web.servlets.login.LoginServices.validateLoginData;

@WebServlet(name = "loginServlet", urlPatterns = "/api/login")
public class LogIn extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Registration.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		Object[] validateData = validateLoginData(body);
		body.forEach((s, s2) -> System.out.println(s + ": " + s2));

		HttpError error = (HttpError) validateData[0];
		PrintWriter pw = resp.getWriter();
		if (Objects.nonNull(error)) {
			pw.write(JSON_Parser.stringify(error.getErrorObject()));
			resp.setStatus(400);
			return;
		}

		HttpSession session = req.getSession();
		String sessionId = session.getId();

		LoginType loginType = (LoginType) validateData[1];
		Object[] list = login(loginType.getEmail(), loginType.getPassword(), sessionId);
		if (Objects.nonNull(list[0])) {
			pw.write(JSON_Parser.stringify(new HttpError.ErrorObject("Invalid input",
					"Invalid login (email) / password combination")));
			resp.setStatus(403);
			return;
		}
		session.setMaxInactiveInterval(-1);
		UserDTO ud = (UserDTO) list[1];
		session.setAttribute("user_dto", ud);
		pw.write("{\"user_id\": " + ud.getId() + " }");
		resp.setStatus(200);
	}

}
