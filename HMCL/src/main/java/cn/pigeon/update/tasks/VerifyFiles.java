package cn.pigeon.update.tasks;

import cn.pigeon.update.Static;
import cn.pigeon.update.data.SyncConfig;
import cn.pigeon.update.data.SyncFolderConfig;
import cn.pigeon.update.enums.SyncMode;
import cn.pigeon.update.utils.Utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class VerifyFiles {
    private final SyncConfig syncConfig;
    private final Path basePath;

    public VerifyFiles(File syncFile, Path basePath) {
        this.syncConfig = new SyncConfig(Utils.readJsonStringFromFile(syncFile));
        this.basePath = basePath;
    }

    public Map<File, String> verifyFile() {
        Map<File, String> requiredFile = new HashMap<>();
        List<String> folderName = syncConfig.getData();
        Map<String, SyncFolderConfig> syncFolderConfig = syncConfig.getFolderConfig();
        for (String folder : folderName) {
            Path folderPath = basePath.resolve(folder);
            SyncFolderConfig folderConfig = syncFolderConfig.get(folder);
            verifyFolder(folderPath, folderConfig, requiredFile);
        }
        verifyFile(requiredFile, syncConfig.getFiles(), Paths.get(""), basePath);
        return requiredFile;
    }

    private static void verifyFolder(Path folderPath, SyncFolderConfig folderConfig, Map<File, String> requiredFile) {
        Path serverPath = Paths.get(folderConfig.getServerPath());
        SyncMode syncMode = folderConfig.getMode();
        if (syncMode == SyncMode.MIRROR) {
            verifyExtraFile(folderPath, folderConfig);
        }
        verifyFile(requiredFile, folderConfig.getFiles(), serverPath, folderPath);
    }

    private static void verifyExtraFile(Path folderPath, SyncFolderConfig folderConfig) {
        ArrayList<String> fileList = new ArrayList<>();
        Utils.listFiles(folderPath, folderPath.toFile(), fileList);
        for (String filePath : fileList) {
            if (!folderConfig.getFiles().containsKey(filePath.replaceAll("\\\\", "/"))) {
                folderPath.resolve(filePath).toFile().delete();
            }
        }
    }

    private static void verifyFile(Map<File, String> requiredFile, Map<String, String> fileMap, Path serverPath, Path folderPath) {
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            File file = folderPath.resolve(entry.getKey()).toFile();
            if (file.exists()) {
                if (entry.getValue().equals("del")) {
                    file.delete();
                    continue;
                }
                if (Utils.calculateMD5(file).equals(entry.getValue())) {
                    continue;
                }
                System.out.println(file.getAbsolutePath());
                System.out.println(entry.getValue());
                System.out.println(Utils.calculateMD5(file));
                file.delete();
            }
            if (entry.getValue().equals("del")) {
                continue;
            }
            requiredFile.put(file, serverPath.resolve(entry.getKey()).toFile().toString());
        }
    }
}