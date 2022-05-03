package HwanKim.SpringToDo.exception;

public class WrongDataAccessException extends RuntimeException{

    public WrongDataAccessException() {
        super();
    }

    public WrongDataAccessException(String message) {
        super(message);
    }

    public WrongDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongDataAccessException(Throwable cause) {
        super(cause);
    }
}
