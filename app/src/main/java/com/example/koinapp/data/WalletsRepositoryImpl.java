package com.example.koinapp.data;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
class WalletsRepositoryImpl implements WalletsRepository {

    private final FirebaseFirestore firestore;
    private final CoinReposytory coinsRepository;

    @Inject
    WalletsRepositoryImpl(CoinReposytory coinsRepository) {
        this.firestore = FirebaseFirestore.getInstance();
        this.coinsRepository = coinsRepository;
    }

    @NonNull
    @Override
    public Observable<List<Wallet>> wallets(@NonNull Currency currency) {
        return Observable
                .<QuerySnapshot>create(emitter -> {
                    final ListenerRegistration registration = firestore
                            .collection("wallets")
                            .orderBy("created_at", Query.Direction.ASCENDING)
                            .addSnapshotListener((snapshots, e) -> {
                                if (emitter.isDisposed()) return;
                                if (snapshots != null) {
                                    emitter.onNext(snapshots);
                                } else if (e != null) {
                                    emitter.tryOnError(e);
                                }
                            });
                    emitter.setCancellable(registration::remove);
                })
                .map(QuerySnapshot::getDocuments)
                .switchMapSingle((documents) -> Observable
                        .fromIterable(documents)
                        .switchMapSingle((document) -> coinsRepository
                                .coin(currency, document.getLong("coinId"))
                                .map((coin) -> Wallet.create(
                                        document.getId(),
                                        coin,
                                        document.getDouble("balance")
                                ))
                        ).toList()
                );
    }


    @NonNull
    @Override
    public Observable<List<Transaction>> transactions(@NonNull Wallet wallet) {
        return Observable.empty();
    }
}
