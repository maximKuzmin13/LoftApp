package com.example.koinapp.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.R;
import com.example.koinapp.databinding.FmtConverterBinding;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ConverterFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final ConverterComponent component;

    private FmtConverterBinding binding;

    private ConverterViewModel viewModel;

    @Inject
    ConverterFragment(BaseComponent baseComponent) {
        component = DaggerConverterComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireParentFragment(), component.viewModelFactory())
                .get(ConverterViewModel.class);
        Timber.d("%s", viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fmt_converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FmtConverterBinding.bind(view);

        final NavController navController = NavHostFragment.findNavController(this);

        disposable.add(viewModel.topCoins().subscribe());

        disposable.add(RxView.clicks(binding.fromCoin).subscribe(v -> {
            final Bundle args = new Bundle();
            args.putInt(ConverterDialog.KEY_MODE, ConverterDialog.MODE_FROM);
            navController.navigate(R.id.converter_dialog, args);
        }));

        disposable.add(RxView.clicks(binding.toCoin).subscribe(v -> {
            final Bundle args = new Bundle();
            args.putInt(ConverterDialog.KEY_MODE, ConverterDialog.MODE_TO);
            navController.navigate(R.id.converter_dialog, args);
        }));

        disposable.add(viewModel.fromCoin().subscribe(coin -> {
            binding.fromCoin.setText(coin.symbol());
        }));
        disposable.add(viewModel.toCoin().subscribe(coin -> {
            binding.toCoin.setText(coin.symbol());
        }));

        disposable.add(RxTextView.textChanges(binding.from).subscribe(viewModel::fromValue));
        disposable.add(RxTextView.textChanges(binding.to).subscribe(viewModel::toValue));

        disposable.add(viewModel.fromValue()
                .distinctUntilChanged()
                .subscribe(text -> {
                    binding.from.setText(text);
                    binding.from.setSelection(text.length());
                }));
        disposable.add(viewModel.toValue()
                .distinctUntilChanged()
                .subscribe(text -> {
                    binding.to.setText(text);
                    binding.to.setSelection(text.length());
                }));
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        super.onDestroyView();
    }
}
