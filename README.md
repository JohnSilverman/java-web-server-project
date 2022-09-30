# java-was-2022
Java Web Application Server 2022

## 개요
2022년 9월 한달동안 JAVA로 웹서버 개발하기 프로젝트<br>
Spring 등의 웹 프레임워크를 이용하지 않고 순수 자바로 만드는 것이 목표

## 기능
### 정적인 HTML 파일 응답
http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### MIME 지원
지금까지 구현한 소스 코드는 stylesheet 파일을 지원하지 못하고 있다. stylesheet 파일을 지원하도록 구현하도록 한다.

### POST로 회원가입 구현
http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.<br>
가입 후 페이지 이동을 위해 redirection 기능을 구현한다.

### COOKIE를 이용한 로그인 구현
“로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.<br>
로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.

앞에서 회원가입한 사용자로 로그인할 수 있어야 한다.<br>
로그인이 성공하면 cookie를 활용해 로그인 상태를 유지할 수 있어야 한다.<br>
로그인이 성공할 경우 요청 header의 Cookie header에 토큰이 전달되어야 한다.

### 동적인 HTML 구현
접근하고 있는 사용자가 “로그인” 상태일 경우(cookie 값이 logined=true) 경우 http://localhost:8080/user/list.html 에서 사용자 목록을 출력한다.<br>
만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.

### Hibernate 연동
인메모리가 아닌 정말 외부 DB에 데이터를 저장해본다.

### Memo 기능 추가
간단한 메모를 업로드하고 조회하는 기능을 추가한다.
