package tis.project.web.servlets.registratinon;

import tis.project.web.helpers.JSON_Parser;
import tis.project.web.components.registration.RegisterType;
import tis.project.web.helpers.HttpError;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.registration;
import static tis.project.web.helpers.Sessions.removeHTTPOnly;
import static tis.project.web.servlets.registratinon.RegistrationServices.validateRegistrationData;

@WebServlet(name = "registrationServlet", urlPatterns = "/api/registration")
public class Registration extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		body.forEach((s, s2) -> System.out.println(s + ": " + s2));

		Object[] validateData = validateRegistrationData(body);
		HttpError error = (HttpError) validateData[0];
		PrintWriter pw = resp.getWriter();
		if (Objects.nonNull(error)) {
			pw.write(JSON_Parser.stringify(error.getErrorObject()));
			resp.setStatus(error.getStatus());
			return;
		}
		RegisterType user = (RegisterType) validateData[1];
		HttpSession session = req.getSession();
		String sessionId = session.getId();
		user.setSessionId(sessionId);

		Object[] list = registration(user);
		if (Objects.nonNull(list[0])) {
			pw.write(JSON_Parser.stringify(new HttpError.ErrorObject("Invalid input",
					((Exception) list[0]).getMessage())));
			resp.setStatus(400);
			return;
		}
		session.setMaxInactiveInterval(-1);
		session.setAttribute("user_dto", list[1]);
		System.out.println("{ \"user_id\": " + user.getId() + "}");
		pw.write("{ \"user_id\": " + user.getId() + " }");
		resp.setStatus(200);
		removeHTTPOnly(resp);
	}


}
