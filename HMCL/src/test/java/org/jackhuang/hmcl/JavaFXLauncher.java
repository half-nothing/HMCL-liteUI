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
package org.jackhuang.hmcl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class JavaFXLauncher {

    private static boolean started = false;

    static {
        // init JavaFX Toolkit
        try {
            // Java 9 or Latter
            final MethodHandle startup =
                    MethodHandles.publicLookup().findStatic(
                            javafx.application.Platform.class, "startup", MethodType.methodType(void.class, Runnable.class));
            startup.invokeExact((Runnable) () -> {
            });
            started = true;
        } catch (NoSuchMethodException e) {
            // Java 8
            try {
                Class.forName("javafx.embed.swing.JFXPanel").getDeclaredConstructor().newInstance();
                started = true;
            } catch (Throwable e0) {
                e0.printStackTrace();
            }
        } catch (IllegalStateException e) {
            started = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private JavaFXLauncher() {
    }

    public static void start() {
    }

    public static boolean isStarted() {
        return started;
    }
}
