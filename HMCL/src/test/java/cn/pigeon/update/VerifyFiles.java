package cn.pigeon.update;
import cn.pigeon.update.utils.Utils;
import okhttp3.HttpUrl;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;

public class VerifyFiles {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // JSON数据
//        String json = "{\"data\":[\"config\",\"mods\"],\"config\":{\"mode\":\"push\",\"serverPath\":\"config\",\"files\":{\"package.json\":\"c50e8e55f609ef2f6af41b26e6fe4a32\"},\"delete\":[]},\"mods\":{\"mode\":\"mirror\",\"serverPath\":\"mods\",\"files\":{\"1.12.2\\\\ChickenASM-1.12-1.0.2.7.jar\":\"6afc6ad00fd6bacd0c5b35d8342f0f79\"},\"delete\":[]},\"files\":{\"servers.dat\":\"4297f44b13955235245b2497399d7a93\",\"xray.zip\":\"del\"}}"; // 这里的"..."是你的JSON数据
        cn.pigeon.update.tasks.VerifyFiles verifyFiles = new cn.pigeon.update.tasks.VerifyFiles(new File("D:\\WorkSpace\\Java\\HMCL\\.minecraft\\pigeon\\mods.json"), Paths.get("."));
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse("http://127.0.0.1")).newBuilder();
        builder.addPathSegment("api");
        builder.addPathSegment("launcher");
        builder.addPathSegment("get-source");
        builder.addQueryParameter("macAddress", Utils.getMacAddress());
        builder.addQueryParameter("username", "1");
        builder.addQueryParameter("uuid", "uuid");
        builder.addQueryParameter("accessKey", "accessToken");
        builder.addQueryParameter("packName", "packName");
        Map<File, String> requireFile = verifyFiles.verifyFile();
        Map<URL, File> fileMap = Utils.prepareForDownload(builder, requireFile);
        for (Map.Entry<File, String> entry : requireFile.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
//        for (Map.Entry<URL, File> entry : fileMap.entrySet()){
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
//        // 创建Gson对象
//        Gson gson = new Gson();
//
//        SyncConfig syncConfig = new SyncConfig(json);
//        System.out.println(syncConfig);
//        Path path = Paths.get("config").resolve("package.json");
//        System.out.println(path.toFile());
        // 将JSON数据解析为Map<String, Object>
//        Map<String, Object> jsonData = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
//
//        if (jsonData.containsKey("data")) {
//            if (jsonData.get("data") instanceof List<?>) {
//                @SuppressWarnings("unchecked")
//                List<String> folderConfig = (List<String>) jsonData.get("data");
//                for (String name : folderConfig) {
//                    if (jsonData.containsKey(name)) {
//                        @SuppressWarnings("unchecked")
//                        Map<String, Object> object = (Map<String, Object>) jsonData.get(name);
//                        SyncFolderConfig syncFolderConfig = new SyncFolderConfig(name, object);
//                        System.out.println(syncFolderConfig.getServerPath());
//                        System.out.println(syncFolderConfig.getMode());
//                        System.out.println(syncFolderConfig.getFiles());
//                        System.out.println(syncFolderConfig.getDelete());
//                        for (Map.Entry<String, String> entry :
//                                syncFolderConfig.getFiles().entrySet()) {
//                            System.out.println(entry.getKey());
//                            System.out.println(entry.getValue());
//                        }
//                        System.out.println(syncFolderConfig);
//                    }
//                }
//            }
//        }

        // 获取config字段的值
//        if (jsonData.containsKey("config")) {
//            Map<String, Object> configData = (Map<String, Object>) jsonData.get("config");
//            // 处理configData中的可选字段
//            String mode = (String) configData.get("mode");
//            String serverPath = (String) configData.get("serverPath");
//            Map<String, String> files = (Map<String, String>) configData.get("files");
//            // ...
//        }

        // 获取mods字段的值
//        if (jsonData.containsKey("mods")) {
//            Map<String, Object> modsData = (Map<String, Object>) jsonData.get("mods");
//            // 处理modsData中的可选字段
//            String mode = (String) modsData.get("mode");
//            String serverPath = (String) modsData.get("serverPath");
//            Map<String, String> files = (Map<String, String>) modsData.get("files");
//            // ...
//        }

        // 获取files字段的值
//        Map<String, String> filesData = (Map<String, String>) jsonData.get("files");
        // 处理filesData中的字段
        // ...
    }
}