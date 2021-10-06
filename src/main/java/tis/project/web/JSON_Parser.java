package tis.project.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import tis.project.web.components.users.UserActiveTypeDTO;
import tis.project.web.components.users.UsersDTO;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class JSON_Parser {

	private final static ObjectWriter objectWriter =
			new ObjectMapper().writer().withDefaultPrettyPrinter();


	public static Map<String, String> parse(String json) {
		try {
			return new ObjectMapper().readValue(json,
					Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	public static Map<String, String> parse(BufferedReader buffJSON) {
		return parse(buffJSON.lines()
				.reduce("", (accumulator, actual) -> accumulator + actual));
	}

	public static String stringify(Object obj) {
		try {
			return objectWriter.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
