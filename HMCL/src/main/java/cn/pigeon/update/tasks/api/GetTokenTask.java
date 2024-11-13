package cn.pigeon.update.tasks.api;

import cn.pigeon.update.Static;
import cn.pigeon.update.data.Respond;
import cn.pigeon.update.data.Token;
import cn.pigeon.update.exception.ConnectionErrorException;
import cn.pigeon.update.exception.HttpRequestException;
import cn.pigeon.update.exception.InterfaceErrorException;
import cn.pigeon.update.utils.Utils;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;
import org.jackhuang.hmcl.auth.yggdrasil.YggdrasilAccount;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.util.gson.JsonUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.Objects;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;

public class GetTokenTask extends Task<Token> {

    private static Token token = null;
    private final String username;
    private final String uuid;
    private final String packName;

    public GetTokenTask(YggdrasilAccount account, String packName) {
        this.username = account.getCharacter();
        this.uuid = account.getUUID().toString().replace("-", "");
        this.packName = packName;
    }

    @Override
    public void execute() throws Exception {
        updateProgress(0);
        getAccessToken();
        updateProgress(5, 5);
    }

    @Override
    public Token getResult() {
        return token;
    }

    public void getAccessToken() throws IOException {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(config().getBaseUrl())).newBuilder();
        updateProgress(1, 5);
        builder.addPathSegment("api");
        builder.addPathSegment("launcher");
        builder.addPathSegment("get-access-key");
        updateProgress(2, 5);
        String url = builder.build().toString();
        String data = String.format("{\"macAddress\": \"%s\", \"username\": \"%s\", \"uuid\": \"%s\", \"packName\": \"%s\"}", Utils.getMacAddress(), username, uuid, packName);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(data, MediaType.get("application/json")))
                .build();
        updateProgress(3, 5);
        try {
            Response response = Static.okHttpClient.newCall(request).execute();
            updateProgress(4, 5);
            if (response.code() == 304) {
                return;
            }
            ResponseBody responseBody = Objects.requireNonNull(response.body());
            if (response.isSuccessful()) {
                token = JsonUtils.fromNonNullJson(responseBody.string(), Token.class);
                return;
            }
            Respond respond = JsonUtils.fromNonNullJson(responseBody.string(), Respond.class);
            throw new HttpRequestException(response.code(), respond.msg);
        } catch (JsonSyntaxException e) {
            throw new InterfaceErrorException();
        } catch (SSLException | SocketException e) {
            throw new ConnectionErrorException();
        }
    }
}