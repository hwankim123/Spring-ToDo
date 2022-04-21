package HwanKim.SpringToDo.exception;

public class TaskNameDuplicateException extends RuntimeException{

    public TaskNameDuplicateException() {
        super();
    }

    public TaskNameDuplicateException(String message) {
        super(message);
    }

    public TaskNameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNameDuplicateException(Throwable cause) {
        super(cause);
    }
}
