package webserver;

import java.io.*;
import java.net.Socket;

import http.request.HttpRequest;
import http.request.SimpleHttpRequest;
import http.response.SimpleHttpResponse;
import http.response.HttpResponse;
import http.response.ResponseHeader;
import http.response.responsebody.FileResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            // 파일 준비
            String path = requestData.getPath();
            FileResponseBody fileResponseBody = new FileResponseBody("webapp/" + path);

            // 응답 객체 만들기 (빌더 패턴 썼으면 더 좋았을까 하는 고민 살짝,,)
            HttpResponse response = new SimpleHttpResponse()
                    .status(200)
                    .addHeader(new ResponseHeader("Content-Type", "text/html;charset=utf-8"))
                    .body(fileResponseBody);

            // 응답 전송
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos);

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

}
