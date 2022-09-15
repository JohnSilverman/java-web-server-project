package http.response;

import http.response.responsebody.PlainTextResponseBody;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

class SimpleHttpResponseTest {

    @Test
    void send() throws UnsupportedEncodingException {
        HttpResponse response = new SimpleHttpResponse();
        response.status(200)
                .addHeader(new ResponseHeader("Content-Type","text/html;charset=utf-8"))
                .body(new PlainTextResponseBody("HelloWorld"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        response.send(baos);
        assertEquals(
                "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html;charset=utf-8\r\n"
                + "Content-Length: 10\r\n\r\n"
                + "HelloWorld"
                , baos.toString()
        );
    }
}