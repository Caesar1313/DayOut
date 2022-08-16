package com.example.dayout.adapters.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dayout.R;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.models.SearchPlaceModel;
import com.example.dayout.models.popualrPlace.PlaceData;
import com.example.dayout.models.popualrPlace.PopularPlacePhoto;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.ui.fragments.home.PlaceInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dayout.api.ApiClient.BASE_URL;
import static com.example.dayout.config.AppConstants.MAIN_FRC;
import static com.example.dayout.helpers.view.ImageViewer.IMAGE_BASE_URL;

public class ExplorePlaceAdapter extends RecyclerView.Adapter<ExplorePlaceAdapter.ViewHolder> {

    List<PlaceData> list;
    Context context;

    public ExplorePlaceAdapter(List<PlaceData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void refresh(List<PlaceData> list) {
        this.list = list;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ExplorePlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        view.findViewById(R.id.delete_favorite_btn).setVisibility(View.GONE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExplorePlaceAdapter.ViewHolder holder, int position) {
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
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FN.addFixedNameFadeFragment(MAIN_FRC, (MainActivity) context, new PlaceInfoFragment(list.get(getAdapterPosition()).id));
        }

        private void bindImageSlider(List<PopularPlacePhoto> photos) {

            if (photos == null) return;

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
