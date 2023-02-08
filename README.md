# spring-boot-todo-project

- Spring Boot를 이용한 프로젝트

## 1. 기술 스택 및 툴

- Spring Boot
  - 전체 프로젝트의 틀
- Jpa
  - DB 관련 세팅을 자동으로 하기 위해서 사용
- Redis
  - RefreshToken을 관리하기 위해서 사용
- IntelliJ
  - 통합 개발 환경을 위해 사용
- DBeaver
  - DB 데이터 관리를 위해 사용

## 실행방법
### 2.1 실행 방법 Intellij (macOs 기준)

1. spring-boot-todo-project 저장소에서 소스를 clone한다
2. Mysql를 설치 및 실행 
   - 2.1 터미널에서 brew install mysql 입력해서 설치
   - 2.2 터미널에서 mysql -u root -p 입력 후 비밀번호 입력
   - 2.3 CREATE DATABASE todo_db DEFAULT CHARACTER SET UTF8; 입력
   - 2.4 CREATE USER 'todo'@'%' identified by 'todoProject#'; 입력
   - 2.5 GRANT ALL PRIVILEGES ON todo_db.* TO 'todo'@'%'; 입력
   - 2.6 flush privileges; 입력
   - Mysql 실행
3. Redis 설치 및 실행
   - 터미널에서 brew intall redis 입력해서 설치
   - brew services start redis 입력해서 실행
4. IntelliJ 에서 clons한 spring-boot-todo-project 선택
5. maven clean 및 compile 진행
   - 5.1 라이브러리를 못찾는다면 Intellij File - Invaildate Caches 선택 - Invaildate and restart 클릭
6. Edit Configuration에서 Application 추가
7. Run 실행

### 2.2 Docker

1. spring-boot-todo-project 저장소에서 소스를 clone한다
2. Mysql를 설치 및 실행
    - 2.1 터미널에서 brew install mysql 입력해서 설치
    - 2.2 터미널에서 mysql -u root -p 입력 후 비밀번호 입력
    - 2.3 CREATE DATABASE todo_db DEFAULT CHARACTER SET UTF8; 입력
    - 2.4 CREATE USER 'todo'@'%' identified by 'todoProject#'; 입력
    - 2.5 GRANT ALL PRIVILEGES ON todo_db.* TO 'todo'@'%'; 입력
    - 2.6 flush privileges; 입력
    - Mysql 실행
3. Redis 설치 및 실행
    - 터미널에서 brew intall redis 입력해서 설치
    - brew services start redis 입력해서 실행
4. docker 다운로드
5. spring-boot-todo-project에서 mvn clean package 실행
6. mvn package jar 생성
7. docker-compose up -d 실행

## 3. 주요 기능

1. Users

   - 로그인
     - 입력받은 사용자의 email 및 발급한 token을 redis에 key,value를 저장
     - header에 accessToken, refreshToken을 저장
   - 회원가입
     - Spring Security에서 제공하는 BCryptPasswordEncoder 를 사용하여 비밀번호 암호화 처리
     - 입력받은 email이 존재한다면 회원가입 실패
   - 회원탈퇴
     - 회원탈퇴시 UserNo를 FK로 가지고 있는 테이블은 전체 삭제됨
   - 로그아웃

2. Todo

   - Todo 추가
   - Todo 조회
     - 전체 Todo 및 가장 최근 1개
   - Todo 수정
     - Todo의 상태를 변경
   - Todo 삭제

## 4. 기타

나중에 공통 DTO를 추가할 수 가능성이 있어 Aspect를 사용하여 파라미터에 공통 정보 주입함
    - controller에 요청이 들어올때마다 공통으로 사용하는 UserCmnDto를 상속받은 Dto로 공통 정보 주입함

## 5. 구현시 
