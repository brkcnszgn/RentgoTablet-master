package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.EquipmentInventoryItemV2Binding;
import com.creatifsoftware.rentgoservice.model.EquipmentInventory;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentInventoryAvailableCallback;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentInventoryMissingCallback;

import java.util.List;

public class EquipmentInventoryListAdapter_v2 extends RecyclerView.Adapter<EquipmentInventoryListAdapter_v2.ItemListViewHolder> {
    @Nullable
    private final EquipmentInventoryAvailableCallback equipmentInventoryAvailableCallback;
    @Nullable
    private final EquipmentInventoryMissingCallback equipmentInventoryMissingCallback;
    List<? extends EquipmentInventory> itemList;

    public EquipmentInventoryListAdapter_v2(@Nullable EquipmentInventoryAvailableCallback equipmentInventoryAvailableCallback,
                                            @Nullable EquipmentInventoryMissingCallback equipmentInventoryMissingCallback) {
        this.equipmentInventoryAvailableCallback = equipmentInventoryAvailableCallback;
        this.equipmentInventoryMissingCallback = equipmentInventoryMissingCallback;
    }

    public void setEquipmentInventoryList(final List<? extends EquipmentInventory> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return EquipmentInventoryListAdapter_v2.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return EquipmentInventoryListAdapter_v2.this.itemList.get(oldItemPosition).logicalName.equals(itemList.get(newItemPosition).logicalName);
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
        EquipmentInventoryItemV2Binding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.equipment_inventory_item_v2,
                        parent, false);

        //binding.setCallback(equipmentInventoryClickCallback);

        binding.setAvailableCallback(equipmentInventoryAvailableCallback);
        binding.setMissingCallback(equipmentInventoryMissingCallback);

        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setEquipmentInventory(itemList.get(position));
        holder.binding.equipmentInventorySegment.setPosition(itemList.get(position).isExist ? 0 : 1, false);
        //holder.binding.availableSegment.setSelected(itemList.get(position).isExist);
        //holder.binding.missingSegment.setSelected(!itemList.get(position).isExist);
        //holder.binding.choiceA.setChecked(itemList.get(position).isExist);
        //holder.binding.choiceB.setChecked(!itemList.get(position).isExist);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final EquipmentInventoryItemV2Binding binding;

        public ItemListViewHolder(EquipmentInventoryItemV2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
