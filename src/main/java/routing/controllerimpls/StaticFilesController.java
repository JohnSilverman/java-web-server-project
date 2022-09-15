package routing.controllerimpls;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import http.response.responsebody.FileResponseBody;
import http.response.responsebody.PlainTextResponseBody;
import routing.Controller;

public class StaticFilesController implements Controller {
    private static StaticFilesController instance = new StaticFilesController();
    private StaticFilesController(){}

    public static StaticFilesController getInstance(){
        return instance;
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) {
        String filepath = request.getPath().equals("") ? "index.html" : request.getPath();

        HttpResponse httpResponse = new SimpleHttpResponse();
        httpResponse.status(200)
                .body(new FileResponseBody("webapp/" + filepath));
        return httpResponse;
    }
}
