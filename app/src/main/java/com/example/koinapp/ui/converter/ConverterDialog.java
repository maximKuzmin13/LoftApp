package com.example.koinapp.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.R;
import com.example.koinapp.databinding.DCurrencyBinding;
import com.example.koinapp.util.RecyclerViews;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ConverterDialog extends BottomSheetDialogFragment {

    static final String KEY_MODE = "mode";

    static final int MODE_FROM = 1;

    static final int MODE_TO = 2;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final ConverterComponent component;

    private DCurrencyBinding binding;

    private ConverterViewModel viewModel;

    private ConverterDialogAdapter adapter;

    private int mode;

    @Inject
    ConverterDialog(BaseComponent baseComponent) {
        component = DaggerConverterComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireParentFragment(), component.viewModelFactory())
                .get(ConverterViewModel.class);
        adapter = component.converterDialogAdapter();
        mode = requireArguments().getInt(KEY_MODE, MODE_FROM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.d_currency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DCurrencyBinding.bind(view);

        binding.recyclerCurrency.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recyclerCurrency.setAdapter(adapter);

        disposable.add(viewModel.topCoins().subscribe(adapter::submitList));
        disposable.add(RecyclerViews.onClick(binding.recyclerCurrency)
                .map((position) -> adapter.getItem(position))
                .subscribe(coin -> {
                    if (MODE_FROM == mode) {
                        viewModel.fromCoin(coin);
                    } else {
                        viewModel.toCoin(coin);
                    }
                    dismissAllowingStateLoss();
                }));
    }

    @Override
    public void onDestroyView() {
        binding.recyclerCurrency.setAdapter(null);
        disposable.dispose();
        super.onDestroyView();
    }
}
