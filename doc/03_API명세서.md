---
title: Coupon System API 명세서
author: psjw
created: 2025-06-21
---

# 📘 API 명세서 (초안)

## 🧾 공통 사항

| 항목       | 설명                          |
|------------|-------------------------------|
| Base URL   | `https://api.psjw-edu.link`  |
| Format     | JSON                          |
| Auth       | Bearer Token (JWT)            |
| Status Code | HTTP 상태 코드 준수 (200, 400, 401 등) |

---

# 📦 Event 도메인 - 관리자 API 명세서

## ✅ 1. 이벤트 생성
### ▶️ **POST /api/v1/admin/events**

| 항목       | 설명                     |
|------------|------------------------|
| Method     | POST                   |
| Path       | `/api/v1/admin/events` |
| 인증       | 필요 (Bearer Token)      |
| 설명       | 관리자가 이벤트를 생성함          |

#### 📥 Request Body

```json
{
  "eventName": "신규 쿠폰 이벤트",
  "description": "다른 회사와 협업",
  "fromAt": "2025-06-21T09:00:00",
  "untilAt": "2025-06-21T21:00:00",
  "eventStatus": "READY"
}
```

#### 📤 Response Body
```json
{
  "eventId": 12345,
  "eventName": "신규 쿠폰 이벤트",
  "eventStatus": "READY"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                   | 메시지                       | 발생 조건 예시           |
|------|-------------------------|---------------------------|--------------------|
| 400  | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.         | 필수 값 누락, 데이터 형식 오류 |
| 400  | `INVALID_DATETIME`      | 시작 시간은 종료 시간보다 이전이어야 합니다. | `fromAt > untilAt` |
| 401  | `UNAUTHORIZED`          | 인증 정보가 없습니다.              | JWT 누락 또는 만료       |
| 403  | `FORBIDDEN`             | 관리자 권한이 필요합니다.            | `ROLE_ADMIN` 미보유   |
| 409  | `DUPLICATE_EVENT`       | 동일한 이름의 이벤트가 이미 실행 중입니다.  | 중복 이벤트 생성 시도       |
| 500  | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다.         | DB 예외, 알 수 없는 예외 등 |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "code": "INVALID_DATETIME",
  "message": "이벤트 시작 시간은 종료 시간보다 이전이어야 합니다.",
  "path": "/api/v1/admin/events"
}
```


## ✅ 2. 이벤트 수정
### ▶️ **PUT /api/v1/admin/events/{id}**

| 항목       | 설명                          |
|------------|-----------------------------|
| Method     | PUT                         |
| Path       | `/api/v1/admin/events/{id}` |
| 인증       | 필요 (Bearer Token)           |
| 설명       | 관리자가 이벤트를 수정함               |

#### 📥 Request Body

```json
{
  "eventName": "신규 쿠폰 이벤트 이름 변경",
  "description": "다른 회사와 협업 변경",
  "fromAt": "2025-06-21T09:00:00",
  "untilAt": "2025-06-21T21:00:00",
  "eventStatus": "READY"
}
```

#### 📤 Response Body
```json
{
  "eventId": 12345,
  "eventName": "신규 쿠폰 이벤트 이름 변경",
  "eventStatus": "READY",
  "updatedAt": "2025-06-21T10:50:00"
}
```


### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                   | 메시지                             | 발생 조건 예시 |
|------|-------------------------|---------------------------------|----------------|
| 400  | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.               | 필수 값 누락, 데이터 형식 오류 |
| 400  | `INVALID_DATETIME`      | 시작 시간은 종료 시간보다 이전이어야 합니다.       | `fromAt > untilAt` |
| 401  | `UNAUTHORIZED`          | 인증 정보가 없습니다.                    | JWT 누락 또는 만료 |
| 404  | `EVENT_NOT_FOUND`       | 이벤트를 찾을 수 없습니다.           | 잘못된 eventId        |
| 403  | `FORBIDDEN`             | 관리자 권한이 필요합니다.                  | `ROLE_ADMIN` 미보유 |
| 409  | `DUPLICATE_EVENT`       | 동일한 이름의 이벤트가 이미 실행 중입니다. | 중복 이벤트 생성 시도 |
| 500  | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다.               | DB 예외, 알 수 없는 예외 등 |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "code": "INVALID_DATETIME",
  "message": "이벤트 시작 시간은 종료 시간보다 이전이어야 합니다.",
  "path": "/api/v1/admin/events/12345"
}
```



## ✅ 3. 이벤트 상태 변경
### ▶️ **PUT /api/v1/admin/events/{id}/status**

| 항목       | 설명                                 |
|------------|------------------------------------|
| Method     | PUT                                |
| Path       | `/api/v1/admin/events/{id}/status` |
| 인증       | 필요 (Bearer Token)                  |
| 설명       | 관리자가 이벤트 상태를 변경함                   |

#### 📥 Request Body

```json
{
  "eventStatus": "FINISHED"
}
```

#### 📤 Response Body
```json
{
  "previousStatus": "READY",
  "updatedStatus": "FINISHED",
  "updatedAt": "2025-06-21T10:50:00"
}
```


### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드              | 메시지               | 발생 조건 예시             |
|--------|------------------------|-------------------|----------------------|
| 400    | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다. | 필수 값 누락, 데이터 형식 오류   |
| 400    | `INVALID_STATUS_TRANSITION`       | 허용되지 않는 상태 변경입니다. | 예: FINISHED → READY  |
| 401    | `UNAUTHORIZED`          | 인증 정보가 없습니다.      | JWT 누락 또는 만료         |
| 403    | `FORBIDDEN`             | 관리자 권한이 필요합니다.    | `ROLE_ADMIN` 미보유     |
| 404    | `EVENT_NOT_FOUND`       | 이벤트를 찾을 수 없습니다.   | 잘못된 eventId          |
| 500    | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다. | DB 예외, 알 수 없는 예외 등   |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "EVENT_NOT_FOUND",
  "message": "이벤트를 찾을 수 없습니다.",
  "path": "/api/v1/admin/events/12345/status"
}
```


## ✅ 4. 이벤트 삭제
### ▶️ **DELETE /api/v1/admin/events/{id}**

| 항목       | 설명                          |
|------------|-----------------------------|
| Method     | DELETE                      |
| Path       | `/api/v1/admin/events/{id}` |
| 인증       | 필요 (Bearer Token)           |
| 설명       | 관리자가 이벤트를 삭제함           |

#### 📥 Request Body

> 없음

#### 📤 Response Body
```json
{
  "eventId": 12345,
  "deletedAt": "2025-06-21T10:50:00"
}
```


### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드              | 메시지                 | 발생 조건 예시    |
|--------|------------------------|---------------------|-------------|
| 400    | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.   | 필수 값 누락, 데이터 형식 오류 |
| 400    | `CANNOT_DELETE_EVENT`       | 해당 이벤트는 삭제할 수 없습니다. | 예: FINISHED |
| 401    | `UNAUTHORIZED`          | 인증 정보가 없습니다.        | JWT 누락 또는 만료 |
| 403    | `FORBIDDEN`             | 관리자 권한이 필요합니다.      | `ROLE_ADMIN` 미보유 |
| 404    | `EVENT_NOT_FOUND`       | 이벤트를 찾을 수 없습니다.     | 잘못된 eventId |
| 500    | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다.   | DB 예외, 알 수 없는 예외 등 |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "EVENT_NOT_FOUND",
  "message": "이벤트를 찾을 수 없습니다.",
  "path": "/api/v1/admin/events/12345"
}
```




## ✅ 5. 이벤트 단건 조회
### ▶️ **GET /api/v1/admin/events/{id}**

| 항목       | 설명                          |
|------------|-----------------------------|
| Method     | GET                         |
| Path       | `/api/v1/admin/events/{id}` |
| 인증       | 필요 (Bearer Token)           |
| 설명       | 관리자가 이벤트 정보를 조회함            |

#### 📥 Request Body
 
> 없음

#### 📤 Response Body
```json
{
  "eventId": 12345,
  "eventName": "신규 쿠폰 이벤트 이름 변경",
  "description": "다른 회사와 협업 변경",
  "fromAt": "2025-06-21T09:00:00",
  "untilAt": "2025-06-21T21:00:00",
  "eventStatus": "READY"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드              | 메시지                 | 발생 조건 예시    |
|--------|------------------------|---------------------|-------------|
| 400    | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.   | 필수 값 누락, 데이터 형식 오류 |
| 401    | `UNAUTHORIZED`          | 인증 정보가 없습니다.        | JWT 누락 또는 만료 |
| 403    | `FORBIDDEN`             | 관리자 권한이 필요합니다.      | `ROLE_ADMIN` 미보유 |
| 404    | `EVENT_NOT_FOUND`       | 이벤트를 찾을 수 없습니다.     | 잘못된 eventId |
| 500    | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다.   | DB 예외, 알 수 없는 예외 등 |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "EVENT_NOT_FOUND",
  "message": "이벤트를 찾을 수 없습니다.",
  "path": "/api/v1/admin/events/12345"
}
```



## ✅ 5. 이벤트 목록 조회
### ▶️ **GET /api/v1/admin/events**

| 항목       | 설명                     |
|------------|------------------------|
| Method     | GET                    |
| Path       | `/api/v1/admin/events` |
| 인증       | 필요 (Bearer Token)      |
| 설명       | 관리자가 이벤트 목록를 조회함       |

### 🔍 요청 쿼리 파라미터

| 기능           | 설명                          | 쿼리 파라미터 예시                         |
|----------------|-------------------------------|---------------------------------------------|
| 상태 필터링     | `eventStatus=READY`           | `/events?status=READY`                      |
| 기간 필터링     | `fromAt`, `untilAt` 범위 검색 | `/events?from=2025-06-01&to=2025-06-30`     |
| 키워드 검색     | 이름 또는 설명 기준 검색      | `/events?keyword=쿠폰`                      |
| 정렬           | 최신순, 시작일순 등 정렬       | `/events?sort=createdAt,DESC`              |
| 페이징         | `page`, `size` 파라미터 사용   | `/events?page=0&size=20`                    |


#### 📥 Request Body

> 없음

#### 📤 Response Body

```json
{
  "count": 2,
  "page": 1,
  "size": 10,
  "data": [
    {
      "eventId": 12345,
      "eventName": "신규 쿠폰 이벤트 이름 변경",
      "description": "다른 회사와 협업 변경",
      "fromAt": "2025-06-21T09:00:00",
      "untilAt": "2025-06-21T21:00:00",
      "eventStatus": "READY"
    },
    {
      "eventId": 12346,
      "eventName": "신규 쿠폰 이벤트 이름 변경1",
      "description": "다른 회사와 협업 변경1",
      "fromAt": "2025-06-21T09:00:00",
      "untilAt": "2025-06-21T21:00:00",
      "eventStatus": "READY"
    }
  ]
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드              | 메시지                 | 발생 조건 예시    |
|--------|------------------------|---------------------|-------------|
| 400    | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.   | 필수 값 누락, 데이터 형식 오류 |
| 401    | `UNAUTHORIZED`          | 인증 정보가 없습니다.        | JWT 누락 또는 만료 |
| 403    | `FORBIDDEN`             | 관리자 권한이 필요합니다.      | `ROLE_ADMIN` 미보유 |
| 500    | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다.   | DB 예외, 알 수 없는 예외 등 |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "code": "INTERNAL_SERVER_ERROR",
  "message": "이벤트를 찾을 수 없습니다.",
  "path": "/api/v1/admin/events"
}
```


---




