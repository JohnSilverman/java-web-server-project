package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public final class HttpStringUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpStringUtil.class);

    // 인풋 스트림에서 문자열을 끝까지 읽어 하나의 string으로 반환합니다.
    public static List<String> inputStreamToLines(InputStream in){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<String> linesArray = new ArrayList<>();
        String line;

        try {
            line = reader.readLine();
            while(!"".equals(line) && line!=null){
                linesArray.add(line.trim());
                line = reader.readLine();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return linesArray;
    }

    // url param 파싱할 때 사용
    // a=1&b=2&c=3이라는 문자열을 넣고 outerDelimiter에 &, innerDelimiter에 "="을 넣으면 {a:1,b:2,c:3}의 map이 나온다.
    public static Map<String,String> parseQueryString(String string){
        Map<String,String> map = new HashMap<>();
        for(String param : string.split("&")){
            String[] keyval = param.split("=");
            map.put(keyval[0],keyval[1]);
        }
        return map;
    }

    public static String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }


    // /user/1 <-> /user/{id} 매치해서 {id:1} 해시맵 반환
    public static Map<String,String> matchPath(String requestPath, String pathPrefix){
        Map<String,String> pathVariables = new HashMap<>();
        List<String> requestPathList = new ArrayList<>(Arrays.asList(requestPath.split("/")));
        List<String> prefixList = new ArrayList<>(Arrays.asList(pathPrefix.split("/")));
        for(int i = 0; i < prefixList.size(); i++){
            String pathPrefixElement = prefixList.get(i);
            String requestPathElement = requestPathList.get(i);
            if(pathPrefixElement.equals(requestPathElement)) continue;
            else if( pathPrefixElement.charAt(0) == '{' && pathPrefixElement.charAt(pathPrefixElement.length()-1) == '}'){
                pathVariables.put(pathPrefixElement.substring(1,pathPrefixElement.length()-1), requestPathElement);
            } else return null;
        }
        return pathVariables;
    }
}
