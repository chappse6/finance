package financial.financialinformation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_INPUT_VALUE(400, "유효하지 않은 입력 값입니다."),
    NOT_HUNDRED_CHANNEL_REVENUE_SHARE(400, "채널과 회사 요율의 합이 100% 가 아닙니다."),
    NOT_HUNDRED_CREATORS_REVENUE_SHARE(400, "크리에이터 요율의 합이 100% 가 아닙니다."),

    NOT_FOUND_CHANNEL(500, "채널 정보를 찾을 수 없습니다."),
    ALREADY_EXIST_CHANNEL(500, "이미 존재하는 채널입니다."),

    NOT_FOUND_CREATOR(500,"크리에이터 정보를 찾을 수 없습니다."),
    ALREADY_EXIST_CREATOR(500, "이미 존재하는 크리에이터입니다."),

    NOT_FOUND_REVENUE(500,"수익 정보를 찾을 수 없습니다."),
    NOT_FOUND_REVENUE_CREATOR(500,"크리에이터의 수익 정보를 찾을 수 없습니다.")
    ;

    private final int status;
    private final String message;
}
