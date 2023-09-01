package cn.pigeon.update.tasks.api;

import cn.pigeon.update.Static;
import cn.pigeon.update.data.Respond;
import cn.pigeon.update.data.Token;
import cn.pigeon.update.exception.HttpRequestException;
import cn.pigeon.update.utils.Utils;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jackhuang.hmcl.auth.Account;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.util.gson.JsonUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;

public class GetTokenTask extends Task<Token> {

    private static Token token = null;
    private final String username;
    private final String uuid;

    public GetTokenTask(Account account) {
        this.username = account.getUsername();
        this.uuid = account.getUUID().toString().replace("-", "");
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

    public void getAccessToken() throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(config().getBaseUrl())).newBuilder();
        updateProgress(1, 5);
        builder.addPathSegment("api");
        builder.addPathSegment("get-access-key");
        builder.addQueryParameter("macAddress", Utils.getMacAddress());
        builder.addQueryParameter("username", username);
        builder.addQueryParameter("uuid", uuid);
        updateProgress(2, 5);
        String url = builder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        updateProgress(3, 5);
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
    }
}
