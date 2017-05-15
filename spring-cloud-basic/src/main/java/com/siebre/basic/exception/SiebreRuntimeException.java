package com.siebre.basic.exception;

public class SiebreRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6911455846092246811L;

	public SiebreRuntimeException() {
		super();
	}

	public SiebreRuntimeException(String message) {
		super(message);
	}

	public SiebreRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SiebreRuntimeException(Throwable cause) {
		super(cause);
	}

	protected SiebreRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
