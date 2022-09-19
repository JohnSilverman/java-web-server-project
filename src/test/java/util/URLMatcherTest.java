package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class URLMatcherTest {
    @Test
    void matchPathSuccessTest() {
        String requestPath = "/user/123";
        String pathPrefix = "/user/{id}";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("123", pathVariables.get("id"));
    }

    @Test
    @DisplayName("매칭되지 않는 경우 null을 잘 리턴하는지 체크합니다.")
    void matchPathNotMatchTest() {
        String requestPath = "/users/123";
        String pathPrefix = "/user/{id}";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNull(pathVariables);
    }

    @Test
    @DisplayName("정적 파일 제공이랑 동일한 상황 테스트입니다.")
    void matchPathStaticFileServingTest() {
        String requestPath = "/dir1/dir2/dir3/file.extension";
        String pathPrefix = "/{filePath}";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("dir1/dir2/dir3/file.extension", pathVariables.get("filePath"));
    }

    @Test
    @DisplayName("http://호스트/ 로 접속했을때 테스트입니다.")
    void matchPathHomepageTest() {
        String requestPath = "/";
        String pathPrefix = "/{filePath}";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("", pathVariables.get("filePath"));
    }

    @Test
    @DisplayName("여러 path variables를 잘 리턴하는지 체크합니다.")
    void matchPathMultipleVariables() {
        String requestPath = "/user/5/age/young";
        String pathPrefix = "/user/{id}/age/{age}";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("5", pathVariables.get("id"));
        assertEquals("young", pathVariables.get("age"));
    }

    @Test
    @DisplayName("슬래시 개수가 다른 복잡한 경우에도 path variables를 잘 리턴하는지 체크합니다.")
    void matchPathComplexSlash() {
        String requestPath = "/user/5/age/young/comments";
        String pathPrefix = "/user/{info}";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("5/age/young/comments", pathVariables.get("info"));
    }

    @Test
    @DisplayName("pathVariable의 구분이 애매모호할 때 어떻게 처리하는지 점검합니다. Path Variable이 맨끝에 있으면 남은 문자열을 모두 매칭하고 중간에 있으면 슬래시 기준으로 한토막만 매칭합니다.")
    void matchPathAmbiguity() {
        String requestPath = "/user/5/age/young/22/comments";
        String pathPrefix = "/user/{age}/{info}";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("5", pathVariables.get("age"));
        assertEquals("age/young/22/comments", pathVariables.get("info"));
    }

    @Test
    @DisplayName("정규화가 잘 진행되는지 테스트합니다. 정규화는 url의 양쪽 끝 슬래시 여부를 통일하는 걸 의미합니다.")
    void matchPathRegularizationTest(){
        String requestPath = "/user/";
        String pathPrefix = "user";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
    }

    @Test
    @DisplayName("/api/user/list 와 /api/user 는 당연히 매치 실패해야됨")
    void matchPathFailureTest(){
        String requestPath = "/api/user";
        String pathPrefix = "/api/user/list";
        Map<String,String> pathVariables = URLMatcher.matchPath(requestPath, pathPrefix);
        assertNull(pathVariables);
    }
}
