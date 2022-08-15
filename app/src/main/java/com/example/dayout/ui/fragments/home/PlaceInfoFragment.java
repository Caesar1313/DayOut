package com.example.dayout.ui.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dayout.R;
import com.example.dayout.models.popualrPlace.PlaceData;
import com.example.dayout.models.popualrPlace.PopularPlacePhoto;
import com.example.dayout.models.room.popularPlaceRoom.databases.PopularPlaceDataBase;
import com.example.dayout.models.trip.place.PlaceDetailsModel;
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

import static com.example.dayout.api.ApiClient.BASE_URL;
import static com.example.dayout.helpers.view.ImageViewer.IMAGE_BASE_URL;

@SuppressLint("NonConstantResourceId")
public class PlaceInfoFragment extends Fragment {

    View view;

    @BindView(R.id.image_slider)
    ImageSlider imageSlider;
    @BindView(R.id.place_full_info_txt)
    TextView placeFullInfoTxt;
    @BindView(R.id.short_descrption)
    TextView shortDescrption;

    PlaceData placeData;


//    public PlaceInfoFragment(PopularPlaceData popularPlaceData) {
//        this.popularPlaceData = popularPlaceData;
//    }

    int placeId;

    public PlaceInfoFragment(int placeId){
        this.placeId = placeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_place_info, container, false);
        ButterKnife.bind(this, view);

        getDataFromApi();
        return view;
    }

    @Override
    public void onStart() {
        ((MainActivity) requireActivity()).hideBottomBar();
        super.onStart();
    }


    private void initInfo(PlaceData placeData) {
        initImageSlider(placeData);
        placeFullInfoTxt.setText(placeData.description);
        shortDescrption.setText(placeData.summary);
    }

    private void initImageSlider(PlaceData placeData) {
        List<SlideModel> slideModels = new ArrayList<>();

        for (PopularPlacePhoto ph : placeData.photos) {
            slideModels.add(new SlideModel(IMAGE_BASE_URL + ph.path
                    , ScaleTypes.FIT));
        }
        imageSlider.setImageList(slideModels);
        imageSlider.setScrollBarFadeDuration(10000);
    }

    private void getDataFromApi(){
        PlaceViewModel.getINSTANCE().getPlaceDetails(placeId);
        PlaceViewModel.getINSTANCE().placeDetailsMutableLiveData.observe(requireActivity(),placeDetailsModel);
    }
    private final Observer<Pair<PlaceDetailsModel,String>> placeDetailsModel = new Observer<Pair<PlaceDetailsModel, String>>() {
        @Override
        public void onChanged(Pair<PlaceDetailsModel, String> placeDetailsModelStringPair) {
            if (placeDetailsModelStringPair != null){
                if (placeDetailsModelStringPair.first != null){
                 initInfo(placeDetailsModelStringPair.first.data);
                }
                else {
                    getDataFromRoom();
                    new ErrorDialog(requireContext(),placeDetailsModelStringPair.second).show();
                }
            }
            else {
                getDataFromRoom();
                new ErrorDialog(requireContext(),getResources().getString(R.string.error_connection)).show();
            }
        }
    };

    private void getDataFromRoom(){
        PopularPlaceDataBase.getINSTANCE(requireContext())
                .iPopularPlaces()
                .getPlace(placeId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PlaceData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull PlaceData data) {
                        initInfo(data);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getter place roomDB", "onError: " + e.toString());
                    }
                });
    }

}