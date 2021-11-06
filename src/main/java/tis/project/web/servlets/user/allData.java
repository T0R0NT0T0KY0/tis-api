package tis.project.web.servlets.user;

import tis.project.web.HttpError;
import tis.project.web.JSON_Parser;
import tis.project.web.components.users.dto.UserPageDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.getUserinfo;
import static tis.project.web.helpers.Validator.validateInputData;

@WebServlet(name = "userAll", urlPatterns = "/api/user")
public class allData extends HttpServlet {
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
		Object[] list = getUserinfo(userId);
		if (Objects.nonNull(list[0])) {
			resp.setStatus(400);
			return;
		}

		UserPageDTO upd = (UserPageDTO) list[1];
		PrintWriter pw = resp.getWriter();

		System.out.println(upd);
		pw.write("{ \"data\": " + JSON_Parser.stringify(upd) + "}");
		resp.setStatus(200);
	}
}
