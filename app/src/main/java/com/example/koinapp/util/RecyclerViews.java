package com.example.koinapp.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViews {

    public static void onItemClick(@NonNull RecyclerView rv, @NonNull View.OnClickListener listener) {
        rv.addOnItemTouchListener(new ClickHelper(rv.getContext(), listener));
    }

    private static class ClickHelper implements RecyclerView.OnItemTouchListener {

        private final GestureDetectorCompat gestureDetectorCompat;
        @NonNull
        private final View.OnClickListener listener;

        ClickHelper(@NonNull Context context, @NonNull View.OnClickListener listener) {
            this.gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
            this.listener = listener;
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            final View view = rv.findChildViewUnder(e.getX(), e.getY());
            if (view != null && gestureDetectorCompat.onTouchEvent(e)) {
                listener.onClick(view);
            }

            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
