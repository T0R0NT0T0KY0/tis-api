package tis.project.web;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HttpError {
	private final Object errorObject;
	private final int errorCode;

	public HttpError(int errorCode, ErrorObject errorObject) {
		this.errorCode = errorCode;
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
