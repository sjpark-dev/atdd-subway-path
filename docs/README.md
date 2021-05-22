## 기능 목록
### 1단계
#### 프론트엔드
- [x] 토큰 발급 요청 API 구현하기

#### 백엔드
- [x] 토큰 발급 API 구현하기
- request
```json
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```
- response
```json
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Dec 2020 04:32:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY"
}
```

- [x] cors 처리 설정

### 2단계
#### 프론트엔드
- [x] 내 정보 기능에 관한 API 호출 기능 구현하기
  
###x 백엔드
- [x] 토큰을 통한 인증 - 내 정보 기능 
    - [x] "/members/me" 요청 시 토큰을 확인하여 로그인 정보를 받아오기
    - [x] @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용
    - [x] Interceptor로 토큰이 있는지 확인