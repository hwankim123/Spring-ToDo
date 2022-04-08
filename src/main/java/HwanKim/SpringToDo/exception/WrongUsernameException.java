package HwanKim.SpringToDo.exception;

public class WrongUsernameException extends RuntimeException{

    public WrongUsernameException() {
        super();
    }

    public WrongUsernameException(String message) {
        super(message);
    }

    public WrongUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongUsernameException(Throwable cause) {
        super(cause);
    }
}
