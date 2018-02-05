package com.katana.infrastructure.support;

import com.katana.ui.BuildConfig;

/**
 * Created by Akwasi Owusu on 2/1/18
 */

public final class Asserter {
    private Asserter() {
    }

    public static void doAssert(boolean condtion, String message) {
        if (BuildConfig.DEBUG && !condtion) {
            throw new AssertionError(message);
        }
    }
}
