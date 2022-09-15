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
    OCTET_STREAM("","application/octet-stream");

    private String extension;
    private String mimeType;

    MIME(String extension, String mimeType){
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public static MIME fromExtension(String extension){
        for(var mime: MIME.values()){
            if(mime.extension.equals(extension)) return mime;
        }
        return null;
    }

    public static MIME fromMime(String mimeType){
        for(var mime: MIME.values()){
            if(mime.mimeType.equals(mimeType)) return mime;
        }
        return null;
    }

    @Override
    public String toString(){
        return this.mimeType;
    }
}
