package org.jackhuang.hmcl.launch;

import java.io.IOException;

public class CheckConfigUpdateException extends IOException {
    public CheckConfigUpdateException() {
    }

    public CheckConfigUpdateException(String message) {
        super(message);
    }

    public CheckConfigUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckConfigUpdateException(Throwable cause) {
        super(cause);
    }
}
