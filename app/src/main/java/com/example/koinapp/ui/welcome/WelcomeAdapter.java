package com.example.koinapp.ui.welcome;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koinapp.R;
import com.example.koinapp.databinding.WelcomePageBinding;

public class WelcomeAdapter extends RecyclerView.Adapter<WelcomeAdapter.ViewHolder> {

    private static final int[] IMAGES = {
            R.drawable.first,
            R.drawable.ic_second,
            R.drawable.ic_third
    };
    private static final int[] TITLES = {
            R.string.welcome_page_one_title,
            R.string.welcome_page_two_title,
            R.string.welcome_page_three_title
    };
    private static final int[] SUBTITLES = {
            R.string.welcome_page_one_subtitle,
            R.string.welcome_page_two_subtitle,
            R.string.welcome_page_three_subtitle
    };
    private LayoutInflater inflater;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(WelcomePageBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeAdapter.ViewHolder holder, int position) {
        holder.binding.ivWelcome.setImageResource(IMAGES[position]);
        holder.binding.titleWelcome.setText(TITLES[position]);
        holder.binding.subtitleWelcome.setText(SUBTITLES[position]);
    }

    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final WelcomePageBinding binding;

        ViewHolder(WelcomePageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
