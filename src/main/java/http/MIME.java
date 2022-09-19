package http;

public enum MIME {
    PLAIN_TEXT(".txt","text/plain"),
    HTML(".html","text/html"),
    CSS(".css","text/css"),
    ICON(".ico", "image/x-icon"),
    JSON(".json","application/json"),
    GIF(".gif", "image/gif"),
    JPEG(".jpeg","image/jpeg"),
    JPG(".jpg","image/jpg"),
    PNG(".png", "image/png"),
    OCTET_STREAM("*","application/octet-stream"),
    FORM("*", "application/x-www-form-urlencoded"),
    XML(".xml","application/xml");

    private String extension;
    private String mimeType;

    MIME(String extension, String mimeType){
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getMime() { return mimeType; }
    public String getExtension() { return extension; }

    public static MIME fromExtension(String extension){
        for(var mime: MIME.values()){
            if(mime.extension.equals(extension)) return mime;
        }
        return OCTET_STREAM;
    }

    public static MIME fromMime(String mimeType){
        for(var mime: MIME.values()){
            if(mime.mimeType.equals(mimeType)) return mime;
        }
        return OCTET_STREAM;
    }

    @Override
    public String toString(){
        return this.mimeType;
    }
}
