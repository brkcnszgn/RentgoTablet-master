package com.creatifsoftware.filonova.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.AdditionalProductItemBinding;
import com.creatifsoftware.filonova.databinding.AdditionalProductItemForSelectionBinding;
import com.creatifsoftware.filonova.databinding.AdditionalProductItemIsMandotaryBinding;
import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.view.callback.AdditionalProductClickCallback;
import com.creatifsoftware.filonova.view.callback.AdditionalProductMinusClickCallback;
import com.creatifsoftware.filonova.view.callback.AdditionalProductPlusClickCallback;

import java.util.List;

public class AdditionalProductListAdapter extends RecyclerView.Adapter<AdditionalProductListAdapter.ItemListViewHolder> {
    @Nullable
    private final AdditionalProductClickCallback additionalProductClickCallback;
    @Nullable
    private final AdditionalProductPlusClickCallback plusTextClickListener;
    @Nullable
    private final AdditionalProductMinusClickCallback minusTextClickListener;
    List<? extends AdditionalProduct> itemList;

    public AdditionalProductListAdapter(@Nullable AdditionalProductClickCallback additionalProductClickCallback,
                                        @Nullable AdditionalProductPlusClickCallback plusTextClickListener,
                                        @Nullable AdditionalProductMinusClickCallback minusTextClickListener) {
        this.additionalProductClickCallback = additionalProductClickCallback;
        this.plusTextClickListener = plusTextClickListener;
        this.minusTextClickListener = minusTextClickListener;
    }

    public void setAdditionalProductList(final List<? extends AdditionalProduct> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return AdditionalProductListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return AdditionalProductListAdapter.this.itemList.get(oldItemPosition).productId.equals(itemList.get(newItemPosition).productId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AdditionalProduct additionalProduct = itemList.get(newItemPosition);
                    AdditionalProduct old = AdditionalProductListAdapter.this.itemList.get(oldItemPosition);
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
        if (itemList.get(viewType).isMandatory) {
            AdditionalProductItemIsMandotaryBinding isMandotaryBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.additional_product_item_is_mandotary,
                            parent, false);
            Context context = isMandotaryBinding.productImage.getContext();
            int id = context.getResources().getIdentifier(itemList.get(viewType).productCode.toLowerCase(), "drawable", context.getPackageName());
            isMandotaryBinding.productImage.setImageResource(id);
            if (itemList.get(viewType).tobePaidAmount == 0) {
                isMandotaryBinding.includeContractTitle.setText("Sözleşmenize Dahildir");
            }

            return new ItemListViewHolder(isMandotaryBinding);
        } else if (itemList.get(viewType).maxPieces == 1) {
            AdditionalProductItemForSelectionBinding bindingForSelection = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.additional_product_item_for_selection,
                            parent, false);
            bindingForSelection.setCallback(additionalProductClickCallback);
            Context context = bindingForSelection.productImage.getContext();
            int id = context.getResources().getIdentifier(itemList.get(viewType).productCode.toLowerCase(), "drawable", context.getPackageName());
            bindingForSelection.productImage.setImageResource(id);
            return new ItemListViewHolder(bindingForSelection);
        } else {
            AdditionalProductItemBinding binding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.additional_product_item,
                            parent, false);
            Context context = binding.productImage.getContext();
            int id = context.getResources().getIdentifier(itemList.get(viewType).productCode.toLowerCase(), "drawable", context.getPackageName());
            binding.productImage.setImageResource(id);
            binding.setPlusCallback(plusTextClickListener);
            binding.setMinusCallback(minusTextClickListener);
            return new ItemListViewHolder(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        AdditionalProduct item = itemList.get(position);
        if (item.isMandatory) {
            holder.isMandotaryBinding.setAdditionalProductItem(item);
            holder.isMandotaryBinding.executePendingBindings();
        } else if (item.maxPieces == 1) {
            holder.selectionBinding.setAdditionalProductItem(item);
            holder.selectionBinding.executePendingBindings();
        } else {
            holder.binding.setAdditionalProductItem(item);
            holder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        AdditionalProductItemBinding binding;
        AdditionalProductItemForSelectionBinding selectionBinding;
        AdditionalProductItemIsMandotaryBinding isMandotaryBinding;

        ItemListViewHolder(AdditionalProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        ItemListViewHolder(AdditionalProductItemForSelectionBinding selectionBinding) {
            super(selectionBinding.getRoot());
            this.selectionBinding = selectionBinding;
        }

        ItemListViewHolder(AdditionalProductItemIsMandotaryBinding isMandotaryBinding) {
            super(isMandotaryBinding.getRoot());
            this.isMandotaryBinding = isMandotaryBinding;
        }
    }
}
