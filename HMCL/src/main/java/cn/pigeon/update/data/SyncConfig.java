package cn.pigeon.update.data;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.pigeon.update.Static.gson;
import static cn.pigeon.update.Static.separator;

public class SyncConfig {
    private final List<String> data;
    private final Map<String, String> files;
    private final Map<String, SyncFolderConfig> folderConfig;

    @SuppressWarnings("unchecked")
    public SyncConfig(String jsonString) {
        this((Map<String, Object>) gson.fromJson(jsonString, new TypeToken<Map<String, Object>>() {
        }.getType()));
    }

    @SuppressWarnings("unchecked")
    public SyncConfig(Map<String, Object> jsonObject) {
        this.data = (List<String>) Objects.requireNonNull(jsonObject.get("data"));
        this.files = (Map<String, String>) Objects.requireNonNull(jsonObject.get("files"));
        this.folderConfig = new HashMap<>();
        for (String name : this.getData()) {
            Map<String, Object> object = (Map<String, Object>) Objects.requireNonNull(jsonObject.get(name));
            SyncFolderConfig syncFolderConfig = new SyncFolderConfig(name, object);
            folderConfig.put(name, syncFolderConfig);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(String.format("SyncFolder: %s\nAdditionalFiles: %s\nFolderConfig:%s", data, files, separator));
        for (SyncFolderConfig syncFolderConfig : folderConfig.values()) {
            stringBuilder.append(syncFolderConfig).append(separator);
        }
        return stringBuilder.toString();
    }

    public List<String> getData() {
        return data;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public Map<String, SyncFolderConfig> getFolderConfig() {
        return folderConfig;
    }
}
