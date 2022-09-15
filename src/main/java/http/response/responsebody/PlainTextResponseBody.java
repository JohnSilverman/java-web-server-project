package http.response.responsebody;

import http.MIME;
import http.response.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PlainTextResponseBody implements ResponseBody {

    private String text;

    public PlainTextResponseBody(String inputText){
        this.text = inputText;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.text.getBytes());
    }

    @Override
    public long getContentLength() {
        return this.text.length();
    }

    @Override
    public MIME getContentType() {
        return MIME.PLAIN_TEXT;
    }
}
