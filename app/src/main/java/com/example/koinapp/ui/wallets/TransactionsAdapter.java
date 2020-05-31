package com.example.koinapp.ui.wallets;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.data.Transaction;
import com.example.koinapp.databinding.ItemTransactionsBinding;
import com.example.koinapp.util.PriceFormatter;

import java.util.Objects;

import javax.inject.Inject;

class TransactionsAdapter extends ListAdapter<Transaction, TransactionsAdapter.ViewHolder> {

    private final PriceFormatter priceFormatter;

    private LayoutInflater inflater;

    @Inject
    TransactionsAdapter(PriceFormatter priceFormatter) {
        super(new DiffUtil.ItemCallback<Transaction>() {
            @Override
            public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
                return Objects.equals(oldItem.uid(), newItem.uid());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.priceFormatter = priceFormatter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTransactionsBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction transaction = getItem(position);
        holder.binding.amount.setText(priceFormatter.format(transaction.amount()));
        final double fiatAmount = transaction.amount() * transaction.coin().price();
        holder.binding.change.setText(priceFormatter.format(transaction.coin().currencyCode(), fiatAmount));
        holder.binding.timestamp.setText(DateFormat.getDateFormat(inflater.getContext()).format(transaction.createdAt()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTransactionsBinding binding;

        ViewHolder(@NonNull ItemTransactionsBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setClipToOutline(true);
            this.binding = binding;
        }

    }

}
