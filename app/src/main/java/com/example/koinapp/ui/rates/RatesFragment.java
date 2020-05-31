package com.example.koinapp.ui.rates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.R;
import com.example.koinapp.databinding.FmtRatesBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class RatesFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final RatesComponent component;

    private RatesAdapter adapter;

    private RatesViewModel viewModel;

    private RecyclerView recyclerView;

    @Inject
    RatesFragment(BaseComponent baseComponent) {
        component = DaggerRatesComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, component.viewModelFactory())
                .get(RatesViewModel.class);
        adapter = component.ratesAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fmt_rates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.rv_rates);
        com.example.koinapp.databinding.FmtRatesBinding binding = FmtRatesBinding.bind(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        binding.refresh.setOnRefreshListener(viewModel::refresh);
        disposable.add(viewModel.coins().subscribe(adapter::submitList));
        disposable.add(viewModel.isRefreshing().subscribe(binding.refresh::setRefreshing));
        disposable.add(viewModel.onError().subscribe(e ->
                Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", v -> viewModel.retry())
                        .show()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.rates, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.currency_dialog == item.getItemId()) {
            NavHostFragment.findNavController(this).navigate(R.id.currency_dialog);
            return true;
        } else if (R.id.sort_dialog == item.getItemId()) {
            viewModel.switchSortingOrder();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        disposable.clear();
        super.onDestroyView();
    }

}
