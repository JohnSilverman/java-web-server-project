package routing;

import http.middlewares.MiddleWare;
import http.middlewares.MiddleWareStack;
import http.request.HttpRequest;
import http.response.SimpleHttpResponse;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.URLMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleRouter implements Router {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRouter.class);

    private List<Controller> controllerList = new ArrayList<>();
    private MiddleWareStack middleWareStack = new MiddleWareStack();

    @Override
    public void addMiddleware(MiddleWare middleWare) {
        middleWareStack.add(middleWare);
    }

    @Override
    public void addController(Controller controller) {
        controllerList.add(controller);
    }

    @Override
    public HttpResponse routeAndGetResponse(HttpRequest request) {
        // 요청 path의 prefix에 매치되는 컨트롤러 찾기
        Controller controller = null;
        Map<String,String> pathVars = null;

        // TODO : Stream API
        for(Controller con : this.controllerList){
            String pathPattern = con.getPattern();
            pathVars = URLMatcher.matchPath(request.getPath(), pathPattern);
            if(pathVars != null) {
                controller = con;
                request.getParamMap().putAll(pathVars);
                break;
            }
        }

        request = this.middleWareStack.processRequest(request);
        return this.middleWareStack.processResponse(
                controller == null ?
                        SimpleHttpResponse.simpleResponse(404)
                        : controller.getResponse(request));
    }


}
