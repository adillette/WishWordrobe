package Today.WishWordrobe.exception;

public class FileUploadException extends RuntimeException{
    public FileUploadException() {
        super();
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable casue) {
        super(message, casue);
    }
}
