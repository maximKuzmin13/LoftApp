package com.example.koinapp.ui.rates;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.BuildConfig;
import com.example.koinapp.R;
import com.example.koinapp.data.Coin;
import com.example.koinapp.databinding.ItemRatesBinding;
import com.example.koinapp.util.Formatter;
import com.example.koinapp.util.OutlineCircle;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {
    private int percentPlus = Color.GREEN;
    private int percentMinus = Color.RED;
    private final Formatter<Double> priceFormatter;
    private final Formatter<Double> changeFormatter;

    private LayoutInflater inflater;

    RatesAdapter(Formatter<Double> priceFormatter, Formatter<Double> changeFormatter) {
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
        this.priceFormatter = priceFormatter;
        this.changeFormatter = changeFormatter;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRatesBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Coin coin = getItem(position);
        holder.binding.symbol.setText(getItem(position).symbol());
        holder.binding.tvPrice.setText(priceFormatter.format(coin.price()));
        holder.binding.tvPriceChange.setText(changeFormatter.format(coin.change24h()));
        if (coin.change24h() > 0) {
            holder.binding.tvPriceChange.setTextColor(percentPlus);
        } else {
            holder.binding.tvPriceChange.setTextColor(percentMinus);
        }
        Picasso.get()
                .load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                .into(holder.binding.coinIcon);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final Context context = recyclerView.getContext();
        inflater = LayoutInflater.from(context);
        TypedValue v = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.percentPlus, v, true);
        percentPlus = v.data;
        context.getTheme().resolveAttribute(R.attr.percentMinus, v, true);
        percentMinus = v.data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRatesBinding binding;

        public ViewHolder(@NonNull ItemRatesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            OutlineCircle.apply(binding.coinIcon);
        }
    }
}
