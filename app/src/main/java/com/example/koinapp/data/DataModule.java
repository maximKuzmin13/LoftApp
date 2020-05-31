package com.example.koinapp.data;

import android.content.Context;

import androidx.room.Room;

import com.example.koinapp.BuildConfig;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public abstract class DataModule {


    @Provides
    static Moshi moshi() {
        final Moshi moshi = new Moshi.Builder().build();
        return moshi.newBuilder()
                .add(Coin.class, moshi.adapter(AutoValue_CmcCoin.class))
                .add(Listings.class, moshi.adapter(AutoValue_Listings.class))
                .build();
    }

    @Provides
    static Retrofit retrofit(OkHttpClient httpClient, Moshi moshi) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(httpClient.newBuilder()
                .addInterceptor(chain -> {
                    final Request request = chain.request();
                    return chain.proceed(request.newBuilder()
                            .addHeader(CoinService.API_KEY, BuildConfig.API_KEY)
                            .build());
                })
                .build());
        builder.baseUrl(BuildConfig.API_ENDPOINT);
        builder.addConverterFactory(MoshiConverterFactory.create(moshi));
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());
        return builder.build();
    }

    @Provides
    static CoinService coinService(Retrofit retrofit) {
        return retrofit.create(CoinService.class);
    }

    @Provides
    @Singleton
    static KoinDatabase koinDatabase(Context context) {
        if (BuildConfig.DEBUG) {
            return Room.inMemoryDatabaseBuilder(context, KoinDatabase.class).build();
        } else {
            return Room.databaseBuilder(context, KoinDatabase.class, "loft.db").build();
        }
    }

    @Binds
    abstract CoinReposytory coinReposytory(CoinReposytoryImpl impl);

    @Binds
    abstract CurrencyRepository currencyRepository(CurrencyRepositoryImpl impl);

    @Binds
    abstract WalletsRepository walletsRepository(WalletsRepositoryImpl impl);

}
