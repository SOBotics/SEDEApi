package org.sobotics.sedeapi.aws;

/**
 * Runtime exception thrown by lambda
 * @author Petter Friberg
 *
 */
public class LambdaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2376359489017309595L;

	public LambdaException() {
		super();
	}

	public LambdaException(String message, Throwable cause) {
		super(message, cause);
	}

	public LambdaException(String message) {
		super(message);
	}

	public LambdaException(Throwable cause) {
		super(cause);
	}

	
	
}
