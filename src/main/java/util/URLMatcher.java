package util;

import java.util.*;

public class URLMatcher {

    // /user/1 <-> /user/{id} 매치해서 {id:1} 해시맵 반환
    public static Map<String,String> matchPath(String requestPath, String pattern){
        List<String> requestPathList = emptyOrDefault(Arrays.asList(regularize(requestPath).split("/")));
        List<String> patternList = emptyOrDefault(Arrays.asList(regularize(pattern).split("/")));

        return matchPathAlgorithmPart(requestPathList, patternList);
    }

    private static List<String> emptyOrDefault(List<String> pathList){
        return pathList.size() == 0 ? new ArrayList<>(Arrays.asList("","")) : pathList;
    }

    private static String regularize(String str){
        int lp = 0, rp = str.length() - 1;
        while(rp >= 0 && str.charAt(rp) == '/') rp--;
        while(lp < str.length() && str.charAt(lp) == '/') lp++;
        if(lp > rp) return "/";
        if(lp == 0) {str = "/" + str; rp++;}
        return str.substring(Math.max(0, lp-1), rp + 1);
    }

    private static boolean isVariable(String str) { return str.charAt(0) == '{' && str.charAt(str.length()-1) == '}';}

    private static String extractName(String str) {return str.substring(1,str.length()-1); }

    // 알고리드믹한 파트라서 클린코드 신경쓰지 않고 작성했습니다.
    // 대신 이 URLMatcher 클래스의 경우는 다른 클래스보다 테스트코드를 좀더 꼼꼼히 만들었습니다.
    private static Map<String,String> matchPathAlgorithmPart(List<String> requestList, List<String> patternList){
        Map<String,String> result = new HashMap<>();
        int reqLen = requestList.size(), patLen = patternList.size(), p1 = 0, p2 = 0;

        while( p1 < reqLen && p2 < patLen){
            if ( !requestList.get(p1).equals(patternList.get(p2)) ) {
                if( isVariable(patternList.get(p2)) ) {
                    if (p2 == patLen - 1) {
                        result.put(extractName(patternList.get(p2)), String.join("/", requestList.subList(p1, reqLen)));
                        return result;
                    }
                    result.put(extractName(patternList.get(p2)), requestList.get(p1));
                    p1++;
                    p2++;
                    continue;
                }
                return null;
            }
            p1++;
            p2++;
        }

        return result;
    }
}
