package com.creatifsoftware.filonova.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.DamageSizeSelectionItemBinding;
import com.creatifsoftware.filonova.model.DamageSize;
import com.creatifsoftware.filonova.view.callback.DamageSizeClickCallback;

import java.util.List;

public class DamageSizeListAdapter extends RecyclerView.Adapter<DamageSizeListAdapter.ItemListViewHolder> {
    @Nullable
    private final DamageSizeClickCallback damageSizeClickCallback;
    List<? extends DamageSize> itemList;

    public DamageSizeListAdapter(@Nullable DamageSizeClickCallback damageSizeClickCallback) {
        this.damageSizeClickCallback = damageSizeClickCallback;
    }

    public void setItemList(final List<? extends DamageSize> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return DamageSizeListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return DamageSizeListAdapter.this.itemList.get(oldItemPosition).damageSizeId.equals(itemList.get(newItemPosition).damageSizeId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DamageSize damageSize = itemList.get(newItemPosition);
                    DamageSize old = DamageSizeListAdapter.this.itemList.get(oldItemPosition);
                    return damageSize.damageSizeId.equals(old.damageSizeId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DamageSizeSelectionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.damage_size_selection_item,
                        parent, false);

        binding.setCallback(damageSizeClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setDamageSize(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final DamageSizeSelectionItemBinding binding;

        public ItemListViewHolder(DamageSizeSelectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
