package com.creatifsoftware.rentgoservice.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentBaseGroupChangeBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.interfaces.CommunicationInterface;
import com.creatifsoftware.rentgoservice.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.model.GroupCodeInformation;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;

import java.util.Locale;

/**
 * Created by kerembalaban on 7.03.2019 at 12:18.
 */


public class BaseGroupCodeChangeFragment extends Fragment implements Injectable {
    private static GroupCodeInformation groupCodeInformation;
    private static AvailabilityGroupCodeInformation updatedGroupCodeInformation;
    private static ContractItem selectedContract;
    private FragmentBaseGroupChangeBinding binding;
    private MainActivity mActivity;

    public static BaseGroupCodeChangeFragment withSelectedContract(GroupCodeInformation aGroupCodeInformation,
                                                                   AvailabilityGroupCodeInformation aUpdatedGroupCodeInformation,
                                                                   ContractItem aSelectedContract) {
        BaseGroupCodeChangeFragment fragment = new BaseGroupCodeChangeFragment();
        groupCodeInformation = aGroupCodeInformation;
        updatedGroupCodeInformation = aUpdatedGroupCodeInformation;
        selectedContract = aSelectedContract;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_group_change, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.setGroupCodeInformation(groupCodeInformation);
        binding.setSelectedGroupCodeInformation(updatedGroupCodeInformation);

        if (updatedGroupCodeInformation.isUpgrade) {
            binding.freeUpdateButton.setText(getString(R.string.upgrade_text));
            if ((updatedGroupCodeInformation.isPriceCalculatedSafely && !selectedContract.isEquipmentChanged) ||
                    selectedContract.customer.paymentMethod != EnumUtils.PaymentMethod.PAY_BROKER.getIntValue() ||
                    selectedContract.customer.paymentMethod != EnumUtils.PaymentMethod.PAY_OFFICE.getIntValue()) {
                binding.updateButton.setText(String.format(Locale.getDefault(), "Upsell (%.02f TL)", updatedGroupCodeInformation.amountToBePaid));
                binding.updateButton.setEnabled(true);
                binding.updateButton.setAlpha(1);
            } else {
                if (!updatedGroupCodeInformation.isPriceCalculatedSafely) {
                    binding.updateButton.setText("Fiyat hesaplanamadı, upsell yapılamaz");
                } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.PAY_BROKER.getIntValue() ||
                        selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.PAY_OFFICE.getIntValue()) {
                    binding.updateButton.setText("Broker sözleşmesinde upsell yapılamaz");
                } else if (selectedContract.isEquipmentChanged) {
                    binding.updateButton.setText("Araç değişikliği sırasında upsell yapılamaz");
                } else {
                    binding.updateButton.setText("Upsell yapılamaz");
                }

                binding.updateButton.setEnabled(false);
                binding.updateButton.setAlpha((float) 0.7);
            }

            binding.messageText.setText(getString(R.string.upgrade_upsell_message_text));
        } else {
            binding.freeUpdateButton.setText(getString(R.string.downgrade_text));
            if (updatedGroupCodeInformation.isPriceCalculatedSafely && !selectedContract.isEquipmentChanged ||
                    selectedContract.customer.paymentMethod != EnumUtils.PaymentMethod.PAY_BROKER.getIntValue() ||
                    selectedContract.customer.paymentMethod != EnumUtils.PaymentMethod.PAY_OFFICE.getIntValue()) {
                binding.updateButton.setText(String.format(Locale.getDefault(), "Downsell (%.02f TL)", updatedGroupCodeInformation.amountToBePaid));
                binding.updateButton.setEnabled(true);
                binding.updateButton.setAlpha(1);
            } else {
                if (!updatedGroupCodeInformation.isPriceCalculatedSafely) {
                    binding.updateButton.setText("Fiyat hesaplanamadı, downsell yapılamaz");
                } else if (selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.PAY_BROKER.getIntValue() ||
                        selectedContract.customer.paymentMethod == EnumUtils.PaymentMethod.PAY_OFFICE.getIntValue()) {
                    binding.updateButton.setText("Broker sözleşmesinde downsell yapılamaz");
                } else if (selectedContract.isEquipmentChanged) {
                    binding.updateButton.setText("Araç değişikliği sırasında downsell yapılamaz");
                } else {
                    binding.updateButton.setText("Upsell yapılamaz");
                }
                binding.updateButton.setEnabled(false);
                binding.updateButton.setAlpha((float) 0.7);
            }
            binding.messageText.setText(getString(R.string.downgrade_downsell_message_text));
        }

        binding.closeButtonLayout.setOnClickListener(view -> mActivity.onBackPressed());

        binding.freeUpdateButton.setOnClickListener(view -> {
            updatedGroupCodeInformation.changeType = updatedGroupCodeInformation.isUpgrade ?
                    EnumUtils.EquipmentChangeType.UPGRADE.getIntValue() :
                    EnumUtils.EquipmentChangeType.DOWNGRADE.getIntValue();
            groupCodeChangeButtonClick();
        });

        binding.updateButton.setOnClickListener(view -> {
            updatedGroupCodeInformation.changeType = updatedGroupCodeInformation.isUpgrade ?
                    EnumUtils.EquipmentChangeType.UPSELL.getIntValue() :
                    EnumUtils.EquipmentChangeType.DOWNSELL.getIntValue();
            groupCodeChangeButtonClick();
        });
    }

    private void groupCodeChangeButtonClick() {
        CommunicationInterface myInterface = (CommunicationInterface) getActivity();
        mActivity.onBackPressed();
        myInterface.groupCodeChanged(updatedGroupCodeInformation);
    }
}
