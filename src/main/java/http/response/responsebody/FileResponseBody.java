package http.response.responsebody;

import http.MIME;
import http.response.ResponseBody;
import util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResponseBody implements ResponseBody {

    private File file;

    public FileResponseBody(String filePath){
        this.file = new File(filePath);
    }

    @Override
    public InputStream getInputStream(){
        try {
            return new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public long getContentLength() {
        return this.file.length();
    }

    @Override
    public MIME getContentType() {
        String ext = StringUtil.getExtension(this.file.getName());
        MIME mimeType = MIME.fromExtension(ext);
        return mimeType != null ? mimeType : MIME.HTML;
    }

}