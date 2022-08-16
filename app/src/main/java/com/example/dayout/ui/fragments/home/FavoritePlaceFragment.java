package com.example.dayout.ui.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dayout.R;
import com.example.dayout.adapters.recyclers.FavoritePlaceAdapter;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.models.popualrPlace.PlaceData;
import com.example.dayout.models.popualrPlace.PlaceModel;
import com.example.dayout.models.room.popularPlaceRoom.databases.PopularPlaceDataBase;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.ui.dialogs.notify.ErrorDialog;
import com.example.dayout.viewModels.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("NonConstantResourceId")
public class FavoritePlaceFragment extends Fragment {


    View view;
    FavoritePlaceAdapter favoritePlaceAdapter;

    @BindView(R.id.arrow_back)
    ImageButton arrowBack;

    @BindView(R.id.favorite_place_rc)
    RecyclerView favoritePlaceRc;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_text)
    TextView emptyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite_place, container, false);
        ButterKnife.bind(this, view);
        initView();
        getDataFromApi();
        return view;
    }

    @Override
    public void onStart() {
        ((MainActivity) requireActivity()).hideBottomBar();
        super.onStart();
    }

    @Override
    public void onStop() {
        ((MainActivity) requireActivity()).showDrawerButton();
        ((MainActivity) requireActivity()).showBottomBar();
        super.onStop();
    }

    private void initView() {
        initRc();
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        arrowBack.setOnClickListener(v -> FN.popTopStack(requireActivity()));
    }

    private void  initRc(){
        favoritePlaceRc.setLayoutManager(new LinearLayoutManager(requireContext()));
        favoritePlaceAdapter  = new FavoritePlaceAdapter(new ArrayList<>(),requireContext());
        favoritePlaceRc.setAdapter(favoritePlaceAdapter);
    }

    private void setAsFavorite(List<PlaceData> places) {
        for (PlaceData place : places) {
            place.isFavorite = true;
        }
    }

    private void getDataFromRoom() {
        PopularPlaceDataBase.getINSTANCE(requireContext())
                .iPopularPlaces()
                .getFavoritePlaces()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PlaceData>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<PlaceData> data) {
                        favoritePlaceAdapter.refresh(data);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getter favorite places roomDB", "onError: " + e.toString());
                    }
                });
    }

    private void getDataFromApi() {
        PlaceViewModel.getINSTANCE().getFavoritePlaces();
        PlaceViewModel.getINSTANCE().placeMutableLiveData.observe(requireActivity(), favoritePlaceObserver);
    }

    private final Observer<Pair<PlaceModel, String>> favoritePlaceObserver = new Observer<Pair<PlaceModel, String>>() {
        @Override
        public void onChanged(Pair<PlaceModel, String> placeModelStringPair) {
            if (placeModelStringPair != null) {
                if (placeModelStringPair.first != null) {
                    if (placeModelStringPair.first.data.isEmpty())
                        emptyText.setVisibility(View.VISIBLE);
                    else {
                        emptyText.setVisibility(View.GONE);
                        favoritePlaceAdapter.refresh(placeModelStringPair.first.data);
                        setAsFavorite(placeModelStringPair.first.data);
                    }

                } else {
                    getDataFromRoom();
                    new ErrorDialog(requireContext(), placeModelStringPair.second).show();
                }
            } else {
                getDataFromRoom();
                new ErrorDialog(requireContext(), getResources().getString(R.string.error_connection)).show();
            }
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setEnabled(false);
            getDataFromApi();
        }
    };
}