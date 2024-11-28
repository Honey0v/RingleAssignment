-- 데이터베이스 초기화
CREATE DATABASE IF NOT EXISTS ringle;
USE ringle;

-- 멤버 테이블 생성
CREATE TABLE IF NOT EXISTS member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- TUTOR or STUDENT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 가능 수업 테이블 생성
CREATE TABLE IF NOT EXISTS possible_lesson (
    possible_lesson_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE CASCADE
    );

-- 수업 테이블 생성
CREATE TABLE IF NOT EXISTS lesson (
    lesson_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tutor_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    duration INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tutor_id) REFERENCES member (member_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES member (member_id) ON DELETE CASCADE
    );

-- 멤버 초기 데이터 삽입
INSERT INTO member (name, role) VALUES
    ('Tutor A', 'TUTOR'),
    ('Tutor B', 'TUTOR'),
    ('Tutor C', 'TUTOR'),
    ('Student A', 'STUDENT'),
    ('Student B', 'STUDENT');

-- 가능 수업 초기 데이터 삽입
INSERT INTO possible_lesson (member_id, start_date, end_date, start_time, end_time) VALUES
    (1, '2024-12-03', '2024-12-04', '10:00:00', '19:00:00'),
    (2, '2024-12-03', '2024-12-04', '10:00:00', '19:00:00');

-- 수업 초기 데이터 삽입
INSERT INTO lesson (tutor_id, student_id, date, start_time, duration) VALUES
    (1, 4, '2024-12-03', '11:00:00', 30);
