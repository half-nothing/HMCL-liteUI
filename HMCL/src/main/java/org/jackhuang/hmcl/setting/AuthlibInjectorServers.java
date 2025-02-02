/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.jackhuang.hmcl.setting;

import com.google.gson.JsonParseException;
import org.jackhuang.hmcl.auth.authlibinjector.AuthlibInjectorServer;
import org.jackhuang.hmcl.task.Schedulers;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.util.gson.JsonUtils;
import org.jackhuang.hmcl.util.gson.Validation;
import org.jackhuang.hmcl.util.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;
import static org.jackhuang.hmcl.util.Logging.LOG;

public class AuthlibInjectorServers implements Validation {

    public static final String CONFIG_FILENAME = "authlib-injectors.json";
    private static final Path configLocation = Paths.get(CONFIG_FILENAME);
    private static AuthlibInjectorServers configInstance;
    private final List<String> urls;

    public AuthlibInjectorServers(List<String> urls) {
        this.urls = urls;
    }

    public synchronized static void init() {
        if (configInstance != null) {
            throw new IllegalStateException("AuthlibInjectorServers is already loaded");
        }

        configInstance = new AuthlibInjectorServers(Collections.emptyList());

        if (Files.exists(configLocation)) {
            try {
                String content = FileUtils.readText(configLocation);
                configInstance = JsonUtils.GSON.fromJson(content, AuthlibInjectorServers.class);
            } catch (IOException | JsonParseException e) {
                LOG.log(Level.WARNING, "Malformed authlib-injectors.json", e);
            }
        }

        if (ConfigHolder.isNewlyCreated() && !AuthlibInjectorServers.getConfigInstance().getUrls().isEmpty()) {
            config().setPreferredLoginType(Accounts.getLoginType(Accounts.FACTORY_AUTHLIB_INJECTOR));
            for (String url : AuthlibInjectorServers.getConfigInstance().getUrls()) {
                Task.supplyAsync(Schedulers.io(), () -> AuthlibInjectorServer.locateServer(url))
                        .thenAcceptAsync(Schedulers.javafx(), server -> config().getAuthlibInjectorServers().add(server))
                        .start();
            }
        }
    }

    public static AuthlibInjectorServers getConfigInstance() {
        return configInstance;
    }

    public List<String> getUrls() {
        return urls;
    }

    @Override
    public void validate() throws JsonParseException {
        if (urls == null)
            throw new JsonParseException("authlib-injectors.json -> urls cannot be null");
    }
}
