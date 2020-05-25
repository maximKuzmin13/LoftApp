package com.example.koinapp.data;

import androidx.annotation.NonNull;

import com.example.koinapp.util.RxShedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
public class CoinReposytoryImpl implements CoinReposytory {

    private final CoinService api;

    private final KoinDatabase db;

    private final RxShedulers shedulers;

    @Inject
    CoinReposytoryImpl(CoinService api, KoinDatabase db, RxShedulers shedulers) {
        this.api = api;
        this.db = db;
        this.shedulers = shedulers;
    }

    @NonNull
    @Override
    public Observable<List<Coin>> listings(@NonNull Query query) {

        return Observable.fromCallable(() -> query.forceUpdate() || db.coins().coinsCount() == 0)
                .switchMap((f) -> f ? api.listings(query.currency()) : Observable.empty())
                .map(listings -> mapToRoomCoins(query, listings.data()))
                .doOnNext((coins) -> db.coins().insert(coins))
                .switchMap((coins) -> fetchFromDb(query))
                .switchIfEmpty(fetchFromDb(query))
                .<List<Coin>>map(Collections::unmodifiableList)
                .subscribeOn(shedulers.io())
                ;
    }

    @NonNull
    @Override
    public Single<Coin> coin(@NonNull Currency currency, long id) {
        return listings(Query.builder().currency(currency.code()).forceUpdate(false).build())
                .switchMapSingle((coins) -> db.coins().fetchOne(id))
                .firstOrError()
                .map((coin) -> coin);

    }

    private Observable<List<RoomCoin>> fetchFromDb(Query query) {
        if (query.sortBy() == SortBy.PRICE) {
            return db.coins().fetchAllSortByPrice();
        } else {
            return db.coins().fetchAllSortByRank();
        }
    }

    private List<RoomCoin> mapToRoomCoins(Query query, List<? extends Coin> data) {
        List<RoomCoin> roomCoins = new ArrayList<>(data.size());
        for (Coin coin : data) {
            roomCoins.add(RoomCoin.create(
                    coin.name(),
                    coin.symbol(),
                    coin.rank(),
                    coin.price(),
                    coin.change24h(),
                    query.currency(),
                    coin.id()
            ));
        }
        return roomCoins;
    }
}