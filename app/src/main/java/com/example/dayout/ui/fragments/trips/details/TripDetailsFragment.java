package com.example.dayout.ui.fragments.trips.details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.dayout.R;
import com.example.dayout.config.AppSharedPreferences;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.models.popualrPlace.PlaceData;
import com.example.dayout.models.room.tripsRoom.database.TripsDatabase;
import com.example.dayout.models.trip.TripData;
import com.example.dayout.models.trip.TripDetailsModel;
import com.example.dayout.models.trip.place.PlaceTripData;
import com.example.dayout.models.trip.tripType.TripType;
import com.example.dayout.ui.dialogs.notify.ErrorDialog;
import com.example.dayout.ui.dialogs.notify.LoadingDialog;
import com.example.dayout.ui.dialogs.notify.MessageDialog;
import com.example.dayout.ui.dialogs.notify.WarningDialog;
import com.example.dayout.viewModels.TripViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.dayout.config.AppConstants.MAIN_FRC;

@SuppressLint("NonConstantResourceId")
public class TripDetailsFragment extends Fragment {

    View view;

    @BindView(R.id.trip_details_back_arrow)
    ImageButton tripDetailsBackArrow;

    @BindView(R.id.trip_details_delete_icon)
    ImageButton tripDetailsDeleteIcon;

    @BindView(R.id.trip_details_title)
    TextView tripDetailsTitle;

    @BindView(R.id.trip_details_type)
    TextView tripDetailsType;

    @BindView(R.id.trip_details_stops)
    TextView tripDetailsStops;

    @BindView(R.id.trip_details_date)
    TextView tripDetailsDate;

    @BindView(R.id.trip_details_price)
    TextView tripDetailsPrice;

    @BindView(R.id.trip_details_expire_date)
    TextView tripDetailsExpireDate;

    @BindView(R.id.trips_end_booking_date)
    TextView tripsEndBookingDate;

    @BindView(R.id.trip_details_roadmap)
    TextView tripDetailsRoadMap;

    @BindView(R.id.trip_details_roadmap_front_arrow)
    ImageButton tripDetailsRoadMapFrontArrow;

    @BindView(R.id.trip_details_passengers_count)
    TextView tripDetailsPassengersCount;

    @BindView(R.id.book_trip_button)
    Button bookTripButton;

    boolean isPost;

    TripData data;

    LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trip_details, container, false);
        ButterKnife.bind(this, view);
        initViews();
        getDataFromApi();
        return view;
    }

    public TripDetailsFragment(TripData data, boolean isPost){
        this.data = data;
        this.isPost = isPost;
    }


    private void initViews() {
        loadingDialog = new LoadingDialog(requireContext());
        tripDetailsBackArrow.setOnClickListener(onBackClicked);
        tripDetailsDeleteIcon.setOnClickListener(onDeleteClicked);
        tripDetailsRoadMap.setOnClickListener(onRoadMapClicked);
        tripDetailsRoadMapFrontArrow.setOnClickListener(onRoadMapClicked);
        if (isPost) {
            tripDetailsDeleteIcon.setVisibility(View.GONE);
            bookTripButton.setVisibility(View.VISIBLE);
            bookTripButton.setOnClickListener(onBookClicked);
        }
    }

    private String getTypes(ArrayList<TripType> types){
        String tripTypes = "";

        for(int i = 0; i < types.size(); i++){
            if (i != 0) {
                tripTypes += ", " + types.get(i).name;
            } else if(i == 0)
                tripTypes += types.get(i).name;
        }

        return tripTypes;
    }

    private void setData(TripDetailsModel model){
        tripDetailsType.setText(getTypes(model.data.types));
        tripDetailsTitle.setText(model.data.title);
        tripDetailsDate.setText(model.data.begin_date);
       tripDetailsStops.setText(getTripStops(model));
        tripDetailsExpireDate.setText(model.data.expire_date);
        tripDetailsPrice.setText(String.valueOf(model.data.price));
        tripsEndBookingDate.setText(model.data.end_booking);
        tripDetailsPassengersCount.setText(String.valueOf(model.data.customer_trips_count));

        //passenger has booked this trip.
        System.out.println(model.data.is_in_trip);
        if(model.data.is_in_trip){
            bookTripButton.setText(R.string.cancel_booking);
        }
    }

    private String getTripStops(TripDetailsModel model){
        StringBuilder stringBuilder = new StringBuilder();
        for(PlaceTripData place :model.data.place_trips)
            stringBuilder.append(place.place.name);

        return stringBuilder.toString();
    }

    private void setRoomData(TripData data) {
        tripDetailsType.setText(getTypes(data.types));
        tripDetailsTitle.setText(data.title);
        tripDetailsDate.setText(data.begin_date);
        tripDetailsStops.setText(data.stopsToDetails);
        tripDetailsExpireDate.setText(data.expire_date);
        tripDetailsPrice.setText(String.valueOf(data.price));
        tripsEndBookingDate.setText(data.end_booking);
        tripDetailsPassengersCount.setText(String.valueOf(data.customer_trips_count));

        //passenger has booked this trip.
        System.out.println(data.is_in_trip);
        if(data.is_in_trip){
            bookTripButton.setText(R.string.cancel_booking);
        }
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

    private void getDataFromApi(){
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
                    //trip is active
                    if (data.isActive)
                        tripDetailsDeleteIcon.setVisibility(View.GONE);
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

    private final View.OnClickListener onBackClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FN.popTopStack(requireActivity());
        }
    };


    private final View.OnClickListener onDeleteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new WarningDialog(requireContext(), getResources().getString(R.string.canceling_reservation), true, data.id).show();
        }
    };

    private final View.OnClickListener onRoadMapClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FN.addFixedNameFadeFragment(MAIN_FRC,requireActivity(),new RoadMapFragment(data.id));
        }
    };

    private final View.OnClickListener onBookClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(AppSharedPreferences.GET_ACC_TOKEN().isEmpty()){
                NoteMessage.showSnackBar(requireActivity(),getString(R.string.presmission_deny));
                return;
            }

            if (bookTripButton.getText().toString().equals(getResources().getString(R.string.book_trip))) {
                new MessageDialog(requireContext(), getString(R.string.booking_nots)).show();
                FN.addFixedNameFadeFragment(MAIN_FRC, requireActivity(), new BookTripFragment(data.id));
            } else if (bookTripButton.getText().toString().equals(getResources().getString(R.string.cancel_booking))){
                new WarningDialog(requireContext(), getResources().getString(R.string.canceling_reservation), true, data.id).show();
            }
        }
    };
}