package HwanKim.SpringToDo.exception;

public class TodoAlreadyExistException extends RuntimeException{

    public TodoAlreadyExistException() {
        super();
    }

    public TodoAlreadyExistException(String message) {
        super(message);
    }

    public TodoAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoAlreadyExistException(Throwable cause) {
        super(cause);
    }

}
