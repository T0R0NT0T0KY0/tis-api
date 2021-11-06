package tis.project.web.helpers;

public class Validator {
	public static Object[] validateData(String data) {
		Object[] goList = new Object[2];
		try {
			goList[1] = Long.valueOf(data);
		} catch (Exception e) {
			goList[0] = e.getMessage();
		}
		return goList;
	}

}
