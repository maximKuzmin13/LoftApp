package com.example.koinapp.ui.wallets;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.R;
import com.example.koinapp.databinding.FmtWalletsBinding;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class WalletsFragment extends Fragment {

    private SnapHelper walletSnapHelper;
    private final WalletsComponent component;
    private WalletAdapter walletAdapter;
    private WalletsViewModel viewModel;
    private FmtWalletsBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    public WalletsFragment(BaseComponent baseComponent) {
        component = DaggerWalletsComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, component.viewModelFactory())
                .get(WalletsViewModel.class);
        walletAdapter = component.walletAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fmt_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FmtWalletsBinding.bind(view);

        walletSnapHelper = new PagerSnapHelper();
        walletSnapHelper.attachToRecyclerView(binding.rvWallet);

        final TypedValue value = new TypedValue();
        view.getContext().getTheme().resolveAttribute(R.attr.walletCardWidth, value, true);
        final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        final int padding = (int) (displayMetrics.widthPixels - value.getDimension(displayMetrics)) / 2;
        binding.rvWallet.setPadding(padding, 0, padding, 0);
        binding.rvWallet.setClipToPadding(false);
        binding.rvWallet.setHasFixedSize(true);

        binding.rvWallet.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvWallet.addOnScrollListener(new CarouselScroller());
//        disposable.add(RecyclerViews
//                .onSnap(binding.rvWallet, walletSnapHelper)
//                .subscribe(viewModel::changeWallet));

        binding.rvWallet.setAdapter(walletAdapter);

        disposable.add(viewModel.wallets().subscribe(walletAdapter::submitList));
        disposable.add(viewModel.wallets().map(List::isEmpty).subscribe((isEmpty) -> {
//            binding.walletCard.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.rvWallet.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }));

        binding.transactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        binding.transactions.setAdapter(transactionsAdapter);
        binding.transactions.setHasFixedSize(true);

    }

    @Override
    public void onDestroyView() {
        walletSnapHelper.attachToRecyclerView(null);
        binding.rvWallet.setAdapter(null);
        super.onDestroyView();
    }

    private static class CarouselScroller extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            final int centerX = (recyclerView.getLeft() + recyclerView.getRight()) / 2;
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                final View child = recyclerView.getChildAt(i);
                final int childCenterX = (child.getLeft() + child.getRight()) / 2;
                final float childOffset = Math.abs(centerX - childCenterX) / (float) centerX;
                float factor = (float) Math.pow(0.85, childOffset);
                child.setScaleX(factor);
                child.setScaleY(factor);
            }
        }
    }
}

