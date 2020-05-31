package com.example.koinapp.ui.rates;

import androidx.test.core.app.ApplicationProvider;

import com.example.koinapp.data.Coin;
import com.example.koinapp.data.CoinReposytory;
import com.example.koinapp.data.FakeCoin;
import com.example.koinapp.data.TestCoinRepository;
import com.example.koinapp.data.TestCurrencyRepository;
import com.example.koinapp.util.TestShedulers;
import com.google.api.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static com.google.common.truth.Truth.assertThat;

public class RatesViewModelTest {

    private TestCoinRepository coinRepository;
    private TestShedulers shedulers;
    private RatesViewModel viewModel;
    private Context context;

    @Before
    public void setUp() throws Exception {
        TestCurrencyRepository currencyRepository = new TestCurrencyRepository(ApplicationProvider.getApplicationContext());
        coinRepository = new TestCoinRepository();
        viewModel = new RatesViewModel(coinRepository, currencyRepository, shedulers);
    }

    @Test
    public void coins() {
        final TestObserver<List<Coin>> coinsTest = viewModel.coins().test();

        viewModel.isRefreshing().test().assertValue(true);
        final List<Coin> coins = Arrays.asList(new FakeCoin(), new FakeCoin());
        coinRepository.listings.onNext(coins);
        viewModel.isRefreshing().test().assertValue(false);
        coinsTest.assertValue(coins);

        CoinReposytory.Query query = coinRepository.lastListingsQuery;
        assertThat(query).isNotNull();
        assertThat(query.forceUpdate()).isTrue();

        viewModel.switchSortingOrder();

        viewModel.isRefreshing().test().assertValue(false);
        coinRepository.listings.onNext(coins);
        viewModel.isRefreshing().test().assertValue(false);

        query = coinRepository.lastListingsQuery;
        assertThat(query).isNotNull();
        assertThat(query.forceUpdate()).isFalse();
    }

}