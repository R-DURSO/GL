package exceptions;

/**
 * Exception raised by {@linkplain process.management.AIManager} when wrong actions are created
 * @author Aldric Vitali Silvestre
 * @see process.management.AIManager
 */
public class WrongActionException extends ReflectiveOperationException {

	public WrongActionException() {
		super();
	}

	public WrongActionException(String message) {
		super(message);
	}

	public WrongActionException(Throwable cause) {
		super(cause);
	}

	public WrongActionException(String message, Throwable cause) {
		super(message, cause);
	}

}
