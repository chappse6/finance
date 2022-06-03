package financial.financialinformation.exception;

public class FinancialInformationException extends RuntimeException {

    private ExceptionCode code;
    private int httpStatus;
    private String message;

    public FinancialInformationException(ExceptionCode code) {
        this.code = code;
        this.httpStatus = code.getStatus();
        this.message = code.getMessage();
    }

    public FinancialInformationException(ExceptionCode code, int httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = code.getMessage();
    }

    public FinancialInformationException(ExceptionCode code, String message) {
        this.code = code;
        this.httpStatus = code.getStatus();
        this.message = message;
    }

    public FinancialInformationException(ExceptionCode code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getErrorCode() {
        return code.name();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
