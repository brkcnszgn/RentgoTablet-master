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
import com.creatifsoftware.rentgoservice.databinding.TransferListItemBinding;
import com.creatifsoftware.rentgoservice.model.TransferItem;
import com.creatifsoftware.rentgoservice.utils.CommonMethods;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.view.callback.TransferItemClickCallback;

import java.util.List;

public class TransferListAdapter extends RecyclerView.Adapter<TransferListAdapter.ItemListViewHolder> {
    @Nullable
    private final TransferItemClickCallback transferItemClickCallback;
    @Nullable
    private final Context context;
    List<? extends TransferItem> itemList;

    public TransferListAdapter(@Nullable TransferItemClickCallback transferItemClickCallback, @Nullable Context context) {
        this.transferItemClickCallback = transferItemClickCallback;
        this.context = context;
    }

    public void setItemList(final List<? extends TransferItem> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return TransferListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return TransferListAdapter.this.itemList.get(oldItemPosition).transferId.equals(itemList.get(newItemPosition).transferId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    TransferItem transferItem = itemList.get(newItemPosition);
                    TransferItem old = TransferListAdapter.this.itemList.get(oldItemPosition);
                    return transferItem.transferId.equals(old.transferId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransferListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.transfer_list_item,
                        parent, false);

        binding.setCallback(transferItemClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        TransferItem item = itemList.get(position);
        holder.binding.setTransferItem(item);
        if (CommonMethods.instance.getSelectedGroupCodeInformation(item.selectedEquipment.groupCodeId) != null) {
            holder.binding.groupCode.setText(CommonMethods.instance.getSelectedGroupCodeInformation(item.selectedEquipment.groupCodeId).groupCodeName);
        }

        holder.binding.transferType.setText(EnumUtils.TransferType.NONE.getName(item.transferType));
        if (item.transferType == EnumUtils.TransferType.FREE.getIntValue()) {
            holder.binding.transferLocation.setText(R.string.free_transfer_title);
        } else if (item.transferType == EnumUtils.TransferType.SECOND_HAND.getIntValue()) {
            holder.binding.transferLocation.setText(R.string.second_hand_title);
        } else if (item.transferType == EnumUtils.TransferType.BRANCH.getIntValue()) {
            holder.binding.transferLocation.setText(item.dropoffBranch.branchName);
        } else if (item.transferType == EnumUtils.TransferType.FIRST_TRANSFER.getIntValue()) {
            holder.binding.transferLocation.setText(item.dropoffBranch.branchName);
        } else {
            holder.binding.transferLocation.setText(item.serviceName);
        }

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final TransferListItemBinding binding;

        public ItemListViewHolder(TransferListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
