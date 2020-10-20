package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.DamageDocumentSelectionItemBinding;
import com.creatifsoftware.rentgoservice.model.DamageDocument;
import com.creatifsoftware.rentgoservice.view.callback.DamageDocumentClickCallback;

import java.util.List;

public class DamageDocumentListAdapter extends RecyclerView.Adapter<DamageDocumentListAdapter.ItemListViewHolder> {
    @Nullable
    private final DamageDocumentClickCallback damageDocumentClickCallback;
    List<? extends DamageDocument> itemList;

    public DamageDocumentListAdapter(@Nullable DamageDocumentClickCallback damageDocumentClickCallback) {
        this.damageDocumentClickCallback = damageDocumentClickCallback;
    }

    public void setItemList(final List<? extends DamageDocument> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return DamageDocumentListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return DamageDocumentListAdapter.this.itemList.get(oldItemPosition).damageDocumentId.equals(itemList.get(newItemPosition).damageDocumentId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DamageDocument damageDocument = itemList.get(newItemPosition);
                    DamageDocument old = DamageDocumentListAdapter.this.itemList.get(oldItemPosition);
                    return damageDocument.damageDocumentId.equals(old.damageDocumentId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DamageDocumentSelectionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.damage_document_selection_item,
                        parent, false);

        binding.setCallback(damageDocumentClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setDamageDocument(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final DamageDocumentSelectionItemBinding binding;

        public ItemListViewHolder(DamageDocumentSelectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
