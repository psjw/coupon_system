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

### 📥 Request Body

```json
{
  "eventName": "신규 쿠폰 이벤트",
  "description": "다른 회사와 협업",
  "fromAt": "2025-06-21T09:00:00",
  "untilAt": "2025-06-21T21:00:00",
  "eventStatus": "READY" 
}
```
> ℹ️ `eventStatus` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.


### 📤 Response Body
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
### ▶️ **PUT /api/v1/admin/events/{eventId}**

| 항목       | 설명                          |
|------------|-----------------------------|
| Method     | PUT                         |
| Path       | `/api/v1/admin/events/{eventId}` |
| 인증       | 필요 (Bearer Token)           |
| 설명       | 관리자가 이벤트를 수정함               |

### 📥 Request Body

```json
{
  "eventName": "신규 쿠폰 이벤트 이름 변경",
  "description": "다른 회사와 협업 변경",
  "fromAt": "2025-06-21T09:00:00",
  "untilAt": "2025-06-21T21:00:00",
  "eventStatus": "READY"
}
```
> ℹ️ `eventStatus` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.


### 📤 Response Body
```json
{
  "eventId": 12345,
  "eventName": "신규 쿠폰 이벤트 이름 변경",
  "eventStatus": "READY",
  "updatedAt": "2025-06-21T10:50:00"
}
```


### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                   | 메시지                           | 발생 조건 예시 |
|------|-------------------------|-------------------------------|----------------|
| 400  | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.             | 필수 값 누락, 데이터 형식 오류 |
| 400  | `EVENT_INVALID_DATETIME`      | 이벤트 시작 시간은 종료 시간보다 이전이어야 합니다. | `fromAt > untilAt` |
| 401  | `UNAUTHORIZED`          | 인증 정보가 없습니다.                  | JWT 누락 또는 만료 |
| 404  | `EVENT_NOT_FOUND`       | 이벤트를 찾을 수 없습니다.               | 잘못된 eventId        |
| 403  | `FORBIDDEN`             | 관리자 권한이 필요합니다.                | `ROLE_ADMIN` 미보유 |
| 409  | `DUPLICATE_EVENT`       | 동일한 이름의 이벤트가 이미 실행 중입니다.      | 중복 이벤트 생성 시도 |
| 500  | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다.             | DB 예외, 알 수 없는 예외 등 |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "code": "EVENT_INVALID_DATETIME",
  "message": "이벤트 시작 시간은 종료 시간보다 이전이어야 합니다.",
  "path": "/api/v1/admin/events/12345"
}
```



## ✅ 3. 이벤트 상태 변경
### ▶️ **PUT /api/v1/admin/events/{eventId}/status**

| 항목       | 설명                                 |
|------------|------------------------------------|
| Method     | PUT                                |
| Path       | `/api/v1/admin/events/{eventId}/status` |
| 인증       | 필요 (Bearer Token)                  |
| 설명       | 관리자가 이벤트 상태를 변경함                   |

### 📥 Request Body

```json
{
  "eventStatus": "FINISHED"
}
```
> ℹ️ `eventStatus` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.


### 📤 Response Body
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
### ▶️ **DELETE /api/v1/admin/events/{eventId}**

| 항목       | 설명                          |
|------------|-----------------------------|
| Method     | DELETE                      |
| Path       | `/api/v1/admin/events/{eventId}` |
| 인증       | 필요 (Bearer Token)           |
| 설명       | 관리자가 이벤트를 삭제함           |

### 📥 Request Body

> 없음

### 📤 Response Body
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
### ▶️ **GET /api/v1/admin/events/{eventId}**

| 항목       | 설명                          |
|------------|-----------------------------|
| Method     | GET                         |
| Path       | `/api/v1/admin/events/{eventId}` |
| 인증       | 필요 (Bearer Token)           |
| 설명       | 관리자가 이벤트 정보를 조회함            |

### 📥 Request Body
 
> 없음

### 📤 Response Body
```json
{
  "eventId": 12345,
  "eventName": "신규 쿠폰 이벤트 이름 변경",
  "description": "다른 회사와 협업 변경",
  "fromAt": "2025-06-21T09:00:00",
  "untilAt": "2025-06-21T21:00:00",
  "eventStatus": "READY",
  "createdBy": "admin_user",
  "createdAt": "2025-06-21T08:00:00",
  "updatedBy": "marketing_mgr",
  "updatedAt": "2025-06-21T09:00:00"
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



## ✅ 6. 이벤트 목록 조회
### ▶️ **GET /api/v1/admin/events**

| 항목       | 설명                     |
|------------|------------------------|
| Method     | GET                    |
| Path       | `/api/v1/admin/events` |
| 인증       | 필요 (Bearer Token)      |
| 설명       | 관리자가 이벤트 목록를 조회함       |

### 🔍 요청 쿼리 파라미터

| 기능         | 설명                                              | 쿼리 파라미터 예시                             |
|--------------|---------------------------------------------------|------------------------------------------------|
| 상태 필터링   | 이벤트 상태(`eventStatus`)로 필터링                      | `/events?eventStatus=READY`                    |
| 기간 필터링   | 이벤트 기간(`fromAt`, `untilAt`) 범위 검색              | `/events?from=2025-06-01&to=2025-06-30`        |
| 키워드 검색   | 이벤트 이름 또는 설명 기준 검색                             | `/events?keyword=쿠폰`                         |
| 정렬         | 정렬 기준 필드와 방향 지정 (`createdAt`, `fromAt`, 등) | `/events?sort=createdAt,DESC`                 |
| 페이징       | 페이지 번호 및 페이지 크기 지정                              | `/events?page=0&size=20`                       |
> ℹ️ `from`, `to`는 내부적으로 `fromAt`, `untilAt` 조건과 매핑됩니다.

### 📥 Request Body

> 없음

### 📤 Response Body

```json
{
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1,
  "data": [
    {
      "eventId": 12345,
      "eventName": "신규 쿠폰 이벤트 이름 변경",
      "description": "다른 회사와 협업 변경",
      "fromAt": "2025-06-21T09:00:00",
      "untilAt": "2025-06-21T21:00:00",
      "eventStatus": "READY",
      "createdBy": "admin_user",
      "createdAt": "2025-06-21T08:00:00",
      "updatedBy": "marketing_mgr",
      "updatedAt": "2025-06-21T09:00:00"
    },
    {
      "eventId": 12346,
      "eventName": "신규 쿠폰 이벤트 이름 변경1",
      "description": "다른 회사와 협업 변경1",
      "fromAt": "2025-06-21T09:00:00",
      "untilAt": "2025-06-21T21:00:00",
      "eventStatus": "READY",
      "createdBy": "admin_user",
      "createdAt": "2025-06-21T08:00:00",
      "updatedBy": "marketing_mgr",
      "updatedAt": "2025-06-21T09:00:00"
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
  "message": "서버 내부 오류가 발생했습니다.",
  "path": "/api/v1/admin/coupons"
}
```

---

# 📦 Coupon 도메인 - 쿠폰 API 명세서

## ✅ 1. 쿠폰재고 생성
### ▶️ **POST /api/v1/admin/coupon-inventories**

| 항목       | 설명                      |
|------------|-------------------------|
| Method     | POST                    |
| Path       | `/api/v1/admin/coupon-inventories` |
| 인증       | 필요 (Bearer Token)       |
| 설명       | 관리자가 쿠폰 재고를 생성함         |

### 📥 Request Body

```json
{
  "eventId": 12345,
  "inventoryName": "A쿠폰",
  "usableFromAt": "2025-06-21T09:00:00",
  "usableUntilAt": "2025-06-21T21:00:00",
  "totalCount": 1000000,
  "inventoryStatus": "READY"
}
```

> ℹ️ `inventoryStatus` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.



### 📤 Response Body
```json
{
  "inventoryId" : 123,
  "eventId": 12345,
  "inventoryName": "A쿠폰",
  "totalCount": 1000000,
  "inventoryStatus": "READY"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                       | 메시지                                | 발생 조건 예시                       |
|------|-----------------------------|------------------------------------|--------------------------------|
| 400  | `INVALID_REQUEST`           | 요청 필드가 유효하지 않습니다.                  | 필수 값 누락, 데이터 형식 오류             |
| 400  | `MAX_COUPON_LIMIT_EXCEEDED` | 생성 가능한 최대 쿠폰 수를 초과했습니다.            | 10,000,000 초과 쿠폰 생성시           |
| 400  | `INVENTORY_INVALID_PERIOD`          | 쿠폰 유효기간의 시작 시간은 종료 시간보다 이전이어야 합니다. | `usableFromAt > usableUntilAt` |
| 401  | `UNAUTHORIZED`              | 인증 정보가 없습니다.                       | JWT 누락 또는 만료                   |
| 403  | `FORBIDDEN`                 | 관리자 권한이 필요합니다.                     | `ROLE_ADMIN` 미보유               |
| 404    | `EVENT_NOT_FOUND`           | 이벤트를 찾을 수 없습니다.                    | 잘못된 eventId                    |
| 500  | `INTERNAL_SERVER_ERROR`     | 서버 내부 오류가 발생했습니다.                  | DB 예외, 알 수 없는 예외 등             |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "code": "MAX_COUPON_LIMIT_EXCEEDED",
  "message": "생성 가능한 최대 쿠폰 수를 초과했습니다",
  "path": "/api/v1/admin/coupon-inventories"
}
```
---


## ✅ 2. 쿠폰재고 수정
### ▶️ **PUT /api/v1/admin/coupon-inventories/{inventoryId}**

| 항목       | 설명                           |
|------------|------------------------------|
| Method     | PUT                          |
| Path       | `/api/v1/admin/coupon-inventories/{inventoryId}` |
| 인증       | 필요 (Bearer Token)            |
| 설명       | 관리자가 쿠폰 재고를 생성함              |

### 📥 Request Body

```json
{
  "eventId": 12345,
  "inventoryName": "A쿠폰",
  "usableFromAt": "2025-06-21T09:00:00",
  "usableUntilAt": "2025-06-21T21:00:00",
  "totalCount": 1000000,
  "inventoryStatus": "READY"
}
```

> ℹ️ `inventoryStatus` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.


### 📤 Response Body
```json
{
  "inventoryId" : 123,
  "eventId": 12345,
  "inventoryName": "A쿠폰",
  "totalCount": 1000000,
  "usableFromAt": "2025-06-21T09:00:00",
  "usableUntilAt": "2025-06-21T21:00:00",
  "inventoryStatus": "READY"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                        | 메시지                                | 발생 조건 예시                      |
|------|------------------------------|------------------------------------|-------------------------------|
| 400  | `INVALID_REQUEST`            | 요청 필드가 유효하지 않습니다.                  | 필수 값 누락, 데이터 형식 오류            |
| 400  | `MAX_COUPON_LIMIT_EXCEEDED`  | 생성 가능한 최대 쿠폰 수를 초과했습니다.            | 10,000,000 초과 쿠폰 생성시          |
| 400  | `INVENTORY_INVALID_PERIOD`   | 쿠폰 유효기간의 시작 시간은 종료 시간보다 이전이어야 합니다. | `usableFromAt > usableUntilAt` |
| 400    | `CANNOT_MODIFY_COUPON_INVENTORY` | 해당 쿠폰 재고는 수정할 수 없습니다.              | 예: ONGOING, END |
| 401  | `UNAUTHORIZED`               | 인증 정보가 없습니다.                       | JWT 누락 또는 만료                  |
| 403  | `FORBIDDEN`                  | 관리자 권한이 필요합니다.                     | `ROLE_ADMIN` 미보유              |
| 404    | `EVENT_NOT_FOUND`            | 이벤트를 찾을 수 없습니다.                    | 잘못된 eventId                   |
| 404    | `COUPON_INVENTORY_NOT_FOUND` | 쿠폰 재고를 찾을 수 없습니다.                  | 잘못된 inventoryId               |
| 500  | `INTERNAL_SERVER_ERROR`      | 서버 내부 오류가 발생했습니다.                  | DB 예외, 알 수 없는 예외 등            |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "code": "MAX_COUPON_LIMIT_EXCEEDED",
  "message": "생성 가능한 최대 쿠폰 수를 초과했습니다",
  "path": "/api/v1/admin/coupon-inventories/123"
}
```
---


## ✅ 3. 쿠폰 재고 삭제
### ▶️ **DELETE /api/v1/admin/coupon-inventories/{inventoryId}**

| 항목       | 설명                           |
|------------|------------------------------|
| Method     | DELETE                       |
| Path       | `/api/v1/admin/coupon-inventories/{inventoryId}` |
| 인증       | 필요 (Bearer Token)            |
| 설명       | 관리자가 쿠폰 재고를 삭제함              |

### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "inventoryId": 123,
  "deletedAt": "2025-06-21T10:50:00"
}
```


### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드                        | 메시지                  | 발생 조건 예시           |
|--------|------------------------------|----------------------|--------------------|
| 400    | `INVALID_REQUEST`            | 요청 필드가 유효하지 않습니다.    | 필수 값 누락, 데이터 형식 오류 |
| 400    | `CANNOT_DELETE_COUPON_INVENTORY` | 이미 생성된 쿠폰 재고는 삭제할 수 없습니다. | 예: ONGOING, END    |
| 401    | `UNAUTHORIZED`               | 인증 정보가 없습니다.         | JWT 누락 또는 만료       |
| 403    | `FORBIDDEN`                  | 관리자 권한이 필요합니다.       | `ROLE_ADMIN` 미보유   |
| 404    | `COUPON_INVENTORY_NOT_FOUND` | 쿠폰 재고를 찾을 수 없습니다.                  | 잘못된 inventoryId    |
| 500    | `INTERNAL_SERVER_ERROR`      | 서버 내부 오류가 발생했습니다.    | DB 예외, 알 수 없는 예외 등 |

--- 

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "COUPON_INVENTORY_NOT_FOUND",
  "message": "쿠폰 재고를 찾을 수 없습니다.",
  "path": "/api/v1/admin/coupon-inventories/123"
}
```
---

## ✅ 4. 쿠폰 재고 단건 조회
### ▶️ **GET /api/v1/admin/coupon-inventories/{inventoryId}**

| 항목       | 설명                           |
|------------|------------------------------|
| Method     | GET                          |
| Path       | `/api/v1/admin/coupon-inventories /{inventoryId}` |
| 인증       | 필요 (Bearer Token)            |
| 설명       | 관리자가 쿠폰 재고 정보를 조회함           |

### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "inventoryId": 123,
  "inventoryName": "A쿠폰",
  "eventId": 12345,
  "totalCount": 1000000,
  "issuedCount": 452390,
  "inventoryStatus": "READY",
  "usableFromAt": "2025-06-21T09:00:00",
  "usableUntilAt": "2025-06-21T21:00:00",
  "createdBy": "admin_user",
  "createdAt": "2025-06-21T08:00:00",
  "updatedBy": "marketing_mgr",
  "updatedAt": "2025-06-21T09:00:00"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드              | 메시지                 | 발생 조건 예시    |
|--------|------------------------|---------------------|-------------|
| 400    | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.   | 필수 값 누락, 데이터 형식 오류 |
| 401    | `UNAUTHORIZED`          | 인증 정보가 없습니다.        | JWT 누락 또는 만료 |
| 403    | `FORBIDDEN`             | 관리자 권한이 필요합니다.      | `ROLE_ADMIN` 미보유 |
| 404    | `EVENT_NOT_FOUND`       | 이벤트를 찾을 수 없습니다.     | 잘못된 eventId |
| 404    | `COUPON_INVENTORY_NOT_FOUND` | 쿠폰 재고를 찾을 수 없습니다.                  | 잘못된 inventoryId    |
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
  "path": "/api/v1/admin/coupon-inventories/12345"
}
```

---


## ✅ 5. 쿠폰 재고 목록 조회
### ▶️ **GET /api/v1/admin/coupon-inventories**

| 항목     | 설명                                       |
|----------|--------------------------------------------|
| Method   | GET                                        |
| Path     | `/api/v1/admin/coupon-inventories`         |
| 인증     | 필요 (Bearer Token)                        |
| 설명     | 관리자가 쿠폰 재고 목록을 조회합니다.      |


### 🔍 요청 쿼리 파라미터

| 기능         | 설명                                         | 쿼리 파라미터 예시                                            |
|--------------|----------------------------------------------|---------------------------------------------------------------|
| 상태 필터링   | 재고 상태(`inventoryStatus`) 기준 필터링         | `/coupon-inventories?inventoryStatus=READY`                   |
| 기간 필터링   | 사용 시작일(`usableFromAt`)과 종료일(`usableUntilAt`) 범위 검색 | `/coupon-inventories?from=2025-06-01&to=2025-06-30`            |
| 키워드 검색   | 이름 또는 설명 기준 검색                          | `/coupon-inventories?keyword=A쿠폰`                            |
| 정렬         | 정렬 기준 필드와 방향 지정                         | `/coupon-inventories?sort=createdAt,DESC`                     |
| 페이징       | 페이지 번호 및 페이지 크기 지정                    | `/coupon-inventories?page=0&size=20`                          |
> ℹ️ `from`, `to`는 내부적으로 `usableFromAt`, `usableUntilAt` 조건과 매핑됩니다.


### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1,
  "data": [
    {
      "inventoryId": 123,
      "eventId": 12345,
      "totalCount": 1000000,
      "issuedCount": 452390,
      "inventoryStatus": "READY",
      "usableFromAt": "2025-06-21T09:00:00",
      "usableUntilAt": "2025-06-21T21:00:00",
      "createdBy": "admin_user",
      "createdAt": "2025-06-21T08:00:00",
      "updatedBy": "marketing_mgr",
      "updatedAt": "2025-06-21T09:00:00"
    },
    {
      "inventoryId": 124,
      "eventId": 12345,
      "totalCount": 50000,
      "issuedCount": 3200,
      "inventoryStatus": "COMPLETED",
      "usableFromAt": "2025-06-01T00:00:00",
      "usableUntilAt": "2025-06-10T23:59:59",
      "createdBy": "admin_user",
      "createdAt": "2025-06-21T08:00:00",
      "updatedBy": "marketing_mgr",
      "updatedAt": "2025-06-21T09:00:00"
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
  "message": "서버 내부 오류가 발생했습니다.",
  "path": "/api/v1/admin/coupon-inventories"
}
```
---



## ✅ 6. 쿠폰 재고 회차 생성
### ▶️ **POST /api/v1/admin/coupon-batches**

| 항목       | 설명                            |
|------------|-------------------------------|
| Method     | POST                          |
| Path       | `/api/v1/admin/coupon-batches` |
| 인증       | 필요 (Bearer Token)             |
| 설명       | 관리자가 쿠폰 재고 회차를 생성함            |

### 📥 Request Body

```json
{
  "inventoryId": 1234,
  "batchName": "A쿠폰 1회차",
  "issueCount": 100000,
  "issueType": "SELF",
  "validFromAt": "2025-06-21T09:00:00",
  "validUntilAt": "2025-06-21T21:00:00",
  "batchStatus": "READY"
}
```

> ℹ️ `batchStatus`, `issueType` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.



### 📤 Response Body
```json
{
  "batchId": 234,
  "inventoryId": 1234,
  "batchName": "A쿠폰 1회차",
  "issueCount": 100000,
  "issueType": "SELF",
  "validFromAt": "2025-06-21T09:00:00",
  "validUntilAt": "2025-06-21T21:00:00",
  "batchStatus": "READY"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                       | 메시지                                | 발생 조건 예시                            |
|------|-----------------------------|------------------------------------|-------------------------------------|
| 400  | `INVALID_REQUEST`           | 요청 필드가 유효하지 않습니다.                  | 필수 값 누락, 데이터 형식 오류                  |
| 400  | `MAX_COUPON_LIMIT_EXCEEDED` | 생성 가능한 최대 쿠폰 수를 초과했습니다.            | CouponInventory totalCount 초과 쿠폰 생성시 |
| 400  | `BATCH_INVALID_PERIOD`      | 쿠폰 유효기간의 시작 시간은 종료 시간보다 이전이어야 합니다. | `validFromAt > validUntilAt`        |
| 401  | `UNAUTHORIZED`              | 인증 정보가 없습니다.                       | JWT 누락 또는 만료                        |
| 403  | `FORBIDDEN`                 | 관리자 권한이 필요합니다.                     | `ROLE_ADMIN` 미보유                    |
| 404    | `INVENTORY_NOT_FOUND`       | 쿠폰 재고를 찾을 수 없습니다.                  | 잘못된 inventoryId                     |
| 500  | `INTERNAL_SERVER_ERROR`     | 서버 내부 오류가 발생했습니다.                  | DB 예외, 알 수 없는 예외 등                  |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "code": "MAX_COUPON_LIMIT_EXCEEDED",
  "message": "생성 가능한 최대 쿠폰 수를 초과했습니다",
  "path": "/api/v1/admin/coupon-batches"
}
```
---


## ✅ 7. 쿠폰 재고 회차 수정
### ▶️ **PUT /api/v1/admin/coupon-batches/{batchId}**

| 항목       | 설명                                  |
|------------|-------------------------------------|
| Method     | PUT                                 |
| Path       | `/api/v1/admin/coupon-batches/{batchId}` |
| 인증       | 필요 (Bearer Token)                   |
| 설명       | 관리자가 쿠폰 재고 회차를 수정함                  |

### 📥 Request Body

```json
{
  "batchName": "A쿠폰 1회차",
  "issueCount": 100000,
  "issueType": "SELF",
  "validFromAt": "2025-06-21T09:00:00",
  "validUntilAt": "2025-06-21T21:00:00",
  "batchStatus": "READY"
}
```
> ℹ️ `batchStatus`, `issueType` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.


### 📤 Response Body
```json
{
  "batchId": 234,
  "inventoryId": 1234,
  "batchName": "A쿠폰 1회차",
  "issueCount": 100000,
  "issueType": "SELF",
  "validFromAt": "2025-06-21T09:00:00",
  "validUntilAt": "2025-06-21T21:00:00",
  "batchStatus": "READY"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                        | 메시지                                | 발생 조건 예시                      |
|------|------------------------------|------------------------------------|-------------------------------|
| 400  | `INVALID_REQUEST`            | 요청 필드가 유효하지 않습니다.                  | 필수 값 누락, 데이터 형식 오류            |
| 400  | `MAX_COUPON_LIMIT_EXCEEDED` | 생성 가능한 최대 쿠폰 수를 초과했습니다.            | CouponInventory totalCount 초과 쿠폰 생성시 |
| 400  | `BATCH_INVALID_PERIOD`      | 쿠폰 유효기간의 시작 시간은 종료 시간보다 이전이어야 합니다. | `validFromAt > validUntilAt`        |
| 400    | `CANNOT_MODIFY_COUPON_BATCH` | 해당 회차는 수정할 수 없습니다.                 | 예: READY, DISABLED |
| 401  | `UNAUTHORIZED`               | 인증 정보가 없습니다.                       | JWT 누락 또는 만료                  |
| 403  | `FORBIDDEN`                  | 관리자 권한이 필요합니다.                     | `ROLE_ADMIN` 미보유              |
| 404    | `COUPON_INVENTORY_NOT_FOUND` | 쿠폰 재고를 찾을 수 없습니다.                  | 잘못된 inventoryId               |
| 500  | `INTERNAL_SERVER_ERROR`      | 서버 내부 오류가 발생했습니다.                  | DB 예외, 알 수 없는 예외 등            |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "code": "CANNOT_MODIFY_COUPON_BATCH",
  "message": "해당 회차는 수정할 수 없습니다.",
  "path": "/api/v1/admin/coupon-batches/234"
}
```

---




## ✅ 8. 쿠폰 재고 회차 삭제
### ▶️ **DELETE /api/v1/admin/coupon-batches/{batchId}**

| 항목     | 설명                                  |
|----------|-------------------------------------|
| Method   | DELETE                              |
| Path     | `/api/v1/admin/coupon-batches/{batchId}` |
| 인증     | 필요 (Bearer Token)                   |
| 설명     | 관리자가 특정 쿠폰 재고 회차를 삭제합니다.            |

### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "batchId": 234,
  "deletedAt": "2025-06-21T10:50:00"
}
```


### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드                            | 메시지                       | 발생 조건 예시           |
|--------|----------------------------------|---------------------------|--------------------|
| 400    | `INVALID_REQUEST`                | 요청 필드가 유효하지 않습니다.         | 필수 값 누락, 데이터 형식 오류 |
| 400    | `CANNOT_MODIFY_COUPON_BATCH` | 해당 회차는 수정할 수 없습니다.                 | 예: READY, DISABLED |
| 401    | `UNAUTHORIZED`                   | 인증 정보가 없습니다.              | JWT 누락 또는 만료       |
| 403    | `FORBIDDEN`                      | 관리자 권한이 필요합니다.            | `ROLE_ADMIN` 미보유   |
| 404    | `COUPON_BATCH_NOT_FOUND`         | 쿠폰 회차를 찾을 수 없습니다.         | 잘못된 batchId        |
| 500    | `INTERNAL_SERVER_ERROR`          | 서버 내부 오류가 발생했습니다.         | DB 예외, 알 수 없는 예외 등 |

--- 

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "COUPON_BATCH_NOT_FOUND",
  "message": "쿠폰 회차를 찾을 수 없습니다.",
  "path": "/api/v1/admin/coupon-batches/234"
}
```
---




## ✅ 9. 쿠폰 재고 회차 단건 조회
### ▶️ **GET /api/v1/admin/coupon-batches/{batchId}**

3| 항목       | 설명                           |
|------------|------------------------------|
| Method     | GET                          |
| Path       | `/api/v1/admin/coupon-batches/{batchId}` |
| 인증       | 필요 (Bearer Token)            |
| 설명       | 관리자가 쿠폰 특정 재고 회차 정보를 조회함           |

### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "batchId": 234,
  "inventoryId": 1234,
  "batchName": "A쿠폰(1회차)",
  "batchNumber": 1,
  "issueCount": 10000,
  "issueType": "SELF",
  "validFromAt": "2025-06-21T09:00:00",
  "validUntilAt": "2025-06-21T21:00:00",
  "batchStatus": "READY",
  "createdAt": "2025-06-21T08:00:00",
  "updatedAt": "2025-06-21T09:00:00"
}
```

### ❗ 예외 상황 및 상태 코드

| 상태코드   | 에러 코드              | 메시지                 | 발생 조건 예시    |
|--------|------------------------|---------------------|-------------|
| 400    | `INVALID_REQUEST`       | 요청 필드가 유효하지 않습니다.   | 필수 값 누락, 데이터 형식 오류 |
| 401    | `UNAUTHORIZED`          | 인증 정보가 없습니다.        | JWT 누락 또는 만료 |
| 403    | `FORBIDDEN`             | 관리자 권한이 필요합니다.      | `ROLE_ADMIN` 미보유 |
| 404    | `EVENT_NOT_FOUND`       | 이벤트를 찾을 수 없습니다.     | 잘못된 eventId |
| 404    | `COUPON_INVENTORY_NOT_FOUND` | 쿠폰 재고를 찾을 수 없습니다.                  | 잘못된 inventoryId    |
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
  "path": "/api/v1/admin/coupon-inventories/12345"
}
```

---




## ✅ 10. 쿠폰 재고 회차 목록 조회
### ▶️ **GET /api/v1/admin/coupon-batches**

| 항목     | 설명                                     |
|----------|------------------------------------------|
| Method   | GET                                      |
| Path     | `/api/v1/admin/coupon-batches`           |
| 인증     | 필요 (Bearer Token)                      |
| 설명     | 관리자가 쿠폰 재고 회차 목록을 조회합니다. |

### 🔍 요청 쿼리 파라미터

| 기능         | 설명                                 | 쿼리 파라미터 예시                                       |
|--------------|--------------------------------------|----------------------------------------------------------|
| 상태 필터링   | 회차 상태(`batchStatus`)로 필터링          | `/coupon-batches?batchStatus=READY`                     |
| 기간 필터링   | `validFromAt`, `validUntilAt` 범위 검색 | `/coupon-batches?from=2025-06-01&to=2025-06-30`         |
| 키워드 검색   | 이름 또는 설명에 대한 키워드 검색             | `/coupon-batches?keyword=A쿠폰`                         |
| 정렬         | 정렬 기준 필드와 방향 지정                 | `/coupon-batches?sort=createdAt,DESC`                   |
| 페이징       | 페이지 번호와 크기 지정                   | `/coupon-batches?page=0&size=20`                        |
> ℹ️ `from`, `to`는 내부적으로 `validFromAt`, `validUntilAt` 조건과 매핑됩니다.


### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1,
  "data": [
    {
      "batchId": 234,
      "inventoryId": 1234,
      "batchName": "A쿠폰(1회차)",
      "batchNumber": 1,
      "issueCount": 10000,
      "issueType": "SELF",
      "validFromAt": "2025-06-21T09:00:00",
      "validUntilAt": "2025-06-21T21:00:00",
      "batchStatus": "READY",
      "createdAt": "2025-06-21T08:00:00",
      "updatedAt": "2025-06-21T09:00:00"
    },
    {
      "batchId": 235,
      "inventoryId": 1234,
      "batchName": "B쿠폰(2회차)",
      "batchNumber": 2,
      "issueCount": 5000,
      "issueType": "AUTO",
      "validFromAt": "2025-06-22T09:00:00",
      "validUntilAt": "2025-06-22T21:00:00",
      "batchStatus": "DISABLED",
      "createdAt": "2025-06-22T08:00:00",
      "updatedAt": "2025-06-22T09:00:00"
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
  "message": "서버 내부 오류가 발생했습니다.",
  "path": "/api/v1/admin/coupon-batches"
}
```
---


## ✅ 11. 쿠폰 일괄 자동 생성
### ▶️ **POST /api/v1/admin/coupon-batches/{batchId}/generate-coupons**

| 항목       | 설명                                                        |
|------------|-----------------------------------------------------------|
| Method     | POST                                                      |
| Path       | `/api/v1/admin/coupon-batches/{batchId}/generate-coupons` |
| 인증       | 필요 (Bearer Token)                                         |
| 설명       | 관리자가 내부 쿠폰을 일괄 생성함                                        |

### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "jobId": "1457689783921571840",
  "jobType": "COUPON_GENERATE",
  "jobStatus": "PENDING",
  "message": "쿠폰 생성 작업이 수락되었습니다. 상태는 jobId를 통해 조회하세요."
}
```
> ℹ️ `jobStatus`, `jobType` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.


### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                           | 메시지                      | 발생 조건 예시                 |
|------|---------------------------------|--------------------------|--------------------------|
| 400  | `INVALID_REQUEST`               | 요청 필드가 유효하지 않습니다.        | 필수 값 누락, 데이터 형식 오류       |
| 401  | `UNAUTHORIZED`                  | 인증 정보가 없습니다.             | JWT 누락 또는 만료             |
| 403  | `FORBIDDEN`                     | 관리자 권한이 필요합니다.           | `ROLE_ADMIN` 미보유         |
| 404  | `BATCH_NOT_FOUND`               | 쿠폰 재고 회차를 찾을 수 없습니다.     | 잘못된 batchId              |
| 498  | `DUPLICATE_BATCH_JOB_REQUEST`   | 이미 실행 중인 쿠폰 생성작업이 존재합니다. | 동일한 batchId로 실행중인 쿠폰이 존재 |
| 500  | `INTERNAL_SERVER_ERROR`         | 서버 내부 오류가 발생했습니다.        | DB 예외, 알 수 없는 예외 등       |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "BATCH_NOT_FOUND",
  "message": "쿠폰 재고 회차를 찾을 수 없습니다.",
  "path": "/api/v1/admin/coupon-batches/234/generate-coupons"
}
```
---

## ✅ 12. 쿠폰 생성 작업 상태 조회
### ▶️ **GET /api/v1/admin/coupon-jobs/{jobId}**

| 항목       | 설명                                  |
|------------|-------------------------------------|
| Method     | POST                                |
| Path       | `/api/v1/admin/coupon-jobs/{jobId}` |
| 인증       | 필요 (Bearer Token)                   |
| 설명       | 관리자가 쿠폰 생성 작업의 상태를 조회함              |

### 📥 Request Body

> 없음

### 📤 Response Body
```json
{
  "jobId": "1457689783921571840",
  "jobType": "COUPON_GENERATE",
  "jobStatus": "IN_PROGRESS",
  "createdAt": "2025-06-22T14:00:00",
  "updatedAt": "2025-06-22T14:01:30",
  "message": "쿠폰 생성이 진행 중입니다."
}
```
> ℹ️ `jobStatus`, `jobType` 값은 [공통 Type 및 Status 정의](#-공통-type-및-status-정의)를 참조하세요.


### ❗ 예외 상황 및 상태 코드

| 상태코드 | 에러 코드                  | 메시지                  | 발생 조건 예시           |
|------|------------------------|----------------------|--------------------|
| 400  | `INVALID_REQUEST`      | 요청 필드가 유효하지 않습니다.    | 필수 값 누락, 데이터 형식 오류 |
| 401  | `UNAUTHORIZED`         | 인증 정보가 없습니다.         | JWT 누락 또는 만료       |
| 403  | `FORBIDDEN`            | 관리자 권한이 필요합니다.       | `ROLE_ADMIN` 미보유   |
| 404    | `JOB_NOT_FOUND`       | 쿠폰 생성 작업을 찾을 수 없습니다. | 잘못된 jobId          |
| 500  | `INTERNAL_SERVER_ERROR` | 서버 내부 오류가 발생했습니다.    | DB 예외, 알 수 없는 예외 등 |

---

### 📤 에러 응답 예시

```json
{
  "timestamp": "2025-06-21T10:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "JOB_NOT_FOUND",
  "message": "쿠폰 생성 작업을 찾을 수 없습니다.",
  "path": "/api/v1/admin/coupon-jobs/1457689783921571840"
}
```
---


# 📘 공통 Type 및 Status 정의

## ✅ EventStatus

| 상태        | 설명                              |
|-------------|-----------------------------------|
| READY       | 이벤트 시작 전 대기 상태                 |
| ONGOING     | 이벤트가 현재 진행 중인 상태             |
| COMPLETED   | 이벤트가 정상적으로 종료된 상태           |
| DISABLED    | 관리자가 비활성화한 상태 (삭제 또는 중단)     |


---

## ✅ InventoryStatus

| 상태      | 설명                                |
|-----------|-------------------------------------|
| READY     | 쿠폰 발급 준비가 완료된 상태                  |
| ONGOING   | 발급이 진행 중인 상태                        |
| END       | 발급이 완료되어 종료된 상태                  |

---

## ✅ BatchStatus

| 상태        | 설명                                          |
|-------------|-----------------------------------------------|
| PENDING     | 회차 정보가 생성되었지만 아직 활성화되지 않은 상태             |
| READY       | 회차가 발급 준비 완료된 상태                          |
| DISABLED    | 관리자가 회차를 비활성화하여 더 이상 발급되지 않는 상태         |

---

## ✅ JobStatus

| 상태        | 설명                                        |
|-------------|---------------------------------------------|
| PENDING     | 작업이 대기 중인 상태                      |
| IN_PROGRESS | 현재 작업이 처리 중인 상태                       |
| COMPLETED   | 작업이 성공적으로 완료된 상태                     |
| FAILED      | 작업 처리 중 오류로 인해 실패한 상태               |
| CANCELLED   | 관리자가 수동으로 작업을 중단하거나 취소한 상태         |


## ✅ CodeStatus

| 상태      | 설명                                                       |
|-----------|------------------------------------------------------------|
| AVAILABLE | 발급되지 않고 재고로 존재하는 쿠폰 코드 (사용 가능)                  |
| ISSUED    | 사용자에게 발급된 쿠폰 코드 (아직 사용되지 않음)                      |
| USED      | 사용자가 실제로 쿠폰을 사용한 상태                                  |
| EXPIRED   | 유효 기간이 지나 사용 불가능해진 상태 (자동 만료 처리됨)                   |



## ✅ IssueStatus

| 상태      | 설명                                                 |
|-----------|------------------------------------------------------|
| PENDING   | 발급 요청이 접수되어 대기 중인 상태                               |
| COMPLETED | 쿠폰 발급이 성공적으로 완료된 상태                              |
| CANCELED  | 사용자의 요청 또는 시스템 정책에 따라 발급 요청이 취소된 상태             |
| EXPIRED   | 유효 기간이 지나 발급이 무효 처리된 상태                          |


---

## ✅ MappingStatus

| 상태    | 설명                                      |
|---------|-------------------------------------------|
| MAPPED  | 외부 시스템(제휴사 등)과의 매핑이 정상적으로 완료된 상태           |
| FAILED  | 매핑 처리 중 오류가 발생하여 실패한 상태                    |

---


## ✅ UserChannel

| 채널      | 설명                                 |
|-----------|--------------------------------------|
| WEB       | 웹사이트 또는 웹 기반 플랫폼에서 유입된 사용자            |
| APP       | 모바일 앱(Android, iOS)에서 유입된 사용자          |
| OFFLINE   | 오프라인 매장, 이벤트 현장 등에서 유입된 사용자         |



---


## ✅ JobType

| 유형                    | 설명                                 |
|-------------------------|--------------------------------------|
| COUPON_GENERATION       | 내부 시스템을 통한 대량 쿠폰 생성 작업                |
| PARTNER_COUPON_UPLOAD   | 제휴사로부터 받은 쿠폰 데이터를 업로드 및 등록하는 작업     |

---

## ✅ IssueType

| 유형     | 설명                                                 |
|----------|------------------------------------------------------|
| SELF     | 사용자가 직접 앱/웹 등을 통해 요청하여 발급받는 쿠폰                  |
| MAPPED   | 제휴사 또는 내부 정책에 따라 특정 사용자에게 자동 또는 수동으로 발급되는 쿠폰 |

---


## ✅ PrizeType

| 유형    | 설명     |
|---------|----------|
| POINT   | 포인트 보상 |
| COUPON  | 쿠폰 보상  |


---
