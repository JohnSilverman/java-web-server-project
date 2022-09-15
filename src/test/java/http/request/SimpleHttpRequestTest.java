package http.request;

import http.request.HttpRequest;
import http.request.SimpleHttpRequest;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimpleHttpRequestTest {

    private final SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest(true);


//    @Test
//    void removePathPrefixTest(){
//        simpleHttpRequest.getHeaderMap().put(SimpleHttpRequest.KEY_PATH, "/abc/def");
//        simpleHttpRequest.removePathPrefix("/abc");
//        assertEquals("/def", simpleHttpRequest.getPath());
//    }

    @Test
    void parseFirstLineOfHTTPRequest() {
        String firstLine = "GET /user?q=john&religion=atheism HTTP/1.1";
        Map<String,String> testResult = simpleHttpRequest.parseFirstLineOfHTTPRequest(firstLine);
        assertEquals(HttpRequest.METHOD.GET.getValue(), testResult.get(SimpleHttpRequest.KEY_METHOD));
        assertEquals("HTTP/1.1", testResult.get(SimpleHttpRequest.KEY_PROTOCOL));
        assertEquals("/user", testResult.get(SimpleHttpRequest.KEY_PATH));
        assertEquals("john", testResult.get("q"));
        assertEquals("atheism", testResult.get("religion"));
    }

    @Test
    void parseHeaderExceptFirstLine() {
        List<String> headerExceptFirstLine = List.of("Host: foo.com",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 13");
        Map<String,String> testResult = simpleHttpRequest.parseHeaderExceptFirstLine(headerExceptFirstLine);

        assertEquals("foo.com", testResult.get("Host"));
        assertEquals("application/x-www-form-urlencoded", testResult.get("Content-Type"));
        assertEquals("13", testResult.get("Content-Length"));
    }

    @Test
    void splitHeaderAndBody() {
        List<String> requestString = List.of(
                "GET /user?q=john&religion=atheism HTTP/1.1",
                "Host: foo.com",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 13",
                "",
                "a=1&b=2"
                );
        List<List<String>> testResult = simpleHttpRequest.splitHeaderAndBody(requestString);

        assertTrue(testResult.size() <= 2);
        assertEquals("GET /user?q=john&religion=atheism HTTP/1.1", testResult.get(0).get(0));
        assertEquals("a=1&b=2", testResult.get(1).get(0));
    }

    @Test
    void splitHeaderAndBodyWhenBodyDoesNotExist() {
        List<String> requestString = List.of(
                "GET /user?q=john&religion=atheism HTTP/1.1",
                "Host: foo.com",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 13"
        );
        List<List<String>> testResult = simpleHttpRequest.splitHeaderAndBody(requestString);

        assertEquals(1, testResult.size() );
        assertEquals("GET /user?q=john&religion=atheism HTTP/1.1", testResult.get(0).get(0));
        assertEquals("Content-Length: 13", testResult.get(0).get(3));
    }

    @Test
    void parseHTTPRequestCheckBodyParsedWell(){
        List<String> request= List.of(
                "GET /user?q=john&religion=atheism HTTP/1.1",
                "Host: foo.com",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 13",
                "",
                "a=1&b=2",
                "c=3&d=4"
        );
        Map<String,String> testResult = simpleHttpRequest.parseHTTPRequest(request);
        assertEquals("a=1&b=2\r\nc=3&d=4", testResult.get(SimpleHttpRequest.KEY_BODY));
    }

}