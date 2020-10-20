package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.AddedAdditionalProductItemBinding;
import com.creatifsoftware.rentgoservice.model.AdditionalProduct;

import java.util.List;
import java.util.Locale;

public class AddedAdditionalProductListAdapter extends RecyclerView.Adapter<AddedAdditionalProductListAdapter.ItemListViewHolder> {
    List<? extends AdditionalProduct> itemList;

    public void setAddedAdditionalProductList(final List<? extends AdditionalProduct> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return AddedAdditionalProductListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return AddedAdditionalProductListAdapter.this.itemList.get(oldItemPosition).productId.equals(itemList.get(newItemPosition).productId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AdditionalProduct additionalProduct = itemList.get(newItemPosition);
                    AdditionalProduct old = AddedAdditionalProductListAdapter.this.itemList.get(oldItemPosition);
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
        AddedAdditionalProductItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.added_additional_product_item,
                        parent, false);

        //binding.setCallback(additionalProductClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setAdditionalProductItem(itemList.get(position));
        //double price = itemList.get(position).actualTotalAmount * itemList.get(position).value;
        if (itemList.get(position).maxPieces > 1) {
            holder.binding.price.setText(String.format(Locale.getDefault(), "%s x %.2f TL", itemList.get(position).value, itemList.get(position).actualTotalAmount));
        } else {
            holder.binding.price.setText(String.format(Locale.getDefault(), "%.2f TL", (itemList.get(position).actualTotalAmount * itemList.get(position).value)));
        }

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final AddedAdditionalProductItemBinding binding;

        public ItemListViewHolder(AddedAdditionalProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
