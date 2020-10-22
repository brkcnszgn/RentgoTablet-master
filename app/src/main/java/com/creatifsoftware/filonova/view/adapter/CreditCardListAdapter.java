package com.creatifsoftware.filonova.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.CreditCardItemBinding;
import com.creatifsoftware.filonova.model.CreditCard;
import com.creatifsoftware.filonova.utils.RegexUtils;
import com.creatifsoftware.filonova.view.callback.CreditCardClickCallback;

import java.util.List;

public class CreditCardListAdapter extends RecyclerView.Adapter<CreditCardListAdapter.ItemListViewHolder> {
    @Nullable
    private final CreditCardClickCallback creditCardClickCallback;
    List<? extends CreditCard> itemList;

    public CreditCardListAdapter(@Nullable CreditCardClickCallback creditCardClickCallback) {
        this.creditCardClickCallback = creditCardClickCallback;
    }

    public void setCardList(final List<? extends CreditCard> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CreditCardListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return CreditCardListAdapter.this.itemList.get(oldItemPosition).creditCardNumber.equals(itemList.get(newItemPosition).creditCardNumber);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    CreditCard equipment = itemList.get(newItemPosition);
                    CreditCard old = CreditCardListAdapter.this.itemList.get(oldItemPosition);
                    return equipment.creditCardNumber.equals(old.creditCardNumber);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CreditCardItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.credit_card_item,
                        parent, false);

        binding.setCallback(creditCardClickCallback);

        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.binding.setCreditCard(itemList.get(position));
        holder.binding.creditCardCheckbox.setChecked(itemList.get(position).isSelected);
        if (itemList.get(position).creditCardNumber.matches(RegexUtils.instance.masterCardCardRegex())) {
            holder.binding.visaCardIcon.setImageResource(R.drawable.icon_master_card);
        } else if (itemList.get(position).creditCardNumber.matches(RegexUtils.instance.visaCardRegex())) {
            holder.binding.visaCardIcon.setImageResource(R.drawable.icon_visa_card);
        } else {
            holder.binding.visaCardIcon.setImageResource(R.drawable.icon_visa_card);
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final CreditCardItemBinding binding;

        public ItemListViewHolder(CreditCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
