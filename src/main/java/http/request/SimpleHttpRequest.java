package http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStringUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpRequest implements HttpRequest {
    public static final String KEY_METHOD = "_METHOD";
    public static final String KEY_PROTOCOL = "_PROTOCOL";
    public static final String KEY_PATH = "_PATH";
    public static final String KEY_BODY = "_BODY";

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpRequest.class);
    private Map<String,String> headerMap;
    private Map<String,String> paramsMap;
    private Map<String,Object> additionalData; // mostly used by middlewares
    private String body;

    public SimpleHttpRequest(boolean testMode){
        headerMap = new HashMap<>();
        paramsMap = new HashMap<>();
        body = new String();
    } //테스트 전용

    public SimpleHttpRequest(InputStream inputStream){
        List<String> requestHeaderList = HttpStringUtil.inputStreamToLines(inputStream);
        this.headerMap = parseHTTPRequest(requestHeaderList);
        this.paramsMap = new HashMap<>();
        this.additionalData = new HashMap<>();
    }

    @Override
    public String getHeader(String key) {return this.headerMap.getOrDefault(key,""); }
    @Override
    public METHOD getMethod() {
        return METHOD.findMethodByValue(headerMap.get(KEY_METHOD));
    }

    @Override
    public String getPath() {
        return headerMap.get(KEY_PATH);
    }

    @Override
    public String getParam(String key) {
        return paramsMap.getOrDefault(key, "");
    }

    @Override
    public Map<String,String> getParamMap() { return paramsMap; }

    @Override
    public String getBody() {
        return body;
    }
    @Override
    public void put(String key, Object data) {
        this.additionalData.put(key, data);
    }

    @Override
    public Object get(String key) {
        return this.additionalData.get(key);
    }

//    @Override
//    public void removePathPrefix(String pathPrefix){
//        if(headerMap.get(KEY_PATH).startsWith(pathPrefix)){
//            headerMap.put(KEY_PATH, headerMap.get(KEY_PATH).substring(pathPrefix.length()));
//        }
//    }

    //첫 줄 파싱. 예시 : "GET /user?q=john&religion=atheism HTTP/1.1"
    public Map<String,String> parseFirstLineOfHTTPRequest(String firstLine){
        Map<String,String> result = new HashMap<>();
        String[] methodParamsProtocol = firstLine.split(" "); // split to method, path+params, protocol
        String[] pathAndParams = methodParamsProtocol[1].split("\\?"); // split to path, params

        if(pathAndParams.length == 2){
            paramsMap = HttpStringUtil.parseQueryString(pathAndParams[1]);
            for(String key : paramsMap.keySet()) result.put(key, paramsMap.get(key));
        }

        result.put(KEY_METHOD, methodParamsProtocol[0]);
        result.put(KEY_PROTOCOL, methodParamsProtocol[2]);
        result.put(KEY_PATH, pathAndParams[0]);
        return result;
    }

    // 첫 줄을 제외한 헤더 나머지부분 파싱 (Accept, Content-Type 이런것들)
    public Map<String, String> parseHeaderExceptFirstLine(List<String> headerPartList){
        Map<String,String> result = new HashMap<>();
        for(String line : headerPartList){
            String[] keyval = line.split(": ");
            result.put(keyval[0], keyval[1]);
        }
        return result;
    }

    // 헤더 파싱
    public Map<String, String> parseHTTPHeader(List<String> headerLines){
        Map<String,String> result = parseFirstLineOfHTTPRequest(headerLines.get(0));
        Map<String,String> parsedExceptFirstLine = parseHeaderExceptFirstLine(headerLines.subList(1, headerLines.size()));
        result.putAll(parsedExceptFirstLine);
        headerMap = result;
        return result;
    }

    //문자열 리스트를 받아서 (한줄이 한 문자열) 빈 줄을 기준으로 헤더 부분이랑 바디 부분을 나눔
    public List<List<String>> splitHeaderAndBody(List<String> linesOfHTTPRequest){
        List<List<String>> result = new ArrayList<>();
        for(int i = 0; i < linesOfHTTPRequest.size(); i++){
            if (linesOfHTTPRequest.get(i).equals("")){
                result.add(linesOfHTTPRequest.subList(0,i));
                result.add(linesOfHTTPRequest.subList(i+1, linesOfHTTPRequest.size()));
                return result;
            }
        }
        result.add(linesOfHTTPRequest.subList(0, linesOfHTTPRequest.size()));
        return result;
    }

    // http 요청 문자열리스트 (한줄이 한 문자열) 을 넣어주면 파싱해서 map형태로 만들어줌
    public Map<String,String> parseHTTPRequest(List<String> linesOfHTTPReqeust) {
        List<List<String>> headerAndBody = splitHeaderAndBody(linesOfHTTPReqeust);
        Map<String,String> result = parseHTTPHeader(headerAndBody.get(0));
        if(headerAndBody.size() == 2) {
            String body = String.join("\r\n", headerAndBody.get(1));
            result.put(KEY_BODY, body);
            this.body = body;
        }
        return result;
    }
}
