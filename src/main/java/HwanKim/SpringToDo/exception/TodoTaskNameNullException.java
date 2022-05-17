package HwanKim.SpringToDo.exception;

public class TodoTaskNameNullException extends RuntimeException{

    public TodoTaskNameNullException() {
        super();
    }

    public TodoTaskNameNullException(String message) {
        super(message);
    }

    public TodoTaskNameNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoTaskNameNullException(Throwable cause) {
        super(cause);
    }
}
