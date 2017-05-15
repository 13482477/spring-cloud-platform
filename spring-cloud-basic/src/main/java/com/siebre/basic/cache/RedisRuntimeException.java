package com.siebre.basic.cache;

public class RedisRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -6134570680112235212L;

	public RedisRuntimeException() {
		super();
	}
	
	public RedisRuntimeException(String message) {
		super(message);
	}

	public RedisRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisRuntimeException(Throwable cause) {
		super(cause);
	}

}
