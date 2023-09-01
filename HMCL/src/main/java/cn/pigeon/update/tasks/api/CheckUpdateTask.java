package cn.pigeon.update.tasks.api;

import cn.pigeon.update.Static;
import cn.pigeon.update.data.Respond;
import cn.pigeon.update.data.Token;
import cn.pigeon.update.enums.SyncMode;
import cn.pigeon.update.exception.HttpRequestException;
import cn.pigeon.update.utils.Utils;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jackhuang.hmcl.auth.Account;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.util.gson.JsonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;

public class CheckUpdateTask extends Task<SyncMode[]> {
    private final String uuid;
    private final String username;
    private final String packName;
    private final File configFile;
    private final Token token;
    private final File modsConfigFile;
    private final File tempDir;
    private final SyncMode[] syncModes = {SyncMode.INCREMENT, SyncMode.INCREMENT};

    public CheckUpdateTask(Account account, String packName, Token token, File configFile, File modsConfigFile, File tempDir) {
        this.uuid = account.getUUID().toString().replace("-", "");
        this.username = account.getUsername();
        this.packName = packName;
        this.configFile = configFile;
        this.token = token;
        this.modsConfigFile = modsConfigFile;
        this.tempDir = tempDir;
    }

    @Override
    public void execute() throws Exception {
        updateProgress(0);
        checkUpdate();
        updateProgress(5, 5);
    }

    @Override
    public SyncMode[] getResult() {
        return syncModes;
    }

    public void checkUpdate() throws IOException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(config().getBaseUrl())).newBuilder();
        updateProgress(1, 5);
        builder.addPathSegment("api");
        builder.addPathSegment("check-update");
        builder.addQueryParameter("macAddress", Utils.getMacAddress());
        builder.addQueryParameter("username", username);
        builder.addQueryParameter("uuid", uuid);
        builder.addQueryParameter("accessKey", token.key);
        builder.addQueryParameter("packName", packName);
        builder.addQueryParameter("localSource", Utils.calculateMD5(Utils.calculateMD5(modsConfigFile) + Utils.calculateMD5(configFile)));
        updateProgress(2, 5);
        String url = builder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        updateProgress(3, 5);
        Response response = Static.okHttpClient.newCall(request).execute();
        syncModes[0] = SyncMode.valueOf(Objects.requireNonNull(response.header("X-Mod-Sync-Mode", "increment")).toUpperCase());
        syncModes[1] = SyncMode.valueOf(Objects.requireNonNull(response.header("X-Config-Sync-Mode", "increment")).toUpperCase());
        Static.updateMaxThread = Integer.parseInt(Objects.requireNonNull(response.header("X-Update-Max-Threads", "8")));
        updateProgress(4, 5);
        if (response.code() == 304) {
            return;
        }
        ResponseBody responseBody = Objects.requireNonNull(response.body());
        if (response.isSuccessful()) {
            try (InputStream inputStream = response.body().byteStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(new File(tempDir, "config.zip"))) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }
            Utils.unzip(new File(tempDir, "config.zip"), tempDir.toPath());
            return;
        }
        Respond respond = JsonUtils.fromNonNullJson(responseBody.string(), Respond.class);
        throw  new HttpRequestException(response.code(), respond.msg);
    }
}
