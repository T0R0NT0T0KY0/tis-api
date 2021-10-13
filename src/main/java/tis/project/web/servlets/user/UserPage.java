package tis.project.web.servlets.user;

import tis.project.web.JSON_Parser;
import tis.project.web.components.users.dto.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static tis.project.web.components.users.UserResources.getUserInformationById;


public class UserPage extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map body = JSON_Parser.parse(req.getReader());
		Object[] validateData = validateData(body);
		if (Objects.nonNull(validateData[0])) {
			resp.sendError(400);
			return;
		}
		UserDTO user = (UserDTO) validateData[1];
		long id = user.getId();
		Object[] list = getUserInformationById(id);
	}

	private Object[] validateData(Map body) {
		Object[] goList = new Object[2];
		try {
			goList[1] = (UserDTO) body.get("user_dto");
		} catch (Exception e) {
			goList[0] = e;
		}
		return goList;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}
}
