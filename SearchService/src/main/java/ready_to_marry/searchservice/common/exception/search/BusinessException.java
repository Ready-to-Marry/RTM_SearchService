package ready_to_marry.searchservice.common.exception.search;

import lombok.Getter;
import ready_to_marry.searchservice.common.exception.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}