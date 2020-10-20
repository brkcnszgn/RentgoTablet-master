package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.EquipmentPartSelectionItemBinding;
import com.creatifsoftware.rentgoservice.model.EquipmentPart;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentPartClickCallback;

import java.util.List;

public class EquipmentPartListAdapter extends RecyclerView.Adapter<EquipmentPartListAdapter.ItemListViewHolder> {
    @Nullable
    private final EquipmentPartClickCallback equipmentPartClickCallback;
    List<? extends EquipmentPart> itemList;

    public EquipmentPartListAdapter(@Nullable EquipmentPartClickCallback equipmentPartClickCallback) {
        this.equipmentPartClickCallback = equipmentPartClickCallback;
    }

    public void setItemList(final List<? extends EquipmentPart> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return EquipmentPartListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return EquipmentPartListAdapter.this.itemList.get(oldItemPosition).equipmentPartId.equals(itemList.get(newItemPosition).equipmentPartId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    EquipmentPart equipmentPart = itemList.get(newItemPosition);
                    EquipmentPart old = EquipmentPartListAdapter.this.itemList.get(oldItemPosition);
                    return equipmentPart.equipmentPartId.equals(old.equipmentPartId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EquipmentPartSelectionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.equipment_part_selection_item,
                        parent, false);

        binding.setCallback(equipmentPartClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setEquipmentPart(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final EquipmentPartSelectionItemBinding binding;

        public ItemListViewHolder(EquipmentPartSelectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
