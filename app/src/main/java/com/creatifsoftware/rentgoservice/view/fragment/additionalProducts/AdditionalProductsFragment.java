package com.creatifsoftware.rentgoservice.view.fragment.additionalProducts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.FragmentAdditionalProductsBinding;
import com.creatifsoftware.rentgoservice.di.Injectable;
import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.AdditionalProductRules;
import com.creatifsoftware.rentgoservice.model.ContractItem;
import com.creatifsoftware.rentgoservice.utils.CommonMethods;
import com.creatifsoftware.rentgoservice.utils.EnumUtils;
import com.creatifsoftware.rentgoservice.utils.SharedPrefUtils;
import com.creatifsoftware.rentgoservice.view.adapter.AddedAdditionalProductListAdapter;
import com.creatifsoftware.rentgoservice.view.adapter.AdditionalProductListAdapter;
import com.creatifsoftware.rentgoservice.view.callback.AdditionalProductClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.AdditionalProductMinusClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.AdditionalProductPlusClickCallback;
import com.creatifsoftware.rentgoservice.view.fragment.base.BaseFragment;
import com.creatifsoftware.rentgoservice.viewmodel.AdditionalProductsViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 12.02.2019 at 22:23.
 */
public class AdditionalProductsFragment extends BaseFragment implements Injectable {

    public static final String TAG = "ProjectListFragment";
    public AddedAdditionalProductListAdapter addedAdditionalProductListAdapter;
    public FragmentAdditionalProductsBinding binding;
    AdditionalProductListAdapter additionalProductListAdapter;
    public final AdditionalProductClickCallback additionalProductClickCallback = item -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) && !item.isMandatory) {
            item.isChecked = !item.isChecked;
            item.value = item.isChecked ? 1 : 0;
            changeAdditionalProductSelectionByParentOrChild(item);
            prepareUpdatedProduct(item);
        }
    };

    //double totalBasketAmount = 0;
    private final AdditionalProductPlusClickCallback plusTextClickCallback = item -> {
        if (item.value < item.maxPieces) {
            item.value++;
            //totalBasketAmount = totalBasketAmount + item.actualTotalAmount;
        }
        item.isChecked = item.value > 0;

        prepareUpdatedProduct(item);
    };
    private final AdditionalProductMinusClickCallback minusTextClickCallback = item -> {
        if (item.value != 0) {
            item.value--;
            //totalBasketAmount = totalBasketAmount - item.actualTotalAmount;
        }
        item.isChecked = item.value > 0;

        prepareUpdatedProduct(item);
    };
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public static AdditionalProductsFragment forSelectedContract(ContractItem selectedContract) {
        AdditionalProductsFragment fragment = new AdditionalProductsFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SELECTED_CONTRACT, selectedContract);
        fragment.setArguments(args);

        return fragment;
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_additional_products, container, false);

        prepareAdditionalProductsRecyclerView();
        prepareAddedAdditionalProductsRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStepView().go(4, true);
        final AdditionalProductsViewModel viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(AdditionalProductsViewModel.class);

        selectedContract = getSelectedContract();
        binding.additionalProductTitle.setText(String.format(Locale.getDefault(), "%s - %s (%s)", getString(R.string.additional_products_title), selectedContract.contractNumber, selectedContract.pnrNumber));
        binding.addedProductTitle.setText(getString(R.string.added_additional_product_amount_title));
        //totalBasketAmount = selectedContract.totalAdditionalProductsAmount ;
        observeAdditionalProductListViewModel(viewModel);
    }

    public void observeAdditionalProductListViewModel(AdditionalProductsViewModel viewModel) {
        bindViewModal();
    }

    private void bindViewModal() {
        additionalProductListAdapter.setAdditionalProductList(filterAdditionalProducts());
        addedAdditionalProductListAdapter.setAddedAdditionalProductList(selectedContract.addedAdditionalProducts);
        binding.totalAdditionalProductAmount.setText(calculateAdditionalProductsTotalAmount());
    }

    public void changeAdditionalProductSelectionByParentOrChild(AdditionalProduct item) {
        if (!item.isChecked) {
            return;
        }

        selectedContract.additionalProductRules.forEach(rule -> {
            if (rule.productId.equals(item.productId)) {
                filterAdditionalProducts().forEach(product -> {
                    if (product.productId.equals(rule.parentProductId)) {
                        product.value = 0;
                        product.isChecked = false;
                        prepareUpdatedProduct(product);
                    }
                });
            }
            if (rule.parentProductId.equals(item.productId)) {
                filterAdditionalProducts().forEach(product -> {
                    if (product.productId.equals(rule.productId)) {
                        product.value = 0;
                        product.isChecked = false;
                        prepareUpdatedProduct(product);
                    }
                });
            }
        });
    }

    RELATION isParentOrChild(String productId) {
        for (AdditionalProductRules item : selectedContract.additionalProductRules) {
            if (item.parentProductId.equals(productId)) {
                return RELATION.PARENT;
            } else if (item.productId.equals(productId)) {
                return RELATION.CHILD;
            }
        }

        return RELATION.NONE;
    }

    @Override
    public void navigate() {
        super.navigate();
        //selectedContract.totalAdditionalProductsAmount = totalBasketAmount;
        AdditionalServicesFragment additionalProductsFragment = AdditionalServicesFragment.forSelectedContract(selectedContract);
        super.changeFragment(additionalProductsFragment);
    }

    public void prepareAdditionalProductsRecyclerView() {
        additionalProductListAdapter = new AdditionalProductListAdapter(additionalProductClickCallback, plusTextClickCallback, minusTextClickCallback);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.additionalProductList.setLayoutManager(layoutManager);
        binding.additionalProductList.setAdapter(additionalProductListAdapter);
    }

    public void prepareAddedAdditionalProductsRecyclerView() {
        addedAdditionalProductListAdapter = new AddedAdditionalProductListAdapter();
        //addedAdditionalProductListAdapter.setAddedAdditionalProductList(addedAdditionalProducts);
        binding.addedAdditionalProductList.setAdapter(addedAdditionalProductListAdapter);
        binding.addedAdditionalProductList.addItemDecoration(new DividerItemDecoration(binding.addedAdditionalProductList.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void prepareUpdatedProduct(AdditionalProduct item) {
        for (AdditionalProduct additionalProduct : selectedContract.addedAdditionalProducts) {
            if (additionalProduct.productId.equals(item.productId)) {
                selectedContract.addedAdditionalProducts.remove(additionalProduct);
                break;
            }
        }

        for (AdditionalProduct initialObject : selectedContract.initialAdditionalProductList) {
            if (initialObject.productId.equals(item.productId)) {
                if (item.value - initialObject.value != 0) {
                    AdditionalProduct additionalProduct = new AdditionalProduct(item);
                    additionalProduct.value = item.value - initialObject.value;
                    selectedContract.addedAdditionalProducts.add(additionalProduct);
                    break;
                }
            }
        }

        binding.totalAdditionalProductAmount.setText(calculateAdditionalProductsTotalAmount());
        addedAdditionalProductListAdapter.setAddedAdditionalProductList(selectedContract.addedAdditionalProducts);
        addedAdditionalProductListAdapter.notifyDataSetChanged();
        additionalProductListAdapter.notifyDataSetChanged();
    }

    private List<AdditionalProduct> filterAdditionalProducts() {
        List<AdditionalProduct> tempList = new ArrayList<>();
        for (AdditionalProduct item : selectedContract.additionalProductList) {
            if (item.productType == EnumUtils.AdditionalProductType.PRODUCT.getIntValue() &&
                    item.showonWeb || (!item.showonWeb && item.value > 0)) {
                tempList.add(item);
            }
        }

        return tempList;
    }

    String calculateAdditionalProductsTotalAmount() {
        return String.format(Locale.getDefault(), "%.02f TL", CommonMethods.instance.calculateAddedAdditionalProductsAmount(selectedContract.addedAdditionalProducts));
    }

    ArrayList<AdditionalProduct> getInitialAdditionalProducts() {
        String json = SharedPrefUtils.instance.getJsonArrayFromSharedPrefs(getContext(), "initial_additional_products");
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<ArrayList<AdditionalProduct>>() {
        }.getType());
    }

    public enum RELATION {
        PARENT(1),
        CHILD(2),
        NONE(3);

        private final int intValue;

        RELATION(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }
}
