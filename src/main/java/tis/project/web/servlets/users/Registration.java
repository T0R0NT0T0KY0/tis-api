package tis.project.web.servlets.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tis.project.web.components.users.UserActiveTypeDTO;
import tis.project.web.components.users.UserResources;
import tis.project.web.components.users.UsersDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "loginServlet", urlPatterns = "/registration")
public class Registration extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Registration.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String> body = JSON_Parser.parse(req.getReader());
		String userName = body.get("userName");
		String nickName = body.get("nickName");
		String email = body.get("email");
		if (Objects.isNull(userName) || Objects.isNull(nickName) || Objects.isNull(email))
			resp.sendError(400);
		UsersDTO user = new UsersDTO(userName, nickName, email, UserActiveTypeDTO.NOT_CONFIRMED);
		boolean registration = UserResources.registration(user);
		PrintWriter writer = resp.getWriter();
		writer.println(JSON_Parser.stringify(user));
		writer.close();
		resp.setStatus(registration?200:400);
	}
}
