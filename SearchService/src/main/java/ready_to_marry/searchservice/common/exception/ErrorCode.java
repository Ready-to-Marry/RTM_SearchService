package ready_to_marry.searchservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 1xxx: 비즈니스 오류
    NO_SEARCH_RESULT(1001, "no search result"),
    NO_SEARCH_TERM(1002, "can't blank search term"),

    // 2xxx: 인프라(시스템) 오류
    REDIS_SAVE_FAILURE(2001, "Can't save search term");

    private final int code;
    private final String message;
}