package com.example.koinapp.ui.wallets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.BuildConfig;
import com.example.koinapp.data.Wallet;
import com.example.koinapp.databinding.ItemWalletBinding;
import com.example.koinapp.util.BalanceFormatter;
import com.example.koinapp.util.ImageLoader;
import com.example.koinapp.util.OutlineCircle;
import com.example.koinapp.util.PriceFormatter;

import java.util.Objects;

import javax.inject.Inject;

class WalletAdapter extends ListAdapter<Wallet, WalletAdapter.ViewHolder> {
    private final PriceFormatter priceFormatter;

    private final BalanceFormatter balanceFormatter;

    private final ImageLoader imageLoader;

    private LayoutInflater inflater;

    @Inject
    WalletAdapter(PriceFormatter priceFormatter, BalanceFormatter balanceFormatter, ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Wallet>() {
            @Override
            public boolean areItemsTheSame(@NonNull Wallet oldItem, @NonNull Wallet newItem) {
                return Objects.equals(oldItem.uid(), newItem.uid());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Wallet oldItem, @NonNull Wallet newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.priceFormatter = priceFormatter;
        this.balanceFormatter = balanceFormatter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemWalletBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Wallet wallet = getItem(position);
        holder.binding.tvCoinLabel.setText(wallet.coin().symbol());
        holder.binding.tvCurrencyWallet.setText(balanceFormatter.format(wallet));
        final double balance = wallet.balance() * wallet.coin().price();
        holder.binding.tvCurrencyWallet2.setText(priceFormatter.format(wallet.coin().currencyCode(), balance));
        imageLoader
                .load(BuildConfig.IMG_ENDPOINT + wallet.coin().id() + ".png")
                .into(holder.binding.ivWalletLogo);
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemWalletBinding binding;

        ViewHolder(@NonNull ItemWalletBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setClipToOutline(true);
            OutlineCircle.apply(binding.ivWalletLogo);
            this.binding = binding;
        }

    }

}