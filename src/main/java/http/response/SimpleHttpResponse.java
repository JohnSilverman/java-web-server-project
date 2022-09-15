package http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleHttpResponse implements HttpResponse{
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private String statusLine = null;
    private List<ResponseHeader> headers;
    private ResponseBody body;

    public SimpleHttpResponse() {
        this.statusLine = "";
        this.headers = new ArrayList<>();
    }

    private enum Status {
        OK(200,"OK"),
        BAD_REQUEST(400,"Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        FORBIDDEN(403,"Forbidden"),
        NOT_FOUND(404,"Not Found"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        INTERNAL_SERVER_ERROR(500,"Internal Server Error");

        private final int statusCode;
        private final String message;

        Status(int statusCode, String message){
            this.statusCode = statusCode;
            this.message = message;
        }

        public static Status of(int statusCode){
            for(var status : Status.values()){
                if(status.statusCode == statusCode) return status;
            }
            return null;
        }

        public String getMessage() { return this.message;}
        public int getStatusCode() {return this.statusCode;}
        public String toString(){ return String.valueOf(this.statusCode) + " " + this.message;}
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
            if( this.body.getInputStream() != null ) {this.body.getInputStream().transferTo(os); }
        } catch(IOException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}