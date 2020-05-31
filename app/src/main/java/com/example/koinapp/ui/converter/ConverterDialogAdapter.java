package com.example.koinapp.ui.converter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.BuildConfig;
import com.example.koinapp.data.Coin;
import com.example.koinapp.databinding.ItemConverterDialogBinding;
import com.example.koinapp.util.ImageLoader;
import com.example.koinapp.util.OutlineCircle;

import java.util.Objects;

import javax.inject.Inject;

public class ConverterDialogAdapter extends ListAdapter<Coin, ConverterDialogAdapter.ViewHolder> {

    private final ImageLoader imageLoader;
    private LayoutInflater inflater;

    @Inject
    ConverterDialogAdapter(ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Coin>() {
            @Override
            public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.imageLoader = imageLoader;
    }

    @Override
    public Coin getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemConverterDialogBinding.inflate(inflater, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Coin coin = getItem(position);
        holder.binding.name.setText(coin.symbol() + " | " + coin.name());
        imageLoader
                .load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                .into(holder.binding.logo);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemConverterDialogBinding binding;

        ViewHolder(@NonNull ItemConverterDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            OutlineCircle.apply(binding.logo);
        }
    }
}
