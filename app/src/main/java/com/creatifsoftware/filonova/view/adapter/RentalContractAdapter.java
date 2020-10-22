package com.creatifsoftware.filonova.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.CollectContractListItemBinding;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.view.callback.ContractClickCallback;

import java.util.List;

public class RentalContractAdapter extends RecyclerView.Adapter<RentalContractAdapter.ContractListViewHolder> {
    @Nullable
    private final ContractClickCallback contractClickCallback;
    List<? extends ContractItem> contractList;

    public RentalContractAdapter(@Nullable ContractClickCallback contractClickCallback) {
        this.contractClickCallback = contractClickCallback;
    }

    public void setContractList(final List<? extends ContractItem> contractList) {
        if (this.contractList == null) {
            this.contractList = contractList;
            notifyItemRangeInserted(0, contractList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RentalContractAdapter.this.contractList.size();
                }

                @Override
                public int getNewListSize() {
                    return contractList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RentalContractAdapter.this.contractList.get(oldItemPosition).contractNumber.equals(contractList.get(newItemPosition).contractNumber);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ContractItem contractItem = contractList.get(newItemPosition);
                    ContractItem old = RentalContractAdapter.this.contractList.get(oldItemPosition);
                    return contractItem.contractNumber.equals(old.contractNumber);
                }
            });

            this.contractList = contractList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ContractListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CollectContractListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.collect_contract_list_item,
                        parent, false);

        binding.setCallback(contractClickCallback);

        return new ContractListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractListViewHolder holder, int position) {
        holder.binding.setContract(contractList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return contractList == null ? 0 : contractList.size();
    }

    static class ContractListViewHolder extends RecyclerView.ViewHolder {

        final CollectContractListItemBinding binding;

        public ContractListViewHolder(CollectContractListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
