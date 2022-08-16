package com.example.dayout.adapters.recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dayout.R;
import com.example.dayout.config.AppSharedPreferences;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.models.popualrPlace.PlaceData;
import com.example.dayout.models.popualrPlace.PopularPlacePhoto;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.ui.dialogs.notify.ErrorDialog;
import com.example.dayout.ui.fragments.home.PlaceInfoFragment;
import com.example.dayout.viewModels.PlaceViewModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


import static com.example.dayout.api.ApiClient.BASE_URL;
import static com.example.dayout.config.AppConstants.MAIN_FRC;
import static com.example.dayout.config.AppSharedPreferences.GET_USER_ID;
import static com.example.dayout.helpers.view.ImageViewer.IMAGE_BASE_URL;

public class HomePlaceAdapter extends RecyclerView.Adapter<HomePlaceAdapter.ViewHolder> {

    private static final String TAG = "Home place Adapter";
    List<PlaceData> list;
    Context context;

    public HomePlaceAdapter(List<PlaceData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void insertRoomObject(PlaceData popularPlace) {

        // insert object in room database
        ((MainActivity) context).roomPopularPlaces
                .insertPopularPlace(popularPlace)
                .subscribeOn(Schedulers.computation()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.toString());
            }
        });
    }

    public void refreshList(List<PlaceData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        view.findViewById(R.id.delete_favorite_btn).setVisibility(View.GONE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        insertRoomObject(list.get(position));


        holder.placeName.setText(list.get(position).name);
        holder.shortDescrption.setText(list.get(position).summary);
        holder.bindImageSlider(list.get(position).photos);

        if (list.get(position).favorites_count == 1) {
            holder.addFavoriteButton.setVisibility(View.GONE);
        } else holder.addFavoriteButton.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NonConstantResourceId")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.place_name)
        TextView placeName;

        @BindView(R.id.short_descrption)
        TextView shortDescrption;

        @BindView(R.id.image_slider)
        ImageSlider imageSlider;

        @BindView(R.id.add_favorite_btn)
        ImageButton addFavoriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
            addFavoriteButton.setOnClickListener(onAddFavoriteClicked);
        }

        private final View.OnClickListener onAddFavoriteClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppSharedPreferences.GET_ACC_TOKEN().isEmpty()) {
                    NoteMessage.showSnackBar((MainActivity) context, context.getString(R.string.presmission_deny));
                    return;
                }

                addFavoriteButton.setEnabled(false);
                PlaceViewModel.getINSTANCE().addToFavorite(getJsonObject());
                PlaceViewModel.getINSTANCE().successfulMutableLiveData.observe((MainActivity) context, successfulObserver);
            }
        };

        private Observer<Pair<Boolean, String>> successfulObserver = new Observer<Pair<Boolean, String>>() {
            @Override
            public void onChanged(Pair<Boolean, String> booleanStringPair) {
                addFavoriteButton.setEnabled(true);
                if (booleanStringPair != null) {
                    if (booleanStringPair.first != null && booleanStringPair.first) {
                        addFavoriteButton.setVisibility(View.GONE);
                        NoteMessage.showSnackBar((MainActivity) context, context.getResources().getString(R.string.added_successfully));
                    } else new ErrorDialog(context, booleanStringPair.second).show();
                } else
                    new ErrorDialog(context, context.getResources().getString(R.string.error_connection)).show();
            }
        };

        private JsonObject getJsonObject() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", GET_USER_ID());
            jsonObject.addProperty("placeId", list.get(getAdapterPosition()).id);
            return jsonObject;
        }

        @Override
        public void onClick(View v) {
            FN.addFixedNameFadeFragment(MAIN_FRC, (MainActivity) context, new PlaceInfoFragment(list.get(getAdapterPosition()).id));
        }

        private void bindImageSlider(List<PopularPlacePhoto> photos) {
            List<SlideModel> slideModels = new ArrayList<>();
            for (PopularPlacePhoto ph : photos) {
                slideModels.add(new SlideModel(IMAGE_BASE_URL + ph.path
                        , ScaleTypes.FIT));
            }

            imageSlider.setImageList(slideModels);

            imageSlider.setScrollBarFadeDuration(10000);
        }
    }
}
