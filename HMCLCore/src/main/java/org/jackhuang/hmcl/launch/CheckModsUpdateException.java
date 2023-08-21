package org.jackhuang.hmcl.launch;

import java.io.IOException;

public class CheckModsUpdateException extends IOException {
    public CheckModsUpdateException() {
    }

    public CheckModsUpdateException(String message) {
        super(message);
    }

    public CheckModsUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckModsUpdateException(Throwable cause) {
        super(cause);
    }
}
