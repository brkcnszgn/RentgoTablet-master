package com.creatifsoftware.filonova.view.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.ExtraServicesItemBinding;
import com.creatifsoftware.filonova.model.AdditionalProduct;

import java.util.List;

public class ExtraServiceListAdapter extends RecyclerView.Adapter<ExtraServiceListAdapter.ItemListViewHolder> {
    List<? extends AdditionalProduct> itemList;

    public void setExtraServiceList(final List<? extends AdditionalProduct> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ExtraServiceListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ExtraServiceListAdapter.this.itemList.get(oldItemPosition).productId.equals(itemList.get(newItemPosition).productId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AdditionalProduct additionalProduct = itemList.get(newItemPosition);
                    AdditionalProduct old = ExtraServiceListAdapter.this.itemList.get(oldItemPosition);
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
        ExtraServicesItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.extra_services_item,
                        parent, false);

        //binding.setCallback(additionalProductClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setItem(itemList.get(position));
        //double price = itemList.get(position).actualTotalAmount * itemList.get(position).value;
        holder.binding.edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    double amount = Double.parseDouble(charSequence.toString());
                    holder.binding.getItem().actualAmount = amount;
                } catch (Exception exception) {
                    //Todo
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public List<? extends AdditionalProduct> getItemList() {
        return itemList;
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final ExtraServicesItemBinding binding;

        public ItemListViewHolder(ExtraServicesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
