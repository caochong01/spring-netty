package com.autumn.httpMapping;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.Charset;

public class SimpleHttpResponse extends DefaultFullHttpResponse {

    private Object result;

    private Charset charset = Charset.defaultCharset();

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
        super.content().writeCharSequence(result.toString(), charset);
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status) {
        super(version, status);
    }

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content) {
        super(version, status, content);
    }

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders) {
        super(version, status, validateHeaders);
    }

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders, boolean singleFieldHeaders) {
        super(version, status, validateHeaders, singleFieldHeaders);
    }

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, boolean validateHeaders) {
        super(version, status, content, validateHeaders);
    }

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, boolean validateHeaders, boolean singleFieldHeaders) {
        super(version, status, content, validateHeaders, singleFieldHeaders);
    }

    public SimpleHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, HttpHeaders headers, HttpHeaders trailingHeaders) {
        super(version, status, content, headers, trailingHeaders);
    }
}
