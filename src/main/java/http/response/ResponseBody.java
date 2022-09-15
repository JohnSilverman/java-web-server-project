package http.response;

import http.MIME;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface ResponseBody {
    public InputStream getInputStream();
    public long getContentLength();
    public MIME getContentType();
}
