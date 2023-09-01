package cn.pigeon.update.tasks;

import cn.pigeon.update.enums.SyncMode;
import cn.pigeon.update.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class VerifyFiles {
    private final Path targetPath;
    private final File targetFile;
    private final SyncMode syncMode;

    public VerifyFiles(Path targetPath, File targetFile, SyncMode syncMode) {
        this.targetPath = targetPath;
        this.targetFile = targetFile;
        this.syncMode = syncMode;
    }

    public ArrayList<String> verifyFile() throws IOException, NoSuchAlgorithmException {
        ArrayList<String> fileArrayList = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(targetFile));
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (syncMode == SyncMode.FORCE) {
            ArrayList<String> filePathList = new ArrayList<>();
            Utils.listFiles(targetPath.getParent(), targetPath.toFile(), filePathList);
            for (String key : filePathList) {
                if (jsonObject.has(key)) {
                    continue;
                }
                targetPath.getParent().resolve(key).toFile().delete();
            }
        }
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            File file = targetPath.getParent().resolve(key).toFile();
            if (!file.exists()) {
                fileArrayList.add(entry.getKey());
                continue;
            }
            String md5 = Utils.calculateMD5(file);
            if (!md5.equals(value.toString().replace("\"", ""))) {
                fileArrayList.add(entry.getKey());
            }
        }
        return fileArrayList;
    }
}
