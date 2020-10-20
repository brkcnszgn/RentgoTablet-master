package com.creatifsoftware.rentgoservice.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.EquipmentDashboardItemBinding;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.utils.CommonMethods;
import com.creatifsoftware.rentgoservice.view.callback.EquipmentClickCallback;

import java.util.List;

public class EquipmentDashboardAdapter extends RecyclerView.Adapter<EquipmentDashboardAdapter.EquipmentListViewHolder> {
    @Nullable
    private final EquipmentClickCallback equipmentClickCallback;
    private final Context context;
    List<? extends Equipment> equipmentList;

    public EquipmentDashboardAdapter(@Nullable EquipmentClickCallback equipmentClickCallback, Context context) {
        this.equipmentClickCallback = equipmentClickCallback;
        this.context = context;
    }

    public void setEquipmentList(final List<? extends Equipment> equipmentList) {
        if (this.equipmentList == null) {
            this.equipmentList = equipmentList;
            notifyItemRangeInserted(0, equipmentList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return EquipmentDashboardAdapter.this.equipmentList.size();
                }

                @Override
                public int getNewListSize() {
                    return equipmentList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return EquipmentDashboardAdapter.this.equipmentList.get(oldItemPosition).equipmentId.equals(equipmentList.get(newItemPosition).equipmentId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Equipment items = equipmentList.get(newItemPosition);
                    Equipment old = EquipmentDashboardAdapter.this.equipmentList.get(oldItemPosition);
                    return items.equipmentId.equals(old.equipmentId);
                }
            });
            this.equipmentList = equipmentList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public EquipmentDashboardAdapter.EquipmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EquipmentDashboardItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.equipment_dashboard_item,
                        parent, false);

        binding.setCallback(equipmentClickCallback);
        return new EquipmentListViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull EquipmentDashboardAdapter.EquipmentListViewHolder holder, int position) {
        holder.binding.setEquipment(equipmentList.get(position));
        holder.binding.groupCode.setText(CommonMethods.instance.getSelectedGroupCodeInformation(equipmentList.get(position).groupCodeId).groupCodeName);
        holder.binding.statusName.setText(CommonMethods.instance.getSelectedStatusInformation(context, equipmentList.get(position).statusReason).label);

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return equipmentList == null ? 0 : equipmentList.size();
    }

    static class EquipmentListViewHolder extends RecyclerView.ViewHolder {

        final EquipmentDashboardItemBinding binding;

        EquipmentListViewHolder(EquipmentDashboardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
