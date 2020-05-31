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
import com.example.koinapp.util.ChangeFormatter;
import com.example.koinapp.util.ImageLoader;
import com.example.koinapp.util.OutlineCircle;
import com.example.koinapp.util.PriceFormatter;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {
    private int percentPlus = Color.GREEN;
    private int percentMinus = Color.RED;
    private final PriceFormatter priceFormatter;
    private final ChangeFormatter changeFormatter;
    private final ImageLoader imageLoader;

    private LayoutInflater inflater;

    @Inject
    RatesAdapter(PriceFormatter priceFormatter, ChangeFormatter changeFormatter, ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Coin>() {
            @Override
            public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return Objects.equals(oldItem, newItem);
            }

            @Override
            public Object getChangePayload(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return newItem;
            }
        });
        this.priceFormatter = priceFormatter;
        this.changeFormatter = changeFormatter;
        this.imageLoader = imageLoader;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRatesBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            final Coin coin = (Coin) payloads.get(0);
            holder.binding.tvPrice.setText(priceFormatter.format(coin.currencyCode(), coin.price()));
            holder.binding.tvPriceChange.setText(changeFormatter.format(coin.change24h()));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Coin coin = getItem(position);
        holder.binding.symbol.setText(coin.symbol());
        holder.binding.tvPrice.setText(priceFormatter.format(coin.currencyCode(), coin.price()));
        holder.binding.tvPriceChange.setText(changeFormatter.format(coin.change24h()));
        if (coin.change24h() > 0) {
            holder.binding.tvPriceChange.setTextColor(percentPlus);
        } else {
            holder.binding.tvPriceChange.setTextColor(percentMinus);
        }
        imageLoader
                .load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                .into(holder.binding.coinIcon);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final Context context = recyclerView.getContext();
        inflater = LayoutInflater.from(context);
        TypedValue v = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.percentMinus, v, true);
        percentMinus = v.data;
        context.getTheme().resolveAttribute(R.attr.percentPlus, v, true);
        percentPlus = v.data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemRatesBinding binding;

        ViewHolder(@NonNull ItemRatesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            OutlineCircle.apply(binding.coinIcon);
        }
    }
}
