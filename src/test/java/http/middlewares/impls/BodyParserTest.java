package http.middlewares.impls;

import http.request.HttpRequest;
import http.request.SimpleHttpRequest;
import http.response.HttpResponse;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BodyParserTest {

    @Test
    void fromBodyParsing() {
        String requestString = "POST /test HTTP/1.1\r\n"
                + "Connection: Keep-Alive\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Content-Length: 7\r\n\r\n"
                + "a=b&c=d";
        InputStream in = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = new SimpleHttpRequest(in);
        HttpRequest processed = BodyParser.getInstance().processRequest(request);
        assertEquals("b", ((Map<String,String>)processed.getAdditionalData(BodyParser.KEY_BODY)).get("a"));
        assertEquals("d", ((Map<String,String>)processed.getAdditionalData(BodyParser.KEY_BODY)).get("c"));
    }

    @Test
    void jsonBodyParsing(){
        String requestString = "POST /test HTTP/1.1\r\n"
                + "Connection: Keep-Alive\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: 17\r\n\r\n"
                + "{\"a\":\"b\",\"c\":\"d\"}";
        InputStream in = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = new SimpleHttpRequest(in);
        HttpRequest processed = BodyParser.getInstance().processRequest(request);
        assertEquals("b", ((Map<String,String>)processed.getAdditionalData(BodyParser.KEY_BODY)).get("a"));
        assertEquals("d", ((Map<String,String>)processed.getAdditionalData(BodyParser.KEY_BODY)).get("c"));
    }

    @Test
    void unknownContentType(){
        String requestString = "POST /test HTTP/1.1\r\n"
                + "Connection: Keep-Alive\r\n"
                + "Content-Type: application/whatisthis\r\n"
                + "Content-Length: 17\r\n\r\n"
                + "{\"a\":\"b\",\"c\":\"d\"}";
        InputStream in = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = new SimpleHttpRequest(in);
        HttpRequest processed = BodyParser.getInstance().processRequest(request);
        assertNull(((Map<String,String>)processed.getAdditionalData(BodyParser.KEY_BODY)).get("a"));
        assertNull(((Map<String,String>)processed.getAdditionalData(BodyParser.KEY_BODY)).get("c"));
    }
}