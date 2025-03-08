# 단축 URL 제공 서비스

이 프로젝트는 URL을 short URL로 변환하고, short URL을 이용해 original URL을 다시 조회할 수 있는 URL 단축 서비스를 구현한 것입니다.

## 주요 기능

- **URL 단축**: 주어진 URL을 고유한 short URL로 변환
- **original URL 조회**: short URL을 제공하면 해당하는 original URL 반환
- **요청 횟수 추적**: short URL과 original URL을 포함한 동일한 URL 요청 시마다 요청 횟수를 증가

## 프로젝트 구조

1. **서비스 레이어**
   - URL의 형식 및 유효성을 검증
   - original URL을 통한 short URL 8자 랜덤 생성 및 조회수 증가
   - short URL을 통해 original URL 조회 및 조회수 증가

2. **데이터 레이어**
   - 기술: MyBatis, H2
   - original URL과 short URL 간 매핑 데이터 저장
   - 각 URL 별 요청 횟수를 관리

3. **컨트롤러 레이어**
   - 서비스를 사용하기 위한 REST 엔드포인트 제공
   - short URL 생성 및 original URL 조회 요청 처리

## 테스트
   - URL 검 증 로직에 대한 단위 테스트
   - URL을 short Url로 변환하여 저장하는 로직에 대한 단위 테스트
   - short URL을 통해 original URL을 조회하는 로직에 대한 단위 테스트
   - 동일한 URL에 대한 여러 요청이 요청 횟수를 올바르게 증가시키는지 검증
