package com.example.koinapp.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.auto.value.AutoValue;

import java.util.List;

public interface CoinReposytory {

    @NonNull
    LiveData<List<Coin>> listings(@NonNull Query query);

    @AutoValue
    abstract class Query {
        @NonNull
        public static Builder builder() {
            return new AutoValue_CoinReposytory_Query.Builder()
                    .forceUpdate(true);
        }

        abstract String currency();

        abstract boolean forceUpdate();

        abstract SortBy sortBy();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder currency(String currency);

            public abstract Builder forceUpdate(boolean forceUpdate);

            public abstract Query build();

            public abstract Builder sortBy(SortBy sortBy);
        }
    }
}
