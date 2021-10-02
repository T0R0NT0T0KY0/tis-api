package tis.project.web;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Envs {
	private static final Properties ENVS = new Properties();

	static {
		try {
			ENVS.load(new FileReader("src/main/resources/properties/application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getENV(String key) {
		return ENVS.getProperty(key);
	}

	public static String getENV(String key, String def) {
		return ENVS.getProperty(key, def);
	}
}
