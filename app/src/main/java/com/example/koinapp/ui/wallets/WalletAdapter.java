package com.example.koinapp.ui.wallets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.databinding.ItemWalletBinding;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private LayoutInflater inflater;

    @NonNull
    @Override
    public WalletAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new WalletAdapter.ViewHolder(ItemWalletBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemWalletBinding binding;

        ViewHolder(ItemWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
