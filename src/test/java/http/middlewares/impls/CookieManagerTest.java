package http.middlewares.impls;

import http.request.HttpRequest;
import http.request.SimpleHttpRequest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CookieManagerTest {

    @Test
    void processRequest() {
        String requestString = "GET /test HTTP/1.1\r\n"
                + "Connection: Keep-Alive\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Cookie: a=b;c=d;\r\n";
        InputStream in = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = new SimpleHttpRequest(in);
        HttpRequest processed = CookieManager.getInstance().processRequest(request);
        assertEquals("b", ((Map<String,String>)processed.getAdditionalData(CookieManager.KEY_COOKIE_MAP)).get("a"));
        assertEquals("d", ((Map<String,String>)processed.getAdditionalData(CookieManager.KEY_COOKIE_MAP)).get("c"));
    }
}