package io.github.majusko.pulsar.exception;

public class PulsarRuntimeException extends RuntimeException {

	public PulsarRuntimeException(String string, Exception e) {
		super(string, e);
	}

	public PulsarRuntimeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PulsarRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PulsarRuntimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PulsarRuntimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PulsarRuntimeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
