package com.example.koinapp.ui.currency;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.R;
import com.example.koinapp.databinding.DCurrencyBinding;
import com.example.koinapp.util.OnItemClick;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.inject.Inject;

public class CurrencyDialog extends AppCompatDialogFragment {

    private final CurrencyComponent component;

    private DCurrencyBinding binding;

    private CurrencyViewModel viewModel;

    private CurrencyAdapter adapter;

    private OnItemClick onItemClick;

    @Inject
    CurrencyDialog(BaseComponent baseComponent) {
        component = DaggerCurrencyComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, component.viewModelFactory())
                .get(CurrencyViewModel.class);
        adapter = new CurrencyAdapter();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DCurrencyBinding.inflate(getLayoutInflater());
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.currency_text)
                .setView(binding.getRoot())
                .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.recyclerCurrency.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerCurrency.setAdapter(adapter);

        viewModel.allCurrencies().observe(this, adapter::submitList);
        onItemClick = new OnItemClick((v) -> {
            final RecyclerView.ViewHolder viewHolder = binding.recyclerCurrency.findContainingViewHolder(v);
            if (viewHolder != null) {
                viewModel.updateCurrency(adapter.getItem(viewHolder.getAdapterPosition()));
            }
            dismissAllowingStateLoss();
        });
        binding.recyclerCurrency.addOnItemTouchListener(onItemClick);
    }
    @Override
    public void onDestroy() {
        binding.recyclerCurrency.removeOnItemTouchListener(onItemClick);
        binding.recyclerCurrency.setAdapter(null);
        super.onDestroy();
    }
}
