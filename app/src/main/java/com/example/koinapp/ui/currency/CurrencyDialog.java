package com.example.koinapp.ui.currency;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.R;
import com.example.koinapp.data.Currency;
import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.databinding.DCurrencyBinding;
import com.example.koinapp.util.RecyclerViews;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CurrencyDialog extends AppCompatDialogFragment {

    private DCurrencyBinding binding;

    private CurrencyViewModel viewModel;

    private CurrencyAdapter adapter;

    private CurrencyRepository repository;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
//        repository = new CurrencyRepositoryImpl(requireContext());
        adapter = new CurrencyAdapter();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DCurrencyBinding.inflate(requireActivity().getLayoutInflater());
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.currency_text)
                .setView(binding.getRoot())
                .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.recyclerCurrency.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerCurrency.swapAdapter(adapter, false);
//        repository.actualCurrencies().observe(this, adapter::submitList);
        viewModel.allCurrencies().observe(this, adapter::submitList);
        RecyclerViews.onItemClick(binding.recyclerCurrency, (v) -> {
            final RecyclerView.ViewHolder viewHolder = binding.recyclerCurrency.findContainingViewHolder(v);
            if (viewHolder != null) {
                final Currency item = adapter.getItem(viewHolder.getAdapterPosition());
                viewModel.updateCurrency(item);
            }
            dismissAllowingStateLoss();
        });

    }

    @Override
    public void onDestroy() {
        binding.recyclerCurrency.setAdapter(null);
        super.onDestroy();
    }
}
