# 크리에이터 정산금액 산정 API

MCN의 백오피스로 사용되는 정산금액 산정 기능의 API

## 기술 스택
- 어플리케이션 : Java 11 / Spring Boot 2.7.0 / MariaDB / JPA / Gradle / JUnit5
- IDE : IntelliJ IDEA
- OS : Mac OS M1

## 구현된 API
특정 유튜브 채널에 수익금액 데이터 등록 API
- POST /revenue

유튜브 채널과 크리에이터 계약정보 등록 API
- POST /revenue/channel

특정 채널수익금액과 계약정보에 따른 크리에이터 정산금액 조회 API
- GET revenue/channel/{id}

특정 크리에이터 기준으로 채널별 정산금액 조회 API
- GET revenue/creator/{id}

유튜브 월별 회사 총매출 / 순매출조회 API
- GET revenue/company

## 예외

API 응답 오류 상황에 대해 명시적으로 Exception 을 발생시킴으로써 에러 핸들링이 가능하도록 수정하였습니다.
예를 들어, 기존에는 500 응답이 내려오는 경우 오류 message를 확인할 수 없는 단점이 있었는데 FinancialInformationException 을 throw 함으로써 정확한 오류 원인을 파악하실 수 있습니다.

### 전역 예외 처리
중복 코드를 줄이고 유지보수를 쉽게하기 위해 RestControllerAdvice, ExceptionHandler 어노테이션을 활용하여 전역에서 예외 처리하도록 처리했습니다.

```java
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
```

## 테스트
src/test/java 의 RevenueApiControllerTest와 RevenueServiceTest를 참조해주세요.
