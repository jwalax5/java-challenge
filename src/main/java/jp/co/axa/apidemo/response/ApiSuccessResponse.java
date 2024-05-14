package jp.co.axa.apidemo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSuccessResponse<T> {
    private HttpStatus status;
    private String message;
    private List<T> results;
    private T result;

    public ApiSuccessResponse() {
        super();
    }

    public ApiSuccessResponse(final HttpStatus status, final String message, final T result) {
        super();
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public ApiSuccessResponse(final HttpStatus status, final String message, final List<T> results) {
        super();
        this.status = status;
        this.message = message;
        this.results = results;
    }

}
