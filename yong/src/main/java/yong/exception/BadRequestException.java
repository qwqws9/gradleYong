package yong.exception;

public class BadRequestException extends RuntimeException{

    private static final long serialVersionUID = -794615407000915682L;
    
    /**
     * Exception 코드
     */
    private final int code;

    public BadRequestException() {
        this.code = 1000;
    }

    public BadRequestException(String message) {
        super(message);
        this.code = 1000;
    }

    public BadRequestException(Exception exception) {
        super(exception);
        this.code = 1000;
    }

    public BadRequestException(int code) {
        this.code = code;
    }

    public BadRequestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BadRequestException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
