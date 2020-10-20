package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.GroupCodeItemBinding;
import com.creatifsoftware.rentgoservice.model.GroupCodeInformation;
import com.creatifsoftware.rentgoservice.view.callback.GroupCodeItemClickCallback;

import java.util.List;

public class GroupCodeListAdapter extends RecyclerView.Adapter<GroupCodeListAdapter.ItemListViewHolder> {
    @Nullable
    private final GroupCodeItemClickCallback groupCodeItemClickCallback;
    List<? extends GroupCodeInformation> itemList;

    public GroupCodeListAdapter(@Nullable GroupCodeItemClickCallback groupCodeItemClickCallback) {
        this.groupCodeItemClickCallback = groupCodeItemClickCallback;
    }

    public void setItemList(final List<? extends GroupCodeInformation> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return GroupCodeListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return GroupCodeListAdapter.this.itemList.get(oldItemPosition).groupCodeId.equals(itemList.get(newItemPosition).groupCodeId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    GroupCodeInformation groupCodeInformation = itemList.get(newItemPosition);
                    GroupCodeInformation old = GroupCodeListAdapter.this.itemList.get(oldItemPosition);
                    return groupCodeInformation.groupCodeId.equals(old.groupCodeId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupCodeItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.group_code_item,
                        parent, false);

        binding.setCallback(groupCodeItemClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setGroupCode(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final GroupCodeItemBinding binding;

        public ItemListViewHolder(GroupCodeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
