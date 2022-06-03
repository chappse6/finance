package financial.financialinformation.advice;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import financial.financialinformation.exception.ExceptionCode;
import financial.financialinformation.exception.FinancialInformationException;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = FinancialInformationException.class)
    public ResponseEntity<ErrorResult> error(FinancialInformationException e) {
        return new ResponseEntity<>(ErrorResult.of(e), HttpStatus.valueOf(e.getHttpStatus()));
    }

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ErrorResult.of(e), HttpStatus.BAD_REQUEST);
    }

    @Value
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ErrorResult{
        private String errorCode;
        private String message;

        static ErrorResult of(FinancialInformationException exception){
            return ErrorResult
                    .builder()
                    .errorCode(exception.getErrorCode())
                    .message(exception.getMessage())
                    .build();
        }

        static ErrorResult of(MethodArgumentNotValidException ValidException){
            return ErrorResult
                    .builder()
                    .errorCode(ExceptionCode.INVALID_INPUT_VALUE.name())
                    .message(ValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                    .build();
        }
    }
}
