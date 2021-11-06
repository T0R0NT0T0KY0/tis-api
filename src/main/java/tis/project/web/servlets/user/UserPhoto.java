package tis.project.web.servlets.user;

import tis.project.web.HttpError;
import tis.project.web.JSON_Parser;
import tis.project.web.components.users.dto.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.*;
import static tis.project.web.helpers.Validator.*;

@WebServlet(name = "userPhoto", urlPatterns = "/api/user/photo")
public class UserPhoto extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user_id = req.getParameter("user_id");
		System.out.println("user_id: " + user_id + ", URL=" + req.getRequestURL());

		Object[] validateData = validateInputData(user_id);
		if (Objects.nonNull(validateData[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					"param - user_id can't be empty")));
			return;
		}
		Long userId = (Long) validateData[1];
		Object[] list = getUserPhoto(userId);
		if (Objects.nonNull(list[0])) {resp.sendError(400, ((Exception) list[0]).getMessage());}

		String o = (String) list[1];
		PrintWriter pw = resp.getWriter();

		pw.write("{ \"data\": { \"link\": \"" + o + "\"} }");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user_id = req.getParameter("user_id");
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		System.out.println("user_id: " + user_id);
		Object[] validateData = validateInputData(user_id);
		if (Objects.nonNull(validateData[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					"param - user_id can't be empty")));
			return;
		}
		UserDTO user_dto = (UserDTO) req.getSession(false).getAttribute("user_dto");
		PrintWriter pw = resp.getWriter();
		Long userId = (Long) validateData[1];
		if (user_dto.getId() != userId) {
			resp.sendError(403, """
					{"err"{"text":"Only owner can add photo"}}
					""");
			return;
		}
		Object[] list = addUserPhoto(userId, body.get("photo_link"));
		Error error = (Error) list[0];
		if (Objects.nonNull(error)) {resp.sendError(400, error.getMessage());}
		String o = (String) list[1];
		pw.write("{ \"data\": { \"link\": \"" + o + "\"} }");
		resp.setStatus(200);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user_id = req.getParameter("user_id");
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		System.out.println("user_id: " + user_id);
		Object[] validateData = validateInputData(user_id);
		if (Objects.nonNull(validateData[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					"param - user_id can't be empty")));
			return;
		}
		UserDTO user_dto = (UserDTO) req.getSession(false).getAttribute("user_dto");
		PrintWriter pw = resp.getWriter();
		Long userId = (Long) validateData[1];
		if (user_dto.getId() != userId) {
			resp.sendError(403, """
					{"err"{"text":"Only owner can change photo"}}
					""");
			return;
		}
		Object[] list = updateUserPhoto(userId, body.get("photo_link"));
		Error error = (Error) list[0];
		if (Objects.nonNull(error)) {resp.sendError(400, error.getMessage());}
		String o = (String) list[1];
		pw.write("{ \"data\": { \"link\": " + o + "} }");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user_id = req.getParameter("user_id");
		System.out.println("user_id: " + user_id);
		Object[] validateData = validateInputData(user_id);
		if (Objects.nonNull(validateData[0])) {
			resp.sendError(400, JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
							"param - user_id can't be empty")));
			return;
		}
		UserDTO user_dto = (UserDTO) req.getSession(false).getAttribute("user_dto");
		PrintWriter pw = resp.getWriter();
		Long userId = (Long) validateData[1];
		if (user_dto.getId() != userId) {
			resp.sendError(403, """
					{"err"{"text":"Only owner can change photo"}}
					""");
			return;
		}
		Object[] list = deleteUserPhoto(userId);
		Error error = (Error) list[0];
		if (Objects.nonNull(error)) {resp.sendError(400, error.getMessage());}
		resp.setStatus(200);
	}
}
