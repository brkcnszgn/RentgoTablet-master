package com.creatifsoftware.filonova.utils;

import android.view.View;

/**
 * Created by hamurcuabi on 22,October,2020
 **/
public abstract class ClickHelper implements View.OnClickListener {
    private static final long DOUBLE_CLICK_TIME_DELTA = 500;//milliseconds
    private long lastClickTime = 0;
    private boolean tap = true;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        // onDoubleClick(v);
        tap = clickTime - lastClickTime >= DOUBLE_CLICK_TIME_DELTA;

        v.postDelayed(() -> {
            if (tap)
                onSingleClick(v);
        }, DOUBLE_CLICK_TIME_DELTA);
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(View v);

    // public abstract void onDoubleClick(View v);
}

