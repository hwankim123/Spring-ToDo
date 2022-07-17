package HwanKim.SpringToDo.exception;

public class TodoNotExistException extends RuntimeException{

    public TodoNotExistException() {
        super();
    }

    public TodoNotExistException(String message) {
        super(message);
    }

    public TodoNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoNotExistException(Throwable cause) {
        super(cause);
    }
}
