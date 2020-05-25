package com.example.koinapp.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CoinReposytory {

    @NonNull
    Observable<List<Coin>> listings(@NonNull Query query);

    @NonNull
    Single<Coin> coin(@NonNull Currency currency, long id);

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
