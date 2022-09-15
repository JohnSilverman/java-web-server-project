package http.response.responsebody;

import http.MIME;
import http.response.ResponseBody;

import java.io.InputStream;

public class JsonResponseBody implements ResponseBody {
    @Override
    public InputStream getInputStream() {
        return null;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public MIME getContentType() {
        return null;
    }
}
