package tis.project.web.servlets.user;

import tis.project.web.helpers.HttpError;
import tis.project.web.helpers.JSON_Parser;
import tis.project.web.components.users.dto.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.getUsername;
import static tis.project.web.components.users.UserResources.updateUsername;
import static tis.project.web.helpers.Validator.validateInputDTO;
import static tis.project.web.helpers.Validator.validateInputData;

@WebServlet(name = "username", urlPatterns = "/api/user/username")
public class Username extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user_id = req.getParameter("user_id");
		System.out.println("user_id: " + user_id + ", URL=" + req.getRequestURL());

		Object[] validateData = validateInputData(user_id);
		PrintWriter pw = resp.getWriter();
		if (Objects.nonNull(validateData[0])) {
			pw.write(JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					"Param - user_id can't be empty")));
			resp.setStatus(400);
			return;
		}

		Long userId = (Long) validateData[1];

		Object[] list = getUsername(userId);
		if (Objects.nonNull(list[0])) {resp.sendError(400, ((Exception) list[0]).getMessage());}

		String o = (String) list[1];

		pw.write("{ \"data\": { \"username\": \"" + o + "\"} }");
		resp.setStatus(200);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		Object user_dto = session.getAttribute("user_dto");
		if (Objects.isNull(user_dto)) {
			resp.setStatus(400);
			return;
		}
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		body.forEach((s, s2) -> System.out.println(s + ": " + s2));

		PrintWriter pw = resp.getWriter();

		String username = body.get("username");
		if (Objects.isNull(username) || username.trim().length() == 0) {
			pw.write(JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					"Updated param - username can't be empty")));
			resp.setStatus(400);
			return;
		}

		Object[] validateData = validateInputDTO(user_dto);

		if (Objects.nonNull(validateData[0])) {
			resp.setStatus(403);
			return;
		}

		UserDTO userDTO = (UserDTO) validateData[1];
		System.out.println("user_dto: " + userDTO + ", URL=" + req.getRequestURL());

		Object[] list = updateUsername(userDTO.getId(), userDTO.getToken(), username);
		if (Objects.nonNull(list[0])) {
			pw.write(JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					((Exception) list[0]).getMessage())));
			resp.setStatus(400);
		}

		if (Objects.isNull(list[1])) {
			pw.write(JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					"Inhabited exception")));
			resp.setStatus(400);
		}

		resp.setStatus(200);
	}
}
