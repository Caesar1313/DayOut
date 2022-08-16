package com.example.dayout.ui.fragments.trips.details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.dayout.R;
import com.example.dayout.config.AppSharedPreferences;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.models.room.roadMapRoom.database.RoadMapDatabase;
import com.example.dayout.models.room.tripsRoom.database.TripsDatabase;
import com.example.dayout.models.trip.TripData;
import com.example.dayout.models.trip.TripDetailsModel;
import com.example.dayout.models.trip.roadMap.RoadMapData;
import com.example.dayout.models.trip.tripType.TripType;
import com.example.dayout.ui.dialogs.notify.ErrorDialog;
import com.example.dayout.ui.dialogs.notify.LoadingDialog;
import com.example.dayout.ui.dialogs.notify.MessageDialog;
import com.example.dayout.viewModels.TripViewModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.example.dayout.config.AppConstants.MAIN_FRC;

@SuppressLint("NonConstantResourceId")
public class OldTripDetailsFragment extends Fragment {

    View view;

    @BindView(R.id.old_trip_details_back_arrow)
    ImageButton oldTripDetailsBackArrow;

    @BindView(R.id.old_trip_details_title)
    TextView oldTripDetailsTitle;

    @BindView(R.id.old_trip_details_type)
    TextView oldTripDetailsType;

    @BindView(R.id.old_trip_details_stops)
    TextView oldTripDetailsStops;

    @BindView(R.id.old_trip_details_date)
    TextView oldTripDetailsDate;

    @BindView(R.id.old_trip_details_price)
    TextView oldTripDetailsPrice;

    @BindView(R.id.old_trip_details_expire_date)
    TextView oldTripDetailsExpireDate;

    @BindView(R.id.old_trips_end_booking_date)
    TextView oldTripsEndBookingDate;

    @BindView(R.id.old_trip_details_roadmap)
    TextView oldTripDetailsRoadMap;

    @BindView(R.id.old_trip_details_roadmap_front_arrow)
    ImageButton oldTripDetailsRoadMapFrontArrow;

    @BindView(R.id.old_trip_details_passengers_count)
    TextView oldTripDetailsPassengersCount;

    @BindView(R.id.old_trip_details_ratingBar)
    RatingBar oldTripDetailsRatingBar;

    float tripRating = 0;
    boolean firstAccess = true;

    TripData data;

    LoadingDialog loadingDialog;

    public OldTripDetailsFragment(TripData data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_old_trip_details, container, false);
        ButterKnife.bind(this, view);
        initViews();
        getDataFromApi();
        return view;
    }

    private void initViews() {
        loadingDialog = new LoadingDialog(requireContext());
        oldTripDetailsBackArrow.setOnClickListener(onBackClicked);
        oldTripDetailsRoadMap.setOnClickListener(onRoadMapClicked);
        oldTripDetailsRoadMapFrontArrow.setOnClickListener(onRoadMapClicked);
        oldTripDetailsRatingBar.setOnRatingBarChangeListener(onRatingBarChanged);
    }

    private void setData(TripDetailsModel model) {
        oldTripDetailsType.setText(getTypes(model.data.types));
        oldTripDetailsTitle.setText(model.data.title);
        oldTripDetailsDate.setText(model.data.begin_date);
        oldTripDetailsStops.setText(model.data.stopsToDetails);
        oldTripDetailsExpireDate.setText(model.data.expire_date);
        oldTripDetailsPrice.setText(String.valueOf(model.data.price));
        oldTripsEndBookingDate.setText(model.data.end_booking);
        oldTripDetailsPassengersCount.setText(String.valueOf(model.data.customer_trips_count));
        oldTripDetailsRatingBar.setRating(model.data.customer_trips.get(0).rate);
    }

    //test this.
    private void setRoomData(TripData data) {
        oldTripDetailsType.setText(getTypes(data.types));
        oldTripDetailsTitle.setText(data.title);
        oldTripDetailsDate.setText(data.begin_date);
        oldTripDetailsStops.setText(data.stopsToDetails);
        oldTripDetailsExpireDate.setText(data.expire_date);
        oldTripDetailsPrice.setText(String.valueOf(data.price));
        oldTripsEndBookingDate.setText(data.end_booking);
        oldTripDetailsPassengersCount.setText(String.valueOf(data.customer_trips_count));
        oldTripDetailsRatingBar.setRating(data.customer_trips.get(0).rate);
    }

    private String getTypes(ArrayList<TripType> types) {
        String tripTypes = "";

        for (int i = 0; i < types.size(); i++) {
            if (i != 0) {
                tripTypes += ", " + types.get(i).name;
            } else tripTypes += types.get(i).name;
        }

        return tripTypes;
    }

    private void getDataFromRoom(){
        TripsDatabase.getINSTANCE(requireContext())
                .iTrip()
                .getTripById(data.id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TripData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull TripData data) {
                        setRoomData(data);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void getDataFromApi() {
        loadingDialog.show();
        TripViewModel.getINSTANCE().getTripDetails(data.id);
        TripViewModel.getINSTANCE().tripDetailsMutableLiveData.observe(requireActivity(), tripDetailsObserver);
    }

    private final Observer<Pair<TripDetailsModel, String>> tripDetailsObserver = new Observer<Pair<TripDetailsModel, String>>() {
        @Override
        public void onChanged(Pair<TripDetailsModel, String> tripDetailsModelStringPair) {
            loadingDialog.dismiss();
            if (tripDetailsModelStringPair != null) {
                if (tripDetailsModelStringPair.first != null) {
                    setData(tripDetailsModelStringPair.first);
                    data = tripDetailsModelStringPair.first.data;
                } else {
                    getDataFromRoom();
                    new ErrorDialog(requireContext(), tripDetailsModelStringPair.second).show();
                }
            } else {
                getDataFromRoom();
                new ErrorDialog(requireContext(), getResources().getString(R.string.error_connection)).show();
            }
        }
    };

    private JsonObject getRateData() {
        JsonObject object = new JsonObject();
        object.addProperty("trip_id", data.id);
        object.addProperty("rate", tripRating);

        return object;
    }

    private void rateTrip() {
        loadingDialog.show();
        TripViewModel.getINSTANCE().rateTrip(getRateData());
        TripViewModel.getINSTANCE().rateTripMutableLiveData.observe(requireActivity(), rateTripObserver);
    }

    private final Observer<Pair<ResponseBody, String>> rateTripObserver = new Observer<Pair<ResponseBody, String>>() {
        @Override
        public void onChanged(Pair<ResponseBody, String> responseBodyStringPair) {
            loadingDialog.dismiss();
            if (responseBodyStringPair != null) {
                if (responseBodyStringPair.first != null) {
                    new MessageDialog(requireContext(), getResources().getString(R.string.trip_rated)).show();
                } else
                    new ErrorDialog(requireContext(), responseBodyStringPair.second).show();
            } else
                new ErrorDialog(requireContext(), getResources().getString(R.string.error_connection)).show();
        }
    };

    private final View.OnClickListener onBackClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FN.popTopStack(requireActivity());
        }
    };

    private final View.OnClickListener onRoadMapClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FN.addFixedNameFadeFragment(MAIN_FRC,requireActivity(),new RoadMapFragment(data.id));
        }
    };

    private final RatingBar.OnRatingBarChangeListener onRatingBarChanged = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if(AppSharedPreferences.GET_ACC_TOKEN().isEmpty()) {
                NoteMessage.showSnackBar(requireActivity(),getString(R.string.presmission_deny));
                return;
            }

            if(!firstAccess) {
                tripRating = rating;
                rateTrip();
            } else
                firstAccess = false;
        }
    };
}
