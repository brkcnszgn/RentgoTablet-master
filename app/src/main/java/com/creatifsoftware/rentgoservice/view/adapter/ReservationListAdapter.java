package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.ReservationListItemBinding;
import com.creatifsoftware.rentgoservice.model.ReservationItem;
import com.creatifsoftware.rentgoservice.view.callback.ReservationClickCallback;

import java.util.List;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ReservationListViewHolder> {
    @Nullable
    private final ReservationClickCallback reservationClickCallback;
    List<? extends ReservationItem> itemList;

    public ReservationListAdapter(@Nullable ReservationClickCallback reservationClickCallback) {
        this.reservationClickCallback = reservationClickCallback;
    }

    public void setItemList(final List<? extends ReservationItem> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ReservationListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ReservationListAdapter.this.itemList.get(oldItemPosition).reservationId.equals(itemList.get(newItemPosition).reservationId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ReservationItem reservationItem = itemList.get(newItemPosition);
                    ReservationItem old = ReservationListAdapter.this.itemList.get(oldItemPosition);
                    return reservationItem.reservationId.equals(old.reservationId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ReservationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReservationListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.reservation_list_item,
                        parent, false);

        binding.setCallback(reservationClickCallback);

        return new ReservationListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationListViewHolder holder, int position) {
        holder.binding.setReservation(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ReservationListViewHolder extends RecyclerView.ViewHolder {

        final ReservationListItemBinding binding;

        public ReservationListViewHolder(ReservationListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
