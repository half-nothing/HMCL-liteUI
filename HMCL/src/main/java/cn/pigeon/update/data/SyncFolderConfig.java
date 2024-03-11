package cn.pigeon.update.data;

import cn.pigeon.update.enums.SyncMode;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class
SyncFolderConfig {
    private final SyncMode mode;
    private final String serverPath;
    private final Map<String, String> files;

    @SuppressWarnings("unchecked")
    public SyncFolderConfig(Map<String, Object> stringObjectMap) {
        this(((String) stringObjectMap.get("mode")).toUpperCase(),
                (String) stringObjectMap.get("serverPath"),
                (Map<String, String>) stringObjectMap.get("files"));
    }

    public SyncFolderConfig(String mode, String serverPath, Map<String, String> files) {
        this(SyncMode.valueOf(mode.toUpperCase()), serverPath, files);
    }

    public SyncFolderConfig(SyncMode mode, String serverPath, Map<String, String> files) {
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
        return String.format("SyncMode: %s\nServerPath: %s\nFiles: %s", mode, serverPath, files);
    }
}
