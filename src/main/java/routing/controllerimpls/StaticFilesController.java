package routing.controllerimpls;

import http.request.HttpRequest;
import http.response.HttpResponse;
import routing.Controller;

public class StaticFilesController implements Controller {
    private static StaticFilesController instance = new StaticFilesController();
    private StaticFilesController(){}

    public static StaticFilesController getInstance(){
        return instance;
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) {
        return null;
    }
}
