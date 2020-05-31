package com.example.koinapp.data;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Singleton
class WalletsRepositoryImpl implements WalletsRepository {

    private final FirebaseFirestore firestore;
    private final CoinReposytory coinsRepository;
    private final Random random = new SecureRandom();

    @Inject
    WalletsRepositoryImpl(CoinReposytory coinRepository) {
        this.firestore = FirebaseFirestore.getInstance();
        this.coinsRepository = coinRepository;
    }

    @NonNull
    @Override
    public Observable<List<Wallet>> wallets(@NonNull Currency currency) {
        return Observable
                .<QuerySnapshot>create(emitter -> {
                    final ListenerRegistration registration = firestore
                            .collection("wallets")
                            .orderBy("at", Query.Direction.ASCENDING)
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
                        .flatMapSingle((document) -> coinsRepository
                                .coin(currency, Objects.requireNonNull(document
                                        .getLong("coinId"), "coinId"))

                                .map((coin) -> Wallet.create(
                                        document.getId(),
                                        coin,
                                        document.getDouble("balance")
                                ))
                        )
                        .toList()
                )
                ;
    }

    @NonNull
    @Override
    public Observable<List<Transaction>> transactions(@NonNull Wallet wallet) {
        return Observable
                .<QuerySnapshot>create(emitter -> {
                    final ListenerRegistration registration = firestore
                            .collection("wallets")
                            .document(wallet.uid())
                            .collection("transactions")
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
                        .map((document) -> Transaction.create(
                                document.getId(),
                                wallet.coin(), Objects.requireNonNull(
                                        document.getDouble("amount"), "amount"),
                                document.getDate("at")
                        ))
                        .toList()
                );
    }

    @NonNull
    @Override
    public Completable addWallet(@NonNull Currency currency, List<Integer> takenIds) {
        return coinsRepository.nextPopularCoin(currency, takenIds)
                .map((coin) -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("balance", 100 * (random.nextDouble() + 0.01));
                    data.put("coinId", coin.id());
                    data.put("at", FieldValue.serverTimestamp());
                    return data;
                })
                .flatMapCompletable((wallet) -> Completable.create((emitter) ->
                        firestore.collection("wallets").add(wallet)
                                .addOnSuccessListener((r) -> {
                                    if (!emitter.isDisposed()) emitter.onComplete();
                                })
                                .addOnFailureListener(emitter::tryOnError)));
    }

}
