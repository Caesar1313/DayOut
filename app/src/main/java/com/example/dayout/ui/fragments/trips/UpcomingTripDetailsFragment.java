package com.example.dayout.ui.fragments.trips;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dayout.R;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.models.trip.TripModel;
import com.example.dayout.ui.dialogs.WarningDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class UpcomingTripDetailsFragment extends Fragment {

    View view;

    @BindView(R.id.upcoming_trip_details_back_arrow)
    ImageButton upcomingTripDetailsBackArrow;

    @BindView(R.id.upcoming_trip_details_delete_icon)
    ImageButton upcomingTripDetailsDeleteIcon;

    @BindView(R.id.upcoming_trip_details_title)
    TextView upcomingTripDetailsTitle;

    @BindView(R.id.upcoming_trip_details_type)
    TextView upcomingTripDetailsType;

    @BindView(R.id.upcoming_trip_details_stops)
    TextView upcomingTripDetailsStops;

    @BindView(R.id.upcoming_trip_details_date)
    TextView upcomingTripDetailsDate;

    @BindView(R.id.upcoming_trip_details_price)
    TextView upcomingTripDetailsPrice;

    @BindView(R.id.upcoming_trip_details_end_booking_date)
    TextView upcomingTripDetailsEndBookingDate;

    @BindView(R.id.upcoming_trips_end_confirmation_date)
    TextView upcomingTripsEndConfirmationDate;

    @BindView(R.id.upcoming_trip_details_roadmap)
    TextView upcomingTripDetailsRoadMap;

    @BindView(R.id.upcoming_trip_details_roadmap_front_arrow)
    ImageButton upcomingTripDetailsRoadMapFrontArrow;

    @BindView(R.id.upcoming_trip_details_passengers_count)
    TextView upcomingTripDetailsPassengersCount;

    TripModel.Data data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upcoming_trip_details, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public UpcomingTripDetailsFragment(TripModel.Data data){
        this.data = data;
    }


    private void initViews(){
        setData();
        upcomingTripDetailsBackArrow.setOnClickListener(onBackClicked);
        upcomingTripDetailsDeleteIcon.setOnClickListener(onDeleteClicked);
        upcomingTripDetailsRoadMap.setOnClickListener(onRoadMapClicked);
        upcomingTripDetailsRoadMapFrontArrow.setOnClickListener(onRoadMapClicked);

        //trip is active
        //FIXME: Always returning 1 - Backend team has to fix it.
        if(data.trip_status_id == 3)
            upcomingTripDetailsDeleteIcon.setVisibility(View.GONE);
    }

    private void setData(){
        upcomingTripDetailsTitle.setText(data.title);
        upcomingTripDetailsDate.setText(data.begin_date);
        upcomingTripDetailsEndBookingDate.setText(data.end_booking);
        upcomingTripDetailsPrice.setText(String.valueOf(data.price));
        upcomingTripsEndConfirmationDate.setText(data.expire_date);
        //upcomingTripDetailsPassengersCount.setText(String.valueOf(data.customer_trips.size()));
    }

    private final View.OnClickListener onBackClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FN.popTopStack(requireActivity());
        }
    };

    private final View.OnClickListener onEditClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener onDeleteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new WarningDialog(requireContext(), getResources().getString(R.string.canceling_reservation)).show();
        }
    };

    private final View.OnClickListener onRoadMapClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener onCheckClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener onBeginTripClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
