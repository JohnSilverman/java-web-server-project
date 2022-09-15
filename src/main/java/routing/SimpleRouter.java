package routing;

import http.MIME;
import http.middlewares.MiddleWare;
import http.middlewares.MiddleWareStack;
import http.request.HttpRequest;
import http.response.SimpleHttpResponse;
import http.response.HttpResponse;
import http.response.ResponseHeader;
import http.response.responsebody.PlainTextResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.URLMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleRouter implements Router {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRouter.class);

    private Map<String, Controller> controllerMap = new HashMap<>();
    private MiddleWareStack middleWareStack = new MiddleWareStack();

    @Override
    public void addMiddleware(MiddleWare middleWare) {
        middleWareStack.add(middleWare);
    }

    @Override
    public void addController(String pathPattern, Controller controller) {
        controllerMap.put(pathPattern, controller);
    }

    @Override
    public HttpResponse routeAndGetResponse(HttpRequest request) {
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

        request = this.middleWareStack.processRequest(request);
        return this.middleWareStack.processResponse(controller == null ? response404(request) : controller.getResponse(request));
    }


    // 실행될일 거의 없음. request handler에서 등록된 마지막 라우터가 \/.*를 매칭하는거라서
    private HttpResponse response404(HttpRequest request){
        HttpResponse response = new SimpleHttpResponse();
        response.status(404)
                .addHeader(new ResponseHeader("Content-Type",MIME.PLAIN_TEXT.toString()))
                .body(new PlainTextResponseBody("404 {} Not Found".replace("{}",request.getPath())));
        return response;
    }

}
