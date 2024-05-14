package jp.co.axa.apidemo.errorhandling;

import jp.co.axa.apidemo.response.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public CustomResponseEntityExceptionHandler() {
        super();
    }

    // 400 BAD REQUEST
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        ApiErrorResponse error = new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid parameter(s)", errors);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    // 404 NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(final ResourceNotFoundException ex, final WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    // 500 INTERNAL ERROR
    @ExceptionHandler({RuntimeException.class, NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }
}
