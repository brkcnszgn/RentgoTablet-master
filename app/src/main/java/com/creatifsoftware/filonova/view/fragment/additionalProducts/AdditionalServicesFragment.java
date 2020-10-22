package com.creatifsoftware.filonova.view.fragment.additionalProducts;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.di.Injectable;
import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.utils.CommonMethods;
import com.creatifsoftware.filonova.utils.EnumUtils;
import com.creatifsoftware.filonova.view.fragment.contractSummary.ContractSummaryForDeliveryFragment;
import com.creatifsoftware.filonova.viewmodel.AdditionalProductsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 12.02.2019 at 22:23.
 */
public class AdditionalServicesFragment extends AdditionalProductsFragment implements Injectable {
    public static final String TAG = "ProjectListFragment";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    /**
     * Creates project fragment for specific project ID
     */
    public static AdditionalServicesFragment forSelectedContract(ContractItem selectedContract) {
        AdditionalServicesFragment fragment = new AdditionalServicesFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(5, true);
        selectedContract = getSelectedContract();
        binding.additionalProductTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.additional_service_title), selectedContract.contractNumber, selectedContract.pnrNumber));
        binding.addedProductTitle.setText(getString(R.string.added_additional_service_amount_title));
        binding.totalAdditionalProductAmount.setText(calculateAdditionalProductsTotalAmount());
    }

    @Override
    public void observeAdditionalProductListViewModel(AdditionalProductsViewModel viewModel) {

    }

    @Override
    public void navigate() {
        super.navigate();
        ContractSummaryForDeliveryFragment contractSummaryFragment = ContractSummaryForDeliveryFragment.forSelectedContract(selectedContract);
        changeFragment(contractSummaryFragment);
    }

    @Override
    public void prepareAdditionalProductsRecyclerView() {
        super.prepareAdditionalProductsRecyclerView();
        additionalProductListAdapter.setAdditionalProductList(filterAdditionalServices());
    }

    @Override
    public void prepareAddedAdditionalProductsRecyclerView() {
        super.prepareAddedAdditionalProductsRecyclerView();
        addedAdditionalProductListAdapter.setAddedAdditionalProductList(selectedContract.addedAdditionalServices);
    }

    @Override
    public void changeAdditionalProductSelectionByParentOrChild(AdditionalProduct item) {
        if (!item.isChecked) {
            return;
        }

        selectedContract.additionalProductRules.forEach(rule -> {
            if (rule.productId.equals(item.productId)) {
                filterAdditionalServices().forEach(product -> {
                    if (product.productId.equals(rule.parentProductId)) {
                        product.value = 0;
                        product.isChecked = false;
                        prepareUpdatedProduct(product);
                    }
                });
            }
            if (rule.parentProductId.equals(item.productId)) {
                filterAdditionalServices().forEach(product -> {
                    if (product.productId.equals(rule.productId)) {
                        product.value = 0;
                        product.isChecked = false;
                        prepareUpdatedProduct(product);
                    }
                });
            }
        });
    }

    @Override
    public void prepareUpdatedProduct(AdditionalProduct item) {
        for (AdditionalProduct additionalProduct : selectedContract.addedAdditionalServices) {
            if (additionalProduct.productId.equals(item.productId)) {
                selectedContract.addedAdditionalServices.remove(additionalProduct);
                break;
            }
        }

        for (AdditionalProduct initialObject : selectedContract.initialAdditionalProductList) {
            if (initialObject.productId.equals(item.productId)) {
                if (item.value - initialObject.value != 0) {
                    AdditionalProduct additionalProduct = new AdditionalProduct(item);
                    additionalProduct.value = item.value - initialObject.value;
                    selectedContract.addedAdditionalServices.add(additionalProduct);
                    break;
                }
            }
        }

        binding.totalAdditionalProductAmount.setText(calculateAdditionalProductsTotalAmount());
        addedAdditionalProductListAdapter.notifyDataSetChanged();
        additionalProductListAdapter.notifyDataSetChanged();
    }

    private List<AdditionalProduct> filterAdditionalServices() {
        List<AdditionalProduct> tempList = new ArrayList<>();
        for (AdditionalProduct item : selectedContract.additionalProductList) {
            if (item.productType == EnumUtils.AdditionalProductType.SERVICE.getIntValue() &&
                    item.showonWeb && !item.productCode.equals("SRV004")) {
                tempList.add(item);
            }
        }

        return tempList;
    }

    @Override
    String calculateAdditionalProductsTotalAmount() {
        return String.format(Locale.getDefault(), "%.02f TL", CommonMethods.instance.calculateAddedAdditionalProductsAmount(selectedContract.addedAdditionalServices));
    }
}
