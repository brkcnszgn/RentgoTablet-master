package com.creatifsoftware.filonova.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.EquipmentInformationItemBinding;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.Equipment;
import com.creatifsoftware.filonova.view.callback.EquipmentClickCallback;

import java.util.List;

public class EquipmentListAdapter extends RecyclerView.Adapter<EquipmentListAdapter.ItemListViewHolder> {
    @Nullable
    private final EquipmentClickCallback equipmentClickCallback;
    private final ContractItem selectedContract;
    List<? extends Equipment> itemList;

    public EquipmentListAdapter(@Nullable EquipmentClickCallback equipmentClickCallback, ContractItem contract) {
        this.equipmentClickCallback = equipmentClickCallback;
        this.selectedContract = contract;

    }

    public void setEquipmentList(final List<? extends Equipment> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());

        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return EquipmentListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return EquipmentListAdapter.this.itemList.get(oldItemPosition).equipmentId.equals(itemList.get(newItemPosition).equipmentId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Equipment equipment = itemList.get(newItemPosition);
                    Equipment old = EquipmentListAdapter.this.itemList.get(oldItemPosition);
                    return equipment.equipmentId.equals(old.equipmentId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EquipmentInformationItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.equipment_information_item,
                        parent, false);

        binding.setCallback(equipmentClickCallback);

        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        //holder.binding.ratingBar.setRating(itemList.get(position).fuelValue);
        holder.binding.setSelectedEquipment(itemList.get(position));
        holder.binding.setIsSelected(itemList.get(position).isSelected);
        holder.binding.setGroupCodeInformation(selectedContract.groupCodeInformation);
        holder.binding.executePendingBindings();
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemList.get(position).isSelected = !itemList.get(position).isSelected;
//                //holder.binding.relativeLayout.setBackgroundResource(R.drawable.equipment_information_selected_bg);
//                // Do your operations here like
//                //holder.txtTitle.setText("new title");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final EquipmentInformationItemBinding binding;

        public ItemListViewHolder(EquipmentInformationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
