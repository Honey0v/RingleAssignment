# 프로젝트 이름: RingleAssignment

## 개요
RingleAssignment 프로젝트는 튜터와 학생 간의 수업 스케줄 관리 및 예약 시스템을 구현하기 위해 설계되었습니다. 이 시스템은 다음 주요 기능을 제공합니다:
- 튜터가 제공할 수 있는 수업 시간대를 설정.
- 학생이 특정 시간대에 수업을 예약.
- 동시성 문제를 고려한 수업 예약 처리.

---

## 실행 방법

### 1. 환경 설정

#### 필수 요구 사항:
- **Java 17 이상**
- **Docker 및 Docker Compose**
- **Postman 또는 curl 명령어를 통한 API 테스트 도구**

#### 프로젝트 클론
```bash
git clone https://github.com/Honey0v/RingleAssignment.git
cd RingleAssignment
```

### 2. 데이터베이스 및 애플리케이션 실행

#### Docker를 이용한 데이터베이스 초기화
```bash
docker-compose up -d
```

Docker Compose가 실행되며 MySQL 데이터베이스와 애플리케이션이 초기화됩니다.

#### 애플리케이션 빌드 및 실행
```bash
./gradlew bootRun
```
애플리케이션이 로컬 서버(http://localhost:8080)에서 실행됩니다.

---

## API 사용 방법

### 1. 가능한 수업 시간대 조회
**Endpoint:** `GET /api/study/possible-lesson`
- 요청 파라미터:
    - `date`: 수업 날짜 (예: `2024-12-03`)
    - `lessonDurationMinutes`: 수업 시간 (30 또는 60 분)

예시:
```bash
curl --location 'http://localhost:8080/api/study/possible-lesson' \
--header 'Content-Type: application/json' \
--data '{"date": "2024-12-03", "lessonDurationMinutes": 30}'
```

### 2. 수업 예약
**Endpoint:** `POST /api/study/lesson`
- 요청 본문:
    - `tutorId`: 튜터의 ID
    - `studentId`: 학생의 ID
    - `date`: 예약 날짜
    - `startTime`: 시작 시간
    - `duration`: 수업 시간 (30 또는 60 분)

예시:
```bash
curl --location 'http://localhost:8080/api/study/lesson' \
--header 'Content-Type: application/json' \
--data '{"tutorId": 1, "studentId": 4, "date": "2024-12-03", "startTime": "14:00:00", "duration": 30}'
```

---

## 설계 배경

### 1. 주요 요구사항
- **수업 예약 충돌 방지**: 동일 시간대에 중복된 수업 예약을 방지해야 함.
- **확장성 고려**: 다수의 튜터와 학생이 동시에 접근할 수 있는 시스템 설계.
- **동시성 처리**: 동시 요청을 안전하게 처리할 수 있도록 낙관적 잠금과 데이터베이스 제약 조건을 함께 사용.

### 2. 아키텍처
- **Spring Boot**: 빠르고 효율적인 애플리케이션 개발을 위한 프레임워크.
- **MySQL**: 관계형 데이터베이스로 수업 및 회원 정보를 관리.
- **Docker**: 환경 설정 및 배포 간소화를 위한 컨테이너 기반 실행 환경.
- **JPA 및 Hibernate**: 데이터베이스와 객체 간 매핑 및 동시성 제어.

### 3. 동시성 문제 해결 전략
- **낙관적 잠금(@Version)**:
    - `@Version` 어노테이션을 사용해 동시성 충돌을 감지.
    - 충돌 시 `OptimisticLockException`을 발생시켜 요청을 재시도하거나 적절한 에러 반환.
- **Unique 제약 조건**:
    - `Lesson` 테이블에 유니크 제약 조건을 추가하여 중복 예약을 방지.

---

## 주요 테이블 설계

### `member` 테이블
- **Columns**:
    - `member_id`: Primary Key
    - `name`: 사용자 이름
    - `role`: 역할 (TUTOR, STUDENT)

### `possible_lesson` 테이블
- **Columns**:
    - `possible_lesson_id`: Primary Key
    - `member_id`: Foreign Key (튜터 ID)
    - `start_time`, `end_time`: 수업 가능 시간대

### `lesson` 테이블
- **Columns**:
    - `lesson_id`: Primary Key
    - `tutor_id`: Foreign Key (튜터 ID)
    - `student_id`: Foreign Key (학생 ID)
    - `date`: 예약 날짜
    - `start_time`: 수업 시작 시간
    - `duration`: 수업 시간
    - **Unique Constraint**: 동일 튜터, 날짜, 시작 시간의 중복 예약 방지.

---

## 테스트 방법

### 병렬 요청 테스트
동시성 문제를 확인하려면 아래 명령을 병렬로 실행:
```bash
curl --location 'http://localhost:8080/api/study/lesson' \
--header 'Content-Type: application/json' \
--data '{"tutorId": 1, "studentId": 4, "date": "2024-12-03", "startTime": "14:00:00", "duration": 30}' &

curl --location 'http://localhost:8080/api/study/lesson' \
--header 'Content-Type: application/json' \
--data '{"tutorId": 1, "studentId": 4, "date": "2024-12-03", "startTime": "14:00:00", "duration": 30}' &
```
