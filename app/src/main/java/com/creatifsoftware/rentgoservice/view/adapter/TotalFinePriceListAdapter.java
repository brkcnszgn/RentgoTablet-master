package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.TotalFineAmountItemBinding;
import com.creatifsoftware.rentgoservice.model.AdditionalProduct;

import java.util.List;

public class TotalFinePriceListAdapter extends RecyclerView.Adapter<TotalFinePriceListAdapter.ItemListViewHolder> {
    List<? extends AdditionalProduct> itemList;

    public void setTotalFineList(final List<? extends AdditionalProduct> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return TotalFinePriceListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return TotalFinePriceListAdapter.this.itemList.get(oldItemPosition).productId.equals(itemList.get(newItemPosition).productId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AdditionalProduct additionalProduct = itemList.get(newItemPosition);
                    AdditionalProduct old = TotalFinePriceListAdapter.this.itemList.get(oldItemPosition);
                    return additionalProduct.productId.equals(old.productId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TotalFineAmountItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.total_fine_amount_item,
                        parent, false);

        //binding.setCallback(additionalProductClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setItem(itemList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final TotalFineAmountItemBinding binding;

        public ItemListViewHolder(TotalFineAmountItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
