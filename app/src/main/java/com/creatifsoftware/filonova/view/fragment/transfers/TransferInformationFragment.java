package com.creatifsoftware.filonova.view.fragment.transfers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.databinding.FragmentTransferInformationBinding;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.TransferItem;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.view.fragment.base.BaseFragment;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForTransferDelivery;
import com.creatifsoftware.filonova.view.fragment.damageEntry.DamageEntryFragmentForTransferReturn;

/**
 * Created by kerembalaban on 24.04.2019 at 17:27.
 */
public class TransferInformationFragment extends BaseFragment implements Injectable {
    private FragmentTransferInformationBinding binding;

    public static TransferInformationFragment withTransferItem(TransferItem transferItem) {
        TransferInformationFragment fragment = new TransferInformationFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_TRANSFER, transferItem);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_information, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(0, true);
        mActivity.showProgressBar();
        binding.setTransfer(selectedTransfer);

        if (selectedTransfer.transferType == EnumUtils.TransferType.BRANCH.getIntValue()) {
            binding.transferLocation.setText(selectedTransfer.dropoffBranch.branchName);
        } else if (selectedTransfer.transferType == EnumUtils.TransferType.DAMAGE.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.FAULT.getIntValue() ||
                selectedTransfer.transferType == EnumUtils.TransferType.SERVICE.getIntValue()) {

            binding.transferLocation.setText(selectedTransfer.serviceName);
        } else if (selectedTransfer.transferType == EnumUtils.TransferType.FREE.getIntValue()) {
            binding.transferLocation.setText(EnumUtils.TransferType.FREE.getName(EnumUtils.TransferType.FREE.getIntValue()));
        } else if (selectedTransfer.transferType == EnumUtils.TransferType.FIRST_TRANSFER.getIntValue()) {
            binding.transferLocation.setText(EnumUtils.TransferType.FIRST_TRANSFER.getName(EnumUtils.TransferType.FIRST_TRANSFER.getIntValue()));
        } else {
            binding.transferLocation.setText(getString(R.string.second_hand_title));
        }

        binding.transferType.setText(EnumUtils.TransferType.NONE.getName(selectedTransfer.transferType));
    }

    @Override
    public void backButtonClicked() {
        super.backButtonClicked();
        mActivity.hideProgressBar();
    }

    @Override
    public void navigate() {
        BaseFragment nextFragment;
        if (getSelectedTransfer().statusCode == EnumUtils.TransferStatusCode.TRANSFERRED.getIntValue()) {
            nextFragment = DamageEntryFragmentForTransferReturn.forSelectedTransfer(selectedTransfer);
        } else {
            nextFragment = DamageEntryFragmentForTransferDelivery.forSelectedTransfer(selectedTransfer);
        }

        mActivity.show(nextFragment);
    }
}