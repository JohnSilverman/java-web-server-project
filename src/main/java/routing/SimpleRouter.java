package routing;

import http.MIME;
import http.request.HttpRequest;
import http.response.SimpleHttpResponse;
import http.response.HttpResponse;
import http.response.ResponseHeader;
import http.response.responsebody.PlainTextResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.URLMatcher;

import java.util.HashMap;
import java.util.Map;

public class SimpleRouter implements Router {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRouter.class);

    public Map<String, Controller> controllerMap = new HashMap<>();

    @Override
    public void addController(String pathPattern, Controller controller) {
        controllerMap.put(pathPattern, controller);
    }

    @Override
    public HttpResponse routeAndGetResponse(HttpRequest request) {
        logger.info("New Request {} {}", request.getMethod(), request.getPath());

        // 요청 path의 prefix에 매치되는 컨트롤러 찾기
        Controller controller = null;
        Map<String,String> pathVars = null;

        for(String pathPattern : this.controllerMap.keySet()){
            pathVars = URLMatcher.matchPath(request.getPath(), pathPattern);
            if(pathVars != null) {
                controller = this.controllerMap.get(pathPattern);
                request.getParamMap().putAll(pathVars);
                break;
            }
        }

        return controller == null ? response404() : controller.getResponse(request);
    }


    private HttpResponse response404(){
        HttpResponse response = new SimpleHttpResponse();
        response.status(404)
                .addHeader(new ResponseHeader("Content-Type",MIME.PLAIN_TEXT.toString()))
                .body(new PlainTextResponseBody("404 Not Found"));
        return response;
    }

}
