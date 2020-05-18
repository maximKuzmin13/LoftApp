package com.example.koinapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {
        RoomCoin.class
}, version = 1)
public abstract class KoinDatabase extends RoomDatabase {
    abstract CoinDao coins();
}
