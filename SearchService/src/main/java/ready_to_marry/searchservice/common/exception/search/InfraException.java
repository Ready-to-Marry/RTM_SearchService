package ready_to_marry.searchservice.common.exception.search;

import lombok.Getter;
import ready_to_marry.searchservice.common.exception.ErrorCode;

@Getter
public class InfraException extends RuntimeException {

    private final int code;

    public InfraException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
    }
}