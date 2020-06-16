package com.sab.catsmoments.adapters;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sab.catsmoments.databinding.MomentLayoutBinding;
import com.sab.catsmoments.models.Moment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MomentsListAdapter extends RecyclerView.Adapter<MomentsListAdapter.MomentHolder> {
    private List<Moment> momentList = new ArrayList<>();

    @NonNull
    @Override
    public MomentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MomentLayoutBinding binding = MomentLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new MomentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MomentHolder holder, int position) {
        Moment currentMoment = momentList.get(position);
        holder.binding.titleItemText.setText(currentMoment.getTitle());
        holder.binding.descriptionItemText.setText(currentMoment.getDescription());
        holder.binding.catNameItem.setText(currentMoment.getCatName());
        Picasso.get()
                .load(currentMoment.getImageUrl())
                .fit()
                .into(holder.binding.momentItemImage);
        holder.binding.itemTimestamp.setText((String) DateUtils
                .getRelativeTimeSpanString(currentMoment.getTimeAdded().getSeconds() * 1000));
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public class MomentHolder extends RecyclerView.ViewHolder {
        MomentLayoutBinding binding;

        public MomentHolder(@NonNull MomentLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setMomentList(List<Moment> momentList) {
        this.momentList = momentList;
        notifyDataSetChanged();
    }
}
