package tis.project.web.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Envs {
	private static final Logger logger = LoggerFactory.getLogger("Environments variables");

	private static final Properties ENVS = new Properties();
	static {
		try {
			ENVS.load(Envs.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public static String getENV(String key) {
		return ENVS.getProperty(key);
	}

	public static String getENV(String key, String def) {
		return ENVS.getProperty(key, def);
	}
}
