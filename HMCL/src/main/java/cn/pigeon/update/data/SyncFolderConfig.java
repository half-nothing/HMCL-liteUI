package cn.pigeon.update.data;

import cn.pigeon.update.enums.SyncMode;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SyncFolderConfig {
    private final SyncMode mode;
    private final String serverPath;
    private final String clientPath;
    private final Map<String, String> files;

    @SuppressWarnings("unchecked")
    public SyncFolderConfig(String clientPath, Map<String, Object> stringObjectMap) {
        this(clientPath,
                ((String) stringObjectMap.get("mode")).toUpperCase(),
                (String) stringObjectMap.get("serverPath"),
                (Map<String, String>) stringObjectMap.get("files"));
    }

    public SyncFolderConfig(String clientPath, String mode, String serverPath, Map<String, String> files) {
        this(clientPath, SyncMode.valueOf(mode.toUpperCase()), serverPath, files);
    }

    public SyncFolderConfig(String clientPath, SyncMode mode, String serverPath, Map<String, String> files) {
        this.clientPath = Objects.requireNonNull(clientPath);
        this.mode = Objects.requireNonNull(mode);
        this.serverPath = Objects.requireNonNull(serverPath);
        this.files = Objects.requireNonNull(files);
    }


    public SyncMode getMode() {
        return mode;
    }

    public String getServerPath() {
        return serverPath;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return String.format("SyncMode: %s\nServerPath: %s\nClientPath: %s\nFiles: %s", mode, serverPath, clientPath, files);
    }
}
