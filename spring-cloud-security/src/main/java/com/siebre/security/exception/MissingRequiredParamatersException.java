package com.siebre.security.exception;

/**
 * Created by jhonelee on 2017/7/20.
 */
public class MissingRequiredParamatersException extends RuntimeException {

	public MissingRequiredParamatersException() {
		super();
	}

	public MissingRequiredParamatersException(String message) {
		super(message);
	}

	public MissingRequiredParamatersException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingRequiredParamatersException(Throwable cause) {
		super(cause);
	}

	protected MissingRequiredParamatersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
