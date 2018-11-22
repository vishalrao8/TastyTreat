package com.example.visha.tastytreat.utils;

import android.util.Log;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;

    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(false);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);

        if (isIdleNow && mCallback != null)
            Objects.requireNonNull(mCallback).onTransitionToIdle();
    }
}
