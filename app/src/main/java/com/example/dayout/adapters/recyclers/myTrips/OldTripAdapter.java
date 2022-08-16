package com.example.dayout.adapters.recyclers.myTrips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dayout.R;
import com.example.dayout.config.AppSharedPreferences;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.models.trip.TripData;
import com.example.dayout.models.trip.TripPhotoData;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.ui.fragments.trips.myTrip.FilterFragment;
import com.example.dayout.ui.fragments.trips.details.OldTripDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dayout.api.ApiClient.BASE_URL;
import static com.example.dayout.config.AppConstants.MAIN_FRC;
import static com.example.dayout.helpers.view.ImageViewer.IMAGE_BASE_URL;

public class OldTripAdapter extends RecyclerView.Adapter<OldTripAdapter.ViewHolder> {

    List<TripData> list;
    Context context;

    public OldTripAdapter(List<TripData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void refresh(List<TripData> list) {
        this.list = list;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_old_trip_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tripStops ="";
        holder.title.setText(list.get(position).title);
        holder.description.setText(list.get(position).description);
        holder.date.setText(list.get(position).begin_date);
        holder.passengersCount.setText(String.valueOf(list.get(position).customer_trips.size()));
        holder.bindImageSlider(list.get(position).trip_photos);

        for (int i = 0; i < list.get(position).place_trips.size(); i++) {
            if (i != 0) {
                tripStops += ", " + list.get(position).place_trips.get(i).place.name;
            } else if (i == 0)
                tripStops += list.get(position).place_trips.get(i).place.name;
            ;
        }

        holder.stops = tripStops;
        holder.tripStops.setText(tripStops);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.old_trip_title)
        TextView title;

        @BindView(R.id.old_trip_passengers_count)
        TextView passengersCount;

        @BindView(R.id.old_trip_date)
        TextView date;

        @BindView(R.id.old_trip_description)
        TextView description;

        @BindView(R.id.old_trip_image_slider)
        ImageSlider imageSlider;

        @BindView(R.id.old_trip_stops)
        TextView tripStops;

        String stops;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {

            if (!FilterFragment.isFilterOpen) {
                TripData data = list.get(getAdapterPosition());
                data.stopsToDetails = stops;
                FN.addFixedNameFadeFragment(MAIN_FRC, (MainActivity) context, new OldTripDetailsFragment(data));
            }
        }

        private void bindImageSlider(List<TripPhotoData> photos) {
            List<SlideModel> slideModels = new ArrayList<>();

            for (TripPhotoData ph : photos) {
                slideModels.add(new SlideModel(IMAGE_BASE_URL+ph.path
                        , ScaleTypes.FIT));
            }

            imageSlider.setImageList(slideModels);

            imageSlider.setScrollBarFadeDuration(10000);
        }
    }
}


