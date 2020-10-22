package com.creatifsoftware.filonova.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.DamageTypeSelectionItemBinding;
import com.creatifsoftware.filonova.model.DamageType;
import com.creatifsoftware.filonova.view.callback.DamageTypeClickCallback;

import java.util.List;

public class DamageTypeListAdapter extends RecyclerView.Adapter<DamageTypeListAdapter.ItemListViewHolder> {
    @Nullable
    private final DamageTypeClickCallback damageTypeClickCallback;
    List<? extends DamageType> itemList;

    public DamageTypeListAdapter(@Nullable DamageTypeClickCallback damageTypeClickCallback) {
        this.damageTypeClickCallback = damageTypeClickCallback;
    }

    public void setItemList(final List<? extends DamageType> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return DamageTypeListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return DamageTypeListAdapter.this.itemList.get(oldItemPosition).damageTypeId.equals(itemList.get(newItemPosition).damageTypeId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DamageType damageType = itemList.get(newItemPosition);
                    DamageType old = DamageTypeListAdapter.this.itemList.get(oldItemPosition);
                    return damageType.damageTypeId.equals(old.damageTypeId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DamageTypeSelectionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.damage_type_selection_item,
                        parent, false);

        binding.setCallback(damageTypeClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setDamageType(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final DamageTypeSelectionItemBinding binding;

        public ItemListViewHolder(DamageTypeSelectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
