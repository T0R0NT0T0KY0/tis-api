package tis.project.web.helpers;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HttpError {
	private final Object errorObject;
	private final int status;

	public HttpError(int status, ErrorObject errorObject) {
		this.status = status;
		this.errorObject = errorObject;
	}

	@Getter
	@ToString
	public static class ErrorObject {
		private final String errorText;
		private final String description;

		public ErrorObject(String errorText) {
			this(errorText, errorText);
		}

		public ErrorObject(String error, String description) {
			this.errorText = error;
			this.description = description;
		}
	}
}
