# 단축 URL 제공 서비스

이 프로젝트는 URL을 short URL로 변환하고, short URL을 이용해 original URL을 다시 조회할 수 있는 URL 단축 서비스를 구현한 것입니다.

## 사용기술
   - Spring Boot
   - H2, MyBatis
   - Gradle

## API 명세서

### 1. 단축 URL 생성 API

- **HTTP Method:** `PATCH`
- **Endpoint:** `/api/shorten`

  원본 URL(`oriUrl`)을 입력받아 8자리 단축 URL(`shortUrl`)을 생성합니다.  
  동일한 URL 요청 시 기존 단축 URL을 반환하며, 요청 횟수(`requestCount`)가 증가합니다.

- **요청 예시:**
    - `PATCH /api/shorten?oriUrl=https://example.com`

- **성공 응답:**
    - **상태 코드:**
        - `201 Created` : 신규 URL 생성 시
        - `200 OK` : 기존 URL 재요청 시
    - **응답 본문:**
      ```
      {
          "id": 1,
          "oriUrl": "https://example.com",
          "shortUrl": "aBcD1234",
          "requestCount": 1
      }
      ```

- **실패 응답:**
    - **상태 코드:** `400 Bad Request`
    - **응답 본문:**
      ```
      { "error": "Invalid original URL" }
      ```
### 2. 단축 URL 해제(Resolve) API

- **HTTP Method:** `PATCH`
- **Endpoint:** `/api/resolve`

  단축 URL(`shortUrl`)을 입력받아 원본 URL(`oriUrl`)을 반환하고, 요청 횟수(`requestCount`)를 증가시킵니다.

- **요청 예시:**
    - `PATCH /api/resolve?shortUrl=aBcD1234`

- **성공 응답:**
    - **상태 코드:** `200 OK`
    - **응답 본문:**
      ```
      {
          "id": 1,
          "oriUrl": "https://example.com",
          "shortUrl": "aBcD1234",
          "requestCount": 2
      }
      ```

- **실패 응답:**
    - **상태 코드:** `500 Bad Request`
    - **응답 본문:**
      ```
      { "error": "Internal Server Error }
      ```


## 테스트

- **isValidUrl_shouldReturnTrueForValidUrl**
    - 유효한 URL ("http://example.com", "https://google.com")에 대해 `isValidUrl` 메서드가 true를 반환하는지 확인

- **isValidUrl_shouldReturnFalseForInvalidUrl**
    - 잘못된 URL ("invalid-url", "htp:example.com")에 대해 `isValidUrl` 메서드가 false를 반환하는지 확인

- **shortenUrl_shouldThrowExceptionForInvalidUrl**
    - 유효하지 않은 URL ("not-a-valid-url") 입력 시 `shortenUrl` 메서드가 `IllegalArgumentException`을 발생시키는지 검증

- **shortenUrl_shouldCreateNewShortUrlForFirstTimeUri**
    - 처음 입력한 URL("http://new-example1.com")에 대해 새로운 단축 URL이 생성되고, DB에 저장된 후 `requestCount`가 1인지 확인

- **shouldResolveShortUrlToOriginalUrl**
    - 생성된 단축 URL에 대해 `resolveUrl` 메서드가 올바른 원본 URL("http://example2.com")을 반환하는지 확인

- **resolveUrl_shouldIncrementRequestCount**
    - 단축 URL을 여러 번 해제 호출하여 (`resolveUrl` 메서드 실행) `requestCount`가 올바르게 증가하는지 (최종적으로 7가 되어야 함) 확인

- **resolveUrl_shouldThrowExceptionForUnknownShortUrl**
    - 존재하지 않는 단축 URL("unknown123") 입력 시 `resolveUrl` 메서드가 `IllegalArgumentException`을 발생시키는지 검증
