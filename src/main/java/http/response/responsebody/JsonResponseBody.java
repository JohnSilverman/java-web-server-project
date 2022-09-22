package http.response.responsebody;

import http.MIME;
import http.response.ResponseBody;
import util.JsonConverter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class JsonResponseBody implements ResponseBody {

    private final String jsonString;

    public JsonResponseBody(Map<String,String> map){
        jsonString = JsonConverter.stringify(map);
    }

    public JsonResponseBody(String jsonString) { this.jsonString = jsonString; }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.jsonString.getBytes());
    }

    @Override
    public long getContentLength() {
        return this.jsonString.length();
    }

    @Override
    public MIME getContentType() {
        return MIME.JSON;
    }
}
