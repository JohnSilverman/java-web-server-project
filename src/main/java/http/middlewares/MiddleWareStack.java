package http.middlewares;

import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public final class MiddleWareStack {

    private List<MiddleWare> middleWareList = new ArrayList<>();

    public void add(MiddleWare middleWare) {
        middleWareList.add(middleWare);
    }

    public HttpRequest processRequest(HttpRequest request){
        for(MiddleWare mw : this.middleWareList){
            request = mw.processRequest(request);
        }
        return request;
    }

    public HttpResponse processResponse(HttpResponse response){
        for(int i = this.middleWareList.size() - 1; i >= 0; i--){
            response = this.middleWareList.get(i).processResponse(response);
        }
        return response;
    }
}
