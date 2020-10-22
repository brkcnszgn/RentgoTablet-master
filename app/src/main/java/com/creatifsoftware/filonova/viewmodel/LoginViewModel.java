package com.creatifsoftware.filonova.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.creatifsoftware.filonova.model.User;
import com.creatifsoftware.filonova.model.request.UserRequest;
import com.creatifsoftware.filonova.model.response.MasterDataResponse;
import com.creatifsoftware.filonova.service.repository.ContractRepository;

import javax.inject.Inject;

/**
 * Created by kerembalaban on 21.02.2019 at 01:17.
 */
public class LoginViewModel extends AndroidViewModel {
    private final ContractRepository contractRepository;

    @Inject
    public LoginViewModel(@NonNull ContractRepository contractRepository, @NonNull Application application) {
        super(application);

        this.contractRepository = contractRepository;
    }

    public LiveData<User> getLoginObervable(UserRequest userRequest) {
        return contractRepository.login(userRequest);
    }

    public LiveData<MasterDataResponse> getMasterDataObservable() {
        return contractRepository.getMasterData();
    }
}
