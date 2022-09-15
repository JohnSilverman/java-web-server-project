package http.middlewares.impls;

import http.middlewares.MiddleWare;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLogger implements MiddleWare {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);
    private static RequestLogger instance = new RequestLogger();

    private RequestLogger(){}

    public static RequestLogger getInstance(){ return instance;}

    @Override
    public HttpRequest processRequest(HttpRequest request) {
        logger.info("{} {} {} {}", request.getMethod(), request.getPath(), request.get(HttpRequest.KEY_IP), request.get(HttpRequest.KEY_PORT));
        return request;
    }

    @Override
    public HttpResponse processResponse(HttpResponse response) {
        return response;
    }
}
