package tis.project.web.servlets.user;

import tis.project.web.helpers.HttpError;
import tis.project.web.helpers.JSON_Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.getLivingPlace;
import static tis.project.web.helpers.Validator.validateInputData;

@WebServlet(name = "userLP", urlPatterns = "/api/user/living_place")
public class LivingPlace extends HttpServlet {
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
		Object[] list = getLivingPlace(userId);
		if (Objects.nonNull(list[0])) {resp.sendError(400, ((Exception) list[0]).getMessage());}

		String o = (String) list[1];

		pw.write("{ \"data\": { \"place\": \"" + o + "\"} }");
		resp.setStatus(200);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPut(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doDelete(req, resp);
	}
}
