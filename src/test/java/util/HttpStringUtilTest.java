package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpStringUtil;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

class HttpStringUtilTest {
    @Test
    void parseQueryString() {
        Map<String,String> testResult = HttpStringUtil.parseQueryString("a=1&b=2&c=3");
        assertEquals("1",testResult.get("a"));
        assertEquals("2",testResult.get("b"));
        assertEquals("3",testResult.get("c"));
    }

    @Test
    void inputStreamToLines() {
        String inputString = "abc\ndef\nghi\n\njklmnop";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        List<String> testResult = HttpStringUtil.inputStreamToLines(inputStream);

        assertEquals( 5,testResult.size());
        assertEquals( "abc",testResult.get(0));
        assertEquals("",testResult.get(3));
    }

    @Test
    void matchPathSuccessTest() {
        String requestPath = "/user/123";
        String pathPrefix = "/user/{id}";
        Map<String,String> pathVariables = HttpStringUtil.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("123", pathVariables.get("id"));
    }

    @Test
    @DisplayName("매칭되지 않는 경우 null을 잘 리턴하는지 체크합니다.")
    void matchPathNotMatchTest() {
        String requestPath = "/users/123";
        String pathPrefix = "/user/{id}";
        Map<String,String> pathVariables = HttpStringUtil.matchPath(requestPath, pathPrefix);
        assertNull(pathVariables);
    }

    @Test
    @DisplayName("복잡한 경우에도 path variables를 잘 리턴하는지 체크합니다.")
    void matchPathComplex() {
        String requestPath = "/user/5/age/young/comments";
        String pathPrefix = "/user/{id}/age/{age}";
        Map<String,String> pathVariables = HttpStringUtil.matchPath(requestPath, pathPrefix);
        assertNotNull(pathVariables);
        assertEquals("5", pathVariables.get("id"));
        assertEquals("young", pathVariables.get("age"));
    }
}