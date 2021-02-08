package com.autumn.httpMapping;

import com.autumn.router.Routing;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;

/**
 * 存储http请求的上下文环境
 */
public class HttpContext {

    private HttpRequest httpRequest;

    private SimpleHttpResponse httpResponse;

    private boolean notFound;

    private Map<String, Object> params;

    private Routing routing;

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public SimpleHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(SimpleHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public boolean isNotFound() {
        return notFound;
    }

    public void setNotFound(boolean notFound) {
        this.notFound = notFound;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }
}
