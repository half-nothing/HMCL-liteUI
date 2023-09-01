package org.jackhuang.hmcl.ui.animation;

import org.jackhuang.hmcl.setting.ConfigHolder;

public final class AnimationUtils {

    private static final boolean enabled = !ConfigHolder.config().isAnimationDisabled();

    private AnimationUtils() {
    }

    /**
     * Trigger initialization of this class.
     * Should be called from {@link org.jackhuang.hmcl.setting.Settings#init()}.
     */
    @SuppressWarnings("JavadocReference")
    public static void init() {
    }

    public static boolean isAnimationEnabled() {
        return enabled;
    }
}
