package com.example.koinapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CircleIndicator extends RecyclerView.ItemDecoration {
    private final Paint enable = new Paint();
    private final Paint disable = new Paint();
    private final float radius;


    public CircleIndicator(@NonNull Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, displayMetrics);
        disable.setStyle(Paint.Style.FILL);
        disable.setColor(Color.WHITE);
        disable.setAlpha(40);
        disable.setAntiAlias(true);

        enable.setStyle(Paint.Style.FILL);
        enable.setColor(Color.WHITE);
        enable.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter != null) {
            float totalWidth = 2 * radius * adapter.getItemCount();
            float x = (parent.getWidth() - totalWidth) / 2f;
            float y = parent.getHeight() - 2 * radius;
            final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            int current = RecyclerView.NO_POSITION;
            if (layoutManager instanceof LinearLayoutManager) {
                current = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            }
            for (int i = 0; i < adapter.getItemCount(); i++) {
                drawIndicator(c, x + 4 * radius * i, y, current == i);
            }
        }
    }

    private void drawIndicator(@NonNull Canvas c, float x, float y, boolean active) {
        c.drawCircle(x, y, radius, active ? enable : disable);
    }
}
