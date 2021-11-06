package tis.project.web.servlets.user;

import tis.project.web.HttpError;
import tis.project.web.JSON_Parser;
import tis.project.web.components.users.dto.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.getUsername;
import static tis.project.web.helpers.Validator.validateInputData;

@WebServlet(name = "username", urlPatterns = "/api/user/username")
public class Username extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user_id = req.getParameter("user_id");
		System.out.println("user_id: " + user_id + ", URL=" + req.getRequestURL());

		Object[] validateData = validateInputData(user_id);
		if (Objects.nonNull(validateData[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Неправильный запрос", "Обязательный" +
					"пареметр - id пользователя не может быть пустым")));
			return;
		}
		Long userId = (Long) validateData[1];
		Object[] list = getUsername(userId);
		if (Objects.nonNull(list[0])) {resp.sendError(400, ((Exception) list[0]).getMessage());}

		String o = (String) list[1];
		PrintWriter pw = resp.getWriter();

		pw.write("{ \"data\": { \"username\": \"" + o + "\"} }");
		resp.setStatus(200);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		Object user_dto = session.getAttribute("user_dto");
		if (Objects.isNull(user_dto)) {
			resp.setStatus(403);
			return;
		}
		Object[] validateData = validateInputData(user_dto);

		if (Objects.nonNull(validateData[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Неправильный запрос", "Обязательный" +
					"пареметр - id пользователя не может быть пустым")));
			return;
		}

		UserDTO userDTO = (UserDTO) validateData[1];
		System.out.println("user_dto: " + userDTO + ", URL=" + req.getRequestURL());

		Long userId = (Long) validateData[1];
		Object[] list = getUsername(userId);
		if (Objects.nonNull(list[0])) {resp.sendError(400, ((Exception) list[0]).getMessage());}

		String o = (String) list[1];
		PrintWriter pw = resp.getWriter();

		pw.write("{ \"data\": { \"username\": \"" + o + "\"} }");
		resp.setStatus(200);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPut(req, resp);
	}
}
