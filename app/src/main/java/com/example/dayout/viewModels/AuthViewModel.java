package com.example.dayout.viewModels;

import android.util.Pair;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dayout.api.ApiClient;
import com.example.dayout.models.LoginModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";
    private final ApiClient apiClient = new ApiClient();

    private static AuthViewModel instance;

    public static AuthViewModel getINSTANCE() {
        if (instance == null) {
            instance = new AuthViewModel();
        }
        return instance;
    }


    public MutableLiveData<Pair<LoginModel,String>> loginMutableLiveData;


    public void login(JsonObject jsonObject){
        loginMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().login(jsonObject).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()){
                    loginMutableLiveData.setValue(new Pair<>(response.body(),null));
                }
                else {
                    loginMutableLiveData.setValue(new Pair<>(null,response.body().message));
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                loginMutableLiveData.setValue(null);
            }
        });
    }


}