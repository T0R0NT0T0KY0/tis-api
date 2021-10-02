package migration;

import org.flywaydb.core.Flyway;
import tis.project.web.Envs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Migration {
	public static void main(String[] args) {

		Flyway flyway = new Flyway();
		flyway.setDataSource(Envs.getENV("db_pg_connection_url"),
				Envs.getENV("db_pg_user", "postgres"),
				Envs.getENV("db_pg_password", "postgres"));
		flyway.repair();
		flyway.migrate();
	}
}
