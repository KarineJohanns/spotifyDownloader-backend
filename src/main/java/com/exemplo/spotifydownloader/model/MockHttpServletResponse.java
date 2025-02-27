package com.exemplo.spotifydownloader.model;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

public class MockHttpServletResponse implements HttpServletResponse {
    private final ByteArrayOutputStream outputStream;
    private String contentType;
    private String characterEncoding = "UTF-8";

    public MockHttpServletResponse(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outputStream.close();
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.characterEncoding = charset;
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    // Métodos não utilizados (podem ser deixados vazios ou lançar UnsupportedOperationException)
    @Override
    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean containsHeader(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String encodeURL(String url) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String encodeRedirectURL(String url) {
        throw new UnsupportedOperationException("Not implemented");
    }


    @Override
    public void sendError(int sc, String msg) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void sendError(int sc) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setDateHeader(String name, long date) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addDateHeader(String name, long date) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setHeader(String name, String value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setStatus(int sc) {
        throw new UnsupportedOperationException("Not implemented");
    }


    @Override
    public int getStatus() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String getHeader(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Collection<String> getHeaders(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Collection<String> getHeaderNames() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setContentLength(int len) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setContentLengthLong(long len) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setBufferSize(int size) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int getBufferSize() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void flushBuffer() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void resetBuffer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setLocale(Locale loc) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not implemented");
    }
}