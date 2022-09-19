package http.response;

import http.MIME;
import http.response.responsebody.PlainTextResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpResponse implements HttpResponse{
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private String statusLine = null;
    private List<ResponseHeader> headers;
    private ResponseBody body;
    private Map<String,Object> additionalData;

    public SimpleHttpResponse() {
        this.statusLine = "";
        this.headers = new ArrayList<>();
        this.additionalData = new HashMap<>();
        this.body = null;
    }

    public static HttpResponse simpleResponse(int statusCode){
        HttpResponse response = new SimpleHttpResponse();
        response.status(statusCode)
                .addHeader(new ResponseHeader("Content-Type", MIME.HTML.toString()))
                .body(new PlainTextResponseBody(statusCode + " " + Status.of(statusCode).getMessage()));
        return response;
    }

    public static HttpResponse redirect(String location){
        HttpResponse response = new SimpleHttpResponse();
        response.status(302)
                .addHeader(new ResponseHeader("Location", location));
        return response;
    }

    @Override
    public HttpResponse status(int statusCode) {
        this.statusLine = "HTTP/1.1 " + Status.of(statusCode).toString();
        return this;
    }

    @Override
    public HttpResponse addHeader(ResponseHeader header) {
        this.headers.add(header);
        return this;
    }

    @Override
    public HttpResponse body(ResponseBody body){
        this.body = body;
        this.addHeader( new ResponseHeader("Content-Length", String.valueOf(body.getContentLength())) );
        this.addHeader( new ResponseHeader("Content-Type", body.getContentType().toString()));
        return this;
    }

    @Override
    public void put(String key, Object data) {
        this.additionalData.put(key, data);
    }

    @Override
    public Object get(String key) {
        return this.additionalData.get(key);
    }

    private String headersToString(List<ResponseHeader> headersList){
        StringBuilder result = new StringBuilder();
        for(var h : headersList){
            result.append(h.toString()).append("\r\n");
        }
        return result.toString();
    }

    @Override
    public void send(OutputStream os) {
        try {
            if( !this.statusLine.equals("") ){ os.write((this.statusLine + "\r\n").getBytes()); }
            if( headers.size() != 0) { os.write( (headersToString(this.headers) + "\r\n").getBytes() ); }
            if( this.body != null && this.body.getInputStream() != null ) {this.body.getInputStream().transferTo(os); }
        } catch(IOException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
