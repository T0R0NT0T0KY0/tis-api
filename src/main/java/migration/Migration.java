package migration;

import org.flywaydb.core.Flyway;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Migration {
	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader("src/main/resources/properties/application.properties"));
		} catch (IOException e){
			throw new IllegalArgumentException(e);
		}
		Flyway flyway = new Flyway();
		flyway.setDataSource(properties.getProperty("db_pg_connection_url"),
				properties.getProperty("db_pg_user", "postgres"),
				properties.getProperty("db_pg_password", "postgres"));
		flyway.repair();
		flyway.migrate();
	}
}
