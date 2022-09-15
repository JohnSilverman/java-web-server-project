package webserver;

import java.io.*;
import java.net.Socket;

import http.MIME;
import http.request.HttpRequest;
import http.request.SimpleHttpRequest;
import http.response.HttpResponse;
import http.response.ResponseHeader;
import http.response.SimpleHttpResponse;
import http.response.responsebody.PlainTextResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.Router;
import routing.SimpleRouter;
import routing.controllerimpls.StaticFilesController;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 요청 패킷 파싱
            HttpRequest requestData = new SimpleHttpRequest(in);

            Router router = new SimpleRouter();

            // 컨트롤러 추가 (우선순위 순으로 추가하면 됨, 구체적인거 -> 일반적인거 순으로)
            router.addController("/", StaticFilesController.getInstance());

            HttpResponse response;
            try {
                response = router.routeAndGetResponse(requestData);
            } catch (Exception e){
                response = response500(e);
            }

            // 응답 전송
            response.send(out);

        } catch (IOException e) {
            logger.info("run() error");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        try {
            connection.close();
            logger.info("Connection closed");
        } catch(Exception ee){
            logger.info("connection.close() error in run()");
            logger.error(ee.getMessage());
        }
    }

    private HttpResponse response500(Exception e){
        HttpResponse response = new SimpleHttpResponse();
        response.status(500)
                .addHeader(new ResponseHeader("Content-Type", MIME.PLAIN_TEXT.toString()))
                .body(new PlainTextResponseBody("500 Interval Server Error\n\n" + e.getMessage()));
        return response;
    }

}
