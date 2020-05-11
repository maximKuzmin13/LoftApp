package com.example.koinapp.util;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;

public class OutlineCircle extends ViewOutlineProvider {

    public static void apply(@NonNull View view) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int min = Math.min(view.getHeight(), view.getWidth());
                outline.setRoundRect(0, 0, view.getHeight(), view.getWidth(), min / 2f);
            }
        });
        view.setClipToOutline(true);
    }

    @Override
    public void getOutline(View view, Outline outline) {
        int min = Math.min(view.getHeight(), view.getWidth());
        outline.setRoundRect(0, 0, view.getHeight(), view.getWidth(), min / 2f);
    }
}
