package routing.controllerimpls;

import db.Database;
import http.middlewares.impls.BodyParser;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import http.response.responsebody.JsonResponseBody;
import model.Memo;
import routing.Controller;
import util.JsonConverter;

import java.util.List;
import java.util.Map;

public class MemoController implements Controller {
    @Override
    public HttpResponse getResponse(HttpRequest request) {
        if(request.getMethod() == HttpRequest.METHOD.GET){
            return get(request);
        }
        else if(request.getMethod() == HttpRequest.METHOD.POST){
            return post(request);
        } else {
            return SimpleHttpResponse.simpleResponse(401);
        }
    }

    @Override
    public String getPattern() {
        return "/api/memo";
    }

    private HttpResponse get(HttpRequest request){
        List<Memo> list = Database.findAllMemos();
        return new SimpleHttpResponse()
                .status(200)
                .body(new JsonResponseBody(JsonConverter.stringify(list)));
    }

    private HttpResponse post(HttpRequest request){
        Map<String,String> requestBody = (Map<String, String>) request.getAdditionalData(BodyParser.KEY_BODY);

        String title = requestBody.get("title");
        String body = requestBody.get("body");

        Memo newMemo = new Memo();
        newMemo.setBody(body);
        newMemo.setTitle(title);

        Database.persist(newMemo);
        return SimpleHttpResponse.simpleResponse(200);
    }
}
