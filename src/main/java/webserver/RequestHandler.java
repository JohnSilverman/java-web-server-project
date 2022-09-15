package webserver;

import java.io.*;
import java.net.Socket;

import http.MIME;
import http.middlewares.RequestLogger;
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
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 요청 패킷 파싱
            HttpRequest requestData = new SimpleHttpRequest(in);

            // IP, PORT 정보 넘겨주기
            requestData.put(HttpRequest.KEY_IP, connection.getInetAddress());
            requestData.put(HttpRequest.KEY_PORT, connection.getPort());

            Router router = new SimpleRouter();

            // 미들웨어 추가
            router.addMiddleware(RequestLogger.getInstance());

            // 컨트롤러 추가 (우선순위 순으로 추가하면 됨, 구체적인거 -> 일반적인거 순으로)
            router.addController("/{filePath}", StaticFilesController.getInstance());

            // 응답 생성
            HttpResponse response;
            try {
                response = router.routeAndGetResponse(requestData);
            } catch (Exception e){
                response = response500(e);
                logger.error(e.getMessage());
                e.printStackTrace();
            }

            // 응답 전송
            response.send(out);

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch(Exception ee){
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
