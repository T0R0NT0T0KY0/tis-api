package tis.project.web.servlets.user;

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

import static tis.project.web.components.users.UserResources.getValidUserIdByToken;
import static tis.project.web.helpers.Validator.validateInputDTO;

@WebServlet(name = "userId", urlPatterns = "/api/user_id")
public class UserId extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);

		if (Objects.isNull(session)) {
			resp.setStatus(403);
			return;
		}
		Object user_dto = session.getAttribute("user_dto");
		System.out.println(123);
		System.out.println("1user_id: " + user_dto.toString() + ", URL=" + req.getRequestURL());

		PrintWriter pw = resp.getWriter();

		Object[] validateData = validateInputDTO(user_dto);

		if (Objects.nonNull(validateData[0])) {
			resp.setStatus(403);
			return;
		}

		UserDTO userDTO = (UserDTO) validateData[1];
		System.out.println("user_dto: " + userDTO + ", URL=" + req.getRequestURL());

		Object[] list = getValidUserIdByToken(userDTO.getToken());
		if (Objects.nonNull(list[0])) {
			resp.setStatus(400);
			return;
		}
		if (Objects.isNull(list[1])) {
			resp.setStatus(403);
			return;
		}
		pw.write("{user_id: " + list[1] + "}");
		resp.setStatus(200);
	}
}
