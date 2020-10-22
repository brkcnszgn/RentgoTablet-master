package com.creatifsoftware.filonova.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.EquipmentInventoryItemBinding;
import com.creatifsoftware.filonova.model.EquipmentInventory;
import com.creatifsoftware.filonova.view.callback.EquipmentInventoryClickCallback;

import java.util.List;

public class EquipmentInventoryListAdapter extends RecyclerView.Adapter<EquipmentInventoryListAdapter.ItemListViewHolder> {
    @Nullable
    private final EquipmentInventoryClickCallback equipmentInventoryClickCallback;
    List<? extends EquipmentInventory> itemList;

    public EquipmentInventoryListAdapter(@Nullable EquipmentInventoryClickCallback equipmentInventoryClickCallback) {
        this.equipmentInventoryClickCallback = equipmentInventoryClickCallback;
    }

    public void setEquipmentInventoryList(final List<? extends EquipmentInventory> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return EquipmentInventoryListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return EquipmentInventoryListAdapter.this.itemList.get(oldItemPosition).logicalName.equals(itemList.get(newItemPosition).logicalName);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    EquipmentInventory equipment = itemList.get(newItemPosition);
                    EquipmentInventory old = itemList.get(oldItemPosition);
                    return equipment.logicalName.equals(old.logicalName);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EquipmentInventoryItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.equipment_inventory_item,
                        parent, false);

        binding.setCallback(equipmentInventoryClickCallback);

        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setEquipmentInventory(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final EquipmentInventoryItemBinding binding;

        public ItemListViewHolder(EquipmentInventoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
