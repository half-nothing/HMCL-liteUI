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
package org.jackhuang.hmcl.util.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public final class JarUtils {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static final Optional<Path> THIS_JAR;
    private static final Manifest manifest;

    static {
        THIS_JAR = Optional.ofNullable(JarUtils.class.getProtectionDomain().getCodeSource())
                .map(codeSource -> {
                    try {
                        return Paths.get(codeSource.getLocation().toURI());
                    } catch (FileSystemNotFoundException | IllegalArgumentException | URISyntaxException e) {
                        return null;
                    }
                })
                .filter(Files::isRegularFile);

        manifest = THIS_JAR.flatMap(JarUtils::getManifest).orElseGet(Manifest::new);
    }

    private JarUtils() {
    }

    public static Optional<Path> thisJar() {
        return THIS_JAR;
    }

    public static String getManifestAttribute(String name, String defaultValue) {
        String value = manifest.getMainAttributes().getValue(name);
        return value != null ? value : defaultValue;
    }

    public static Optional<Manifest> getManifest(Path jar) {
        try (JarFile file = new JarFile(jar.toFile())) {
            return Optional.ofNullable(file.getManifest());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
