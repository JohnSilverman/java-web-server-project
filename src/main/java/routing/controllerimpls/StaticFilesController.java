package routing.controllerimpls;

import http.middlewares.impls.BodyParser;
import http.middlewares.impls.CookieParser;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import http.response.responsebody.FileResponseBody;
import http.response.responsebody.PlainTextResponseBody;
import routing.Controller;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import static http.request.HttpRequest.METHOD.GET;

public class StaticFilesController implements Controller {

    private final String pathPattern;

    public StaticFilesController(String pathPattern){
        this.pathPattern = pathPattern;
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) {
        if (request.getMethod() != GET){
            return SimpleHttpResponse.simpleResponse(404);
        }

        String filepath = "webapp/" + (request.getParam("filePath").equals("") ? "index.html" : request.getParam("filePath"));

        if(new File(filepath).exists() == false){
            return SimpleHttpResponse.simpleResponse(404);
        }

        HttpResponse httpResponse = new SimpleHttpResponse();
        httpResponse.status(200)
                .body(new FileResponseBody(filepath));
        return httpResponse;
    }

    @Override
    public String getPattern() {
        return pathPattern;
    }
}
