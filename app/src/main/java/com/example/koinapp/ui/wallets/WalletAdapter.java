package com.example.koinapp.ui.wallets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.data.Wallet;
import com.example.koinapp.databinding.ItemWalletBinding;
import com.example.koinapp.util.ChangeFormatter;
import com.example.koinapp.util.ImageLoader;
import com.example.koinapp.util.PriceFormatter;

import java.util.Objects;

import javax.inject.Inject;

class WalletAdapter extends ListAdapter<Wallet, WalletAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private final PriceFormatter priceFormatter;

    private final ChangeFormatter changeFormatter;

    private final ImageLoader imageLoader;

    @Inject
    public WalletAdapter(PriceFormatter priceFormatter, ChangeFormatter changeFormatter, ImageLoader imageLoader) {
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
        this.changeFormatter = changeFormatter;
        this.imageLoader = imageLoader;
    }


    @NonNull
    @Override
    public WalletAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new WalletAdapter.ViewHolder(ItemWalletBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletAdapter.ViewHolder holder, int position) {

        final Wallet wallet = getItem(position);
        holder.binding.tvCoinLabel.setText(wallet.coin().symbol());
        holder.binding.tvCurrencyWallet.setText(priceFormatter.format(wallet.balance()));
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }
//
//    @Override
//    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView);
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemWalletBinding binding;

        ViewHolder(ItemWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
