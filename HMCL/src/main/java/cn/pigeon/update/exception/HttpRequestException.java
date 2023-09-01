package cn.pigeon.update.exception;

import java.io.IOException;

public class HttpRequestException extends IOException {
    public final int httpCode;

    public HttpRequestException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }
}
