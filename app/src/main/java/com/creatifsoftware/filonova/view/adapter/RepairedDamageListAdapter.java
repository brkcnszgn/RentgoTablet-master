package com.creatifsoftware.filonova.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.RepairedDamageInformationItemBinding;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.view.callback.DamageItemClickCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepairedDamageListAdapter extends RecyclerView.Adapter<RepairedDamageListAdapter.ItemListViewHolder> {
    @Nullable
    private final DamageItemClickCallback damageItemClickCallback;
    List<? extends DamageItem> itemList;


    public RepairedDamageListAdapter(@Nullable DamageItemClickCallback damageItemClickCallback) {

        this.damageItemClickCallback = damageItemClickCallback;
    }

    public void setDamageList(final List<? extends DamageItem> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RepairedDamageListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RepairedDamageListAdapter.this.itemList.get(oldItemPosition).damageId.equals(itemList.get(newItemPosition).damageId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DamageItem damageItem = itemList.get(newItemPosition);
                    DamageItem old = RepairedDamageListAdapter.this.itemList.get(oldItemPosition);
                    return damageItem.damageId.equals(old.damageId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RepairedDamageInformationItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.repaired_damage_information_item,
                        parent, false);

        binding.setCallback(damageItemClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        DamageItem damageItem = itemList.get(position);
        holder.binding.setDamageItem(damageItem);

        if (damageItem.blobStoragePath != null) {
            Picasso.get().load(damageItem.blobStoragePath).into(holder.binding.damagePhoto);
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final RepairedDamageInformationItemBinding binding;

        public ItemListViewHolder(RepairedDamageInformationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
