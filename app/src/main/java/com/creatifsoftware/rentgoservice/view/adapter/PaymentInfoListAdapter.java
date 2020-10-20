package com.creatifsoftware.rentgoservice.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.PaymentInfoItemBinding;
import com.creatifsoftware.rentgoservice.model.PaymentInfo;

import java.util.List;
import java.util.Locale;

public class PaymentInfoListAdapter extends RecyclerView.Adapter<PaymentInfoListAdapter.ItemListViewHolder> {
    List<? extends PaymentInfo> itemList;

    public void setPaymentInfoList(final List<? extends PaymentInfo> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return PaymentInfoListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return PaymentInfoListAdapter.this.itemList.get(oldItemPosition).paymentId.equals(itemList.get(newItemPosition).paymentId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    PaymentInfo paymentInfo = itemList.get(newItemPosition);
                    PaymentInfo old = PaymentInfoListAdapter.this.itemList.get(oldItemPosition);
                    return paymentInfo.paymentId.equals(old.paymentId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentInfoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.payment_info_item,
                        parent, false);

        //binding.setCallback(additionalProductClickCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setPaymentInfo(itemList.get(position));
        if (itemList.get(position).value > 1) {
            holder.binding.price.setText(String.format(Locale.getDefault(), "%s x %.2f TL", itemList.get(position).value, itemList.get(position).paymentInfoAmount));
        } else {
            holder.binding.price.setText(String.format(Locale.getDefault(), "%.2f TL", (itemList.get(position).paymentInfoAmount * itemList.get(position).value)));
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final PaymentInfoItemBinding binding;

        public ItemListViewHolder(PaymentInfoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
