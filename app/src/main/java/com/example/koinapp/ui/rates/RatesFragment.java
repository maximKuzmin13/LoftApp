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

import com.example.koinapp.R;
import com.example.koinapp.databinding.FmtRatesBinding;
import com.example.koinapp.util.ChangeFormatter;
import com.example.koinapp.util.PriceFormatter;

public class RatesFragment extends Fragment {

    private FmtRatesBinding binding;

    private RatesAdapter adapter;

    private RatesViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RatesViewModel.class);
        adapter = new RatesAdapter(new PriceFormatter(), new ChangeFormatter());
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
        binding = FmtRatesBinding.bind(view);
        binding.rvRates.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.rvRates.swapAdapter(adapter, false);
        binding.rvRates.setHasFixedSize(true);
        viewModel.coins().observe(getViewLifecycleOwner(), adapter::submitList);
        viewModel.isRefreshing().observe(getViewLifecycleOwner(), binding.refresh::setRefreshing);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        binding.rvRates.swapAdapter(null, false);
        super.onDestroyView();
    }

}
