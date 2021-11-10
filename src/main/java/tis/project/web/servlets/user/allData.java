package tis.project.web.servlets.user;

import tis.project.web.helpers.HttpError;
import tis.project.web.helpers.JSON_Parser;
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
		PrintWriter pw = resp.getWriter();
		if (Objects.nonNull(validateData[0])) {
			pw.write(JSON_Parser.stringify(new HttpError.ErrorObject("Bed request",
					"Param - user_id can't be empty")));
			resp.setStatus(400);
			return;
		}
		Long userId = (Long) validateData[1];
		Object[] list = getUserinfo(userId);
		if (Objects.nonNull(list[0])) {
			resp.setStatus(400);
			return;
		}

		UserPageDTO upd = (UserPageDTO) list[1];

		System.out.println(upd);
		pw.write("{ \"data\": " + JSON_Parser.stringify(upd) + "}");
		resp.setStatus(200);
	}
}
