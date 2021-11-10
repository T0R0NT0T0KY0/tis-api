package tis.project.web.helpers;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class Sessions {

	public static HttpServletResponse removeHTTPOnly(HttpServletResponse res) {
		String hName = "set-cookie";
		String cookie = res.getHeader(hName);
		if (Objects.isNull(cookie))
			return res;
		res.setHeader(hName, cookie.replace("HttpOnly", "SameSite=None; Secure"));
		return res;
	}
}
