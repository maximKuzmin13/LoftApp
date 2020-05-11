package com.example.koinapp.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import timber.log.Timber;

public class DebugTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        final StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        final StackTraceElement stackTraceElement = stackTrace[5];
        super.log(priority, tag, String.format(Locale.US,
                "[%s] %s(%s:%d): %s",
                Thread.currentThread().getName(),
                stackTraceElement.getMethodName(),
                stackTraceElement.getFileName(),
                stackTraceElement.getLineNumber(),
                message)
                , t);
    }
}
