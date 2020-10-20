package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.AvailabilityDataItemBinding;
import com.creatifsoftware.rentgoservice.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.rentgoservice.view.callback.AvailabilityItemClickCallback;

import java.util.List;
import java.util.Locale;

public class AvailabilityListAdapter extends RecyclerView.Adapter<AvailabilityListAdapter.ItemListViewHolder> {
    @Nullable
    private final AvailabilityItemClickCallback availabilityItemClickCallback;
    List<? extends AvailabilityGroupCodeInformation> itemList;

    public AvailabilityListAdapter(@Nullable AvailabilityItemClickCallback availabilityItemClickCallback) {
        this.availabilityItemClickCallback = availabilityItemClickCallback;
    }

    public void setItemList(final List<? extends AvailabilityGroupCodeInformation> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return AvailabilityListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return AvailabilityListAdapter.this.itemList.get(oldItemPosition).groupCodeId.equals(itemList.get(newItemPosition).groupCodeId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AvailabilityGroupCodeInformation equipment = itemList.get(newItemPosition);
                    AvailabilityGroupCodeInformation old = AvailabilityListAdapter.this.itemList.get(oldItemPosition);
                    return equipment.groupCodeId.equals(old.groupCodeId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AvailabilityDataItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.availability_data_item,
                        parent, false);

        binding.setCallback(availabilityItemClickCallback);

        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setGroupCodeInformation(itemList.get(position));
        holder.binding.toBePaidAmount.setText(String.format(Locale.getDefault(), "%.02f TL", itemList.get(position).amountToBePaid));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final AvailabilityDataItemBinding binding;

        public ItemListViewHolder(AvailabilityDataItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
